package com.example.demo.scheduling;

import com.example.demo.exceptions.scheduler.AssignmentFailedException;
import com.example.demo.models.*;
import com.example.demo.services.CourseSessionService;
import com.example.demo.services.TimingService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class BacktrackingScheduler implements Scheduler {
    private List<AvailabilityMatrix> availabilityMatricesOfRoomsWithComputers;
    private List<AvailabilityMatrix> availabilityMatricesOfRoomsWithoutComputers;
    private List<AvailabilityMatrix> allAvailabilityMatrices;
    private List<CourseSession> courseSessionsWithComputersNeeded;
    private List<CourseSession> courseSessionsWithoutComputersNeeded;
    private final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private TimeTable timeTable;
    Stack<AssignmentStackEntry> assignmentStack;

    private final TimingService timingService;
    private final CourseSessionService courseSessionService;
    private final GroupAssignmentMap groupAssignmentMap;

    public BacktrackingScheduler(TimingService timingService, CourseSessionService courseSessionService) {
        this.timingService = timingService;
        this.courseSessionService = courseSessionService;
        this.groupAssignmentMap = new GroupAssignmentMap(2);
    }

    /**
     * This class is used to save the current state of the map with all possible candidates during execution of the
     * backtracking assignment algorithm
     */
    private static class AssignmentState {
        Map<CourseSession, List<Candidate>> possibleCandidateMap;
        int index;

        AssignmentState(Map<CourseSession, List<Candidate>> possibleCandidateMap, int index) {
            this.possibleCandidateMap = new HashMap<>(possibleCandidateMap);
            this.index = index;
        }
    }

    /**
     * Initializes the Scheduler with a certain timeTable.
     * The courseSessions are split into ones that need a room with computers and others that don't need a
     * room with computers. Also, the availabilityMatrices are split into ones of computer rooms and others
     * of rooms without computers.
     * @param timeTable to initialize Scheduler with
     */
    public void setTimeTable(TimeTable timeTable){
        this.timeTable = timeTable;
        availabilityMatricesOfRoomsWithComputers = new ArrayList<>();
        availabilityMatricesOfRoomsWithoutComputers = new ArrayList<>();
        allAvailabilityMatrices = new ArrayList<>();
        courseSessionsWithoutComputersNeeded = new ArrayList<>();
        courseSessionsWithComputersNeeded = new ArrayList<>();
        for(RoomTable roomTable : timeTable.getRoomTablesWithComputersAvailable()){
            AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(roomTable);
            availabilityMatricesOfRoomsWithComputers.add(availabilityMatrix);
            allAvailabilityMatrices.add(availabilityMatrix);
        }
        for(RoomTable roomTable : timeTable.getRoomTablesWithoutComputersAvailable()){
            AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(roomTable);
            availabilityMatricesOfRoomsWithoutComputers.add(availabilityMatrix);
            allAvailabilityMatrices.add(availabilityMatrix);
        }
        this.courseSessionsWithComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithComputersNeeded());
        this.courseSessionsWithoutComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithoutComputersNeeded());
        this.assignmentStack = new Stack<>();
    }

    /**
     * Starts the assignment algorithm for all unassigned courseSessions of a timeTable. First, all courseSessions that
     * don't need rooms with computers are processed, then all courseSessions that need computer rooms.
     */
    @Transactional
    public void assignUnassignedCourseSessions(){
        log.info("> Processing courseSessions that don't need computers ...");
        assignCourseSessions(courseSessionsWithoutComputersNeeded, availabilityMatricesOfRoomsWithoutComputers);
        log.info("Finished processing courseSessions that don't need computers");
        log.info("> Processing courseSessions that need computers ...");
        assignCourseSessions(courseSessionsWithComputersNeeded, availabilityMatricesOfRoomsWithComputers);
        log.info("Finished processing courseSessions that need computers");
    }

    /**
     * This method first checks the preconditions, then splits the courseSessions into single, group and split
     * courseSessions and processes them. At the end, it checks if there is an assignment candidate for all courseSessions.
     * If yes, the courseSessions are assigned, if not
     *
     * @param courseSessions to be processed
     * @param availabilityMatrices to be used for assigning the courseSessions
     */
    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        Map<CourseSession, List<Candidate>> possibleCandidatesForCourseSessions = new HashMap<>();
        List<CourseSession> singleCourseSessions;
        List<CourseSession> groupCourseSessions;

        if(!checkPreConditions(courseSessions, availabilityMatrices)){
            log.error("preconditions failed");
            throw new AssignmentFailedException("Preconditions failed");
        }

        singleCourseSessions = filterAndSortSingleCourseSessions(courseSessions);
        groupCourseSessions =filterAndSortGroupCourseSessions(courseSessions);

        prepareSingleCourseSessions(possibleCandidatesForCourseSessions, singleCourseSessions, availabilityMatrices);
        prepareGroupCourseSessions(possibleCandidatesForCourseSessions, groupCourseSessions, availabilityMatrices);

        log.info("Starting assignment");
        try {
            if (processAssignment(possibleCandidatesForCourseSessions)) {
                log.info("Finished processing assignment.");
            }
        } catch (AssignmentFailedException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * This method executes the backtracking algorithm to find an assignment for all courseSessions that fits all
     * constraints.
     * @param possibleCandidatesForCourseSessions map to start the backtracking algorithm from
     * @return true, if the assignment was successful, else false
     */
    private boolean processAssignment(Map<CourseSession, List<Candidate>> possibleCandidatesForCourseSessions){
        int courseSessionSize = possibleCandidatesForCourseSessions.size();

        if(possibleCandidatesForCourseSessions.isEmpty()){
            return true;
        }

        Stack<AssignmentState> stack = new Stack<>();
        AssignmentState currentState = new AssignmentState(possibleCandidatesForCourseSessions, 0);
        stack.push(currentState);

        Map<CourseSession, List<Candidate>> currentCourseSessionMap;
        int currentIndex;
        int candidateSize;
        Map<CourseSession, List<Candidate>> filteredCourseSessions;

        while(true){
            currentCourseSessionMap = currentState.possibleCandidateMap;
            if(currentCourseSessionMap.size() < courseSessionSize){
                courseSessionSize = currentCourseSessionMap.size();
                log.info("CourseSessions to process: {}", currentCourseSessionMap.size());
            }

            currentIndex = currentState.index;

            Map<CourseSession, List<Candidate>> finalCurrentCourseSessionMap = currentCourseSessionMap;
            CourseSession currentCourseSession = currentCourseSessionMap.keySet().stream()
                    .min(Comparator.comparingInt(c -> finalCurrentCourseSessionMap.get(c).size()))
                    .orElseThrow();
            candidateSize = currentCourseSessionMap.get(currentCourseSession).size();
            //if all candidates of the current state are iterated
            if(currentIndex >= candidateSize){
                if(stack.isEmpty()){
                    return false;
                }
                unassignLatestEntry();
                currentState = stack.pop();
                currentState.index++;
                continue;
            }
            //assign candidate
            Candidate currentCandidate = currentCourseSessionMap.get(currentCourseSession).get(currentIndex);
            currentCandidate.assignToCourseSession(currentCourseSession);
            assignmentStack.push(new AssignmentStackEntry(currentCourseSession, currentCandidate));
            groupAssignmentMap.addEntry(currentCourseSession, currentCandidate);

            //filter candidates
            filteredCourseSessions = filterCandidates(currentCourseSessionMap, currentCourseSession, currentCandidate);

            // check if assignment is finished
            if(filteredCourseSessions.isEmpty()){
                break;
            }

            //check if one of the courseSessions has no candidates after filtering. if yes pop the previous state
            for(Map.Entry<CourseSession, List<Candidate>> entry : filteredCourseSessions.entrySet()){
                if(entry.getValue().isEmpty()){
                    if(stack.isEmpty()){
                        return false;
                    }
                    unassignLatestEntry();
                    currentState = stack.pop();
                    currentState.index++;
                    break;
                }
            }

            AssignmentState newState = new AssignmentState(filteredCourseSessions, 0);
            stack.push(newState);
            currentState = newState;
        }

        //if all courseSessions have been processed, finish assignment
        finishAssignment();
        return true;
    }

    /**
     * This method removes the latest entry from the assignmentStack, the AvailabilityMatrix and the groupAssignmentMap
     */
    private void unassignLatestEntry(){
        AssignmentStackEntry entry = assignmentStack.pop();
        entry.candidate.clearInAvailabilityMatrix();
        groupAssignmentMap.removeEntry(entry.courseSession);
    }

    /**
     * If all courseSessions are present as a AssignmentStackEntry together with a candidate to assign to, the assignment
     * is finalized by creating the timing objects and assigning them to the corresponding courseSession together with
     * the roomTable.
     */
    private void finishAssignment(){
        Iterator<AssignmentStackEntry> iterator = assignmentStack.iterator();
        List<CourseSession> courseSessionsToAssign = new ArrayList<>();
        while(iterator.hasNext()){
            AssignmentStackEntry assignmentStackEntry = iterator.next();
            courseSessionsToAssign.add(assignmentStackEntry.courseSession);
            Timing timing = AvailabilityMatrix.toTiming(assignmentStackEntry.candidate);
            timing = timingService.createTiming(timing);
            assignmentStackEntry.courseSession.setRoomTable(assignmentStackEntry.candidate.getRoomTable());
            assignmentStackEntry.courseSession.setTiming(timing);
            assignmentStackEntry.courseSession.setAssigned(true);
        }
        courseSessionService.saveAll(courseSessionsToAssign);
        assignmentStack.clear();
    }

    /**
     * This method is used to filter all possible candidates after every step of the backtracking algorithm.
     * @param possibleCandidatesForCourseSessions current state of the courseSession map
     * @param currentCourseSession courseSession that was assigned is this step
     * @param currentCandidate candidate the currentCourseSession was assigned to
     * @return the filtered map of courseSessions and their corresponding possible candidates
     */
    private Map<CourseSession, List<Candidate>> filterCandidates(Map<CourseSession, List<Candidate>> possibleCandidatesForCourseSessions,
                                                                 CourseSession currentCourseSession, Candidate currentCandidate){
        Map<CourseSession, List<Candidate>> filteredCandidates = new HashMap<>();

        for (Map.Entry<CourseSession, List<Candidate>> entry : possibleCandidatesForCourseSessions.entrySet()) {
            CourseSession cs = entry.getKey();
            if (cs.equals(currentCourseSession)) {
                continue;
            }
            List<Candidate> candidates = entry.getValue();
            //if the cs is from the same degree and same semester as the current cs
            if (!cs.isAllowedToIntersectWith(currentCourseSession)) {
                //if the cs is not part of the same course as the current cs
                //filter all candidates with intersecting timing
                if (!cs.isFromSameCourse(currentCourseSession)) {
                    candidates = candidates.stream()
                            .filter(c -> !c.intersects(currentCandidate))
                            .collect(Collectors.toList());
                }
                //if it is part of the same course, and it is a group course
                //filter all candidates intersecting in the same roomTable
                else if (cs.isGroupCourse()) {
                    if(!groupAssignmentMap.isLimitExceeded(currentCourseSession, currentCandidate)){
                        candidates = candidates.stream()
                                .filter(c -> !c.intersects(currentCandidate) || !c.isInSameRoom(currentCandidate))
                                .sorted(Comparator.comparing(Candidate::isPreferredSlots).
                                        thenComparingInt(c -> Math.abs(c.getDay() - currentCandidate.getDay())).
                                        thenComparingInt(Candidate::getSlot))
                                .collect(Collectors.toList());
                    }
                    else{
                        candidates = candidates.stream()
                                .filter(c -> !c.intersects(currentCandidate))
                                .collect(Collectors.toList());
                        }
                    }
                //if it is part of the same course, and it is a split course
                //filter all candidates at the same day
                else if (cs.isSplitCourse()) {
                    candidates = candidates.stream()
                            .filter((c) -> (!c.intersects(currentCandidate) && !c.hasSameDay(currentCandidate)) ||
                                    (!c.isInSameRoom(currentCandidate) && !c.hasSameDay(currentCandidate)))
                            .collect(Collectors.toList());
                }
            }
            candidates = candidates.stream()
                    .filter(c -> !c.intersects(currentCandidate) || !c.isInSameRoom(currentCandidate))
                    .collect(Collectors.toList());

            filteredCandidates.put(cs, candidates);
        }
        return filteredCandidates;
    }

    /**
     * This method finds all possible candidates for the single and split courseSessions and collects them in a map
     * @param map to collect courseSessions and their corresponding candidates
     * @param courseSessions to be prepared
     * @param availabilityMatrices to find possible candidates in
     */
    private void prepareSingleCourseSessions(Map<CourseSession, List<Candidate>> map, List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        for(CourseSession courseSession : courseSessions){
            List<Candidate> candidates = new ArrayList<>();
            for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                candidates.addAll(availabilityMatrix.getAllAvailableCandidates(courseSession));
            }
            List<Candidate> filteredCandidates = candidates.stream()
                    .filter(c -> checkConstraintsFulfilled(courseSession, c))
                    .collect(Collectors.toList());
            map.put(courseSession, filteredCandidates);
        }
    }

    /**
     * This method finds all possible candidates for the group courseSessions and collects them in a map
     * @param map to collect courseSessions and their corresponding candidates
     * @param courseSessions to be prepared
     * @param availabilityMatrices to find possible candidates in
     */
    private void prepareGroupCourseSessions(Map<CourseSession, List<Candidate>> map, List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        String currentGroup;
        if(courseSessions == null || courseSessions.isEmpty()){
            return;
        }
        currentGroup = courseSessions.getFirst().getCourseId();
        groupAssignmentMap.initGroup(currentGroup);
        for(CourseSession courseSession: courseSessions){
            List<Candidate> candidates = new ArrayList<>();
            if(!courseSession.getCourseId().equals(currentGroup)){
                currentGroup = courseSession.getCourseId();
                groupAssignmentMap.initGroup(currentGroup);
            }
            for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                candidates.addAll(availabilityMatrix.getAllAvailableCandidates(courseSession));
            }
            List<Candidate> filteredCandidates = candidates.stream()
                    .filter(c -> checkConstraintsFulfilled(courseSession, c))
                    .sorted(Comparator.comparingInt(Candidate::getSlot))
                    .collect(Collectors.toList());
            map.put(courseSession, filteredCandidates);
        }
    }

    /**
     * Filters and sorts a list of courseSessions to obtain only single and split courseSessions sorted descending by
     * duration and descending by numberOfParticipants.
     * @param courseSessions to be filtered and sorted
     * @return sorted list of single courseSessions
     */
    private List<CourseSession> filterAndSortSingleCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(c -> !c.isGroupCourse())
                .sorted(Comparator.comparingInt(CourseSession::getDuration).reversed()
                        .thenComparing(CourseSession::getNumberOfParticipants).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Filters and sorts a list of courseSessions to obtain only group courseSessions sorted descending by duration and
     * ascending by groupID.
     * @param courseSessions to be filtered and sorted
     * @return sorted list of group courseSessions
     */
    private List<CourseSession> filterAndSortGroupCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(CourseSession::isGroupCourse)
                .sorted(Comparator.comparingInt(CourseSession::getDuration).reversed()
                        .thenComparing(CourseSession::getStudyType).reversed()
                        .thenComparing(CourseSession::getSemester).reversed()
                        .thenComparing(CourseSession::getCourseId))
                .collect(Collectors.toList());
    }

    /**
     * Checks preconditions for a certain list of courseSessions and availabilityMatrices before starting assignment.
     * Precondition checks are used to determine in advance if the assignment is even possible considering time needed
     * and time available.
     *
     * @param courseSessions to be checked
     * @param availabilityMatrices to be checked
     * @return true if all checks were successful, false if at least one check failed
     */
    protected boolean checkPreConditions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        log.info("Starting precondition checks ...");
        if(!checkAvailableTime(courseSessions, availabilityMatrices)){
            log.error("Not enough time available to assign all courseSessions");
            return false;
        }
        else{
            log.info("+ Available time check successful");
        }
        if(!checkAvailableTimePerRoomCapacity(courseSessions, availabilityMatrices)){
            log.error("- Not enough time available to assign all courseSessions based on their numberOfParticipants");
            return false;
        }
        else{
            log.info("+ Available time per capacity check successful");
        }
        log.info("Precondition checks successful");
        return true;
    }

    /**
     * Checks if there is enough available time for all numbers of participants. Therefore, it begins with the largest
     * number of participants n and checks if there is enough available time in availabilityMatrices with capacity >= n.
     * This continues for all numbers of participants in descending order.
     *
     * @param courseSessions to be checked
     * @param availabilityMatrices to be checked
     * @return true if there is enough available time for all numbers of participants, false if at least one does not
     * have enough space
     */
    protected boolean checkAvailableTimePerRoomCapacity(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        Set<Integer> numbersOfParticipants = new HashSet<>();
        List<Integer> numbersOfParticipantsSorted = new ArrayList<>();
        long totalTimeAvailable;
        long totalTimeNeeded;
        long totalPreferredTimeAvailable;
        for(CourseSession courseSession : courseSessions){
            numbersOfParticipants.add(courseSession.getNumberOfParticipants());
            numbersOfParticipantsSorted = numbersOfParticipants.stream().sorted().toList();
        }
        for(Integer number : numbersOfParticipantsSorted){
            totalTimeNeeded = courseSessions.stream()
                    .filter(c -> c.getNumberOfParticipants() >= number)
                    .mapToLong(CourseSession::getDuration)
                    .sum();
            totalTimeAvailable = availabilityMatrices.stream()
                    .filter(a -> a.getCapacity() >= number)
                    .mapToLong(AvailabilityMatrix::getTotalAvailableTime)
                    .sum();
            totalPreferredTimeAvailable = availabilityMatrices.stream()
                    .filter(a -> a.getCapacity() >= number)
                    .mapToLong(AvailabilityMatrix::getTotalAvailableTime)
                    .sum();
            if (totalTimeAvailable < totalTimeNeeded) {
                return false;
            }
            if(totalPreferredTimeAvailable < totalTimeNeeded){
                log.info("There is not enough preferred time available for courseSessions with {} or more participants", number);
            }
        }
        return true;
    }

    /**
     * Checks if there is enough total available time to assign all courseSessions to the availabilityMatrices.
     *
     * @param courseSessions to be checked
     * @param availabilityMatrices to be checked
     * @return true if there is enough space to assign all courseSessions, false if not
     */
    protected boolean checkAvailableTime(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        int totalTimeNeeded = 0;
        int totalTimeAvailable = 0;
        int totalPreferredTimeAvailable = 0;

        for(CourseSession courseSession : courseSessions){
            totalTimeNeeded += courseSession.getDuration();
        }
        for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
            totalTimeAvailable += (int) availabilityMatrix.getTotalAvailableTime();
            totalPreferredTimeAvailable += (int) availabilityMatrix.getTotalAvailablePreferredTime();
        }

        if(totalTimeNeeded > totalTimeAvailable){
            return false;
        }

        if(totalTimeNeeded > totalPreferredTimeAvailable){
            log.warn("There is not enough space reserved for COMPUTER_SCIENCE. " +
                            "{} more minutes will be used from other free space",
                    totalTimeNeeded - totalPreferredTimeAvailable);
        }
        return true;
    }

    /**
     * Checks if all constraints are fulfilled to assign a courseSession to a certain candidate.
     * @param courseSession to be checked
     * @param candidate where the courseSession might be assigned
     * @return true if all checks are successful, false if at least one was not
     */
    protected boolean checkConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        if(!checkRoomCapacity(courseSession, candidate.getAvailabilityMatrix())){
            log.debug("room capacity of candidate is not fitting courseSession");
            return false;
        }
        if(!checkTimingConstraintsFulfilled(courseSession, candidate)){
            log.debug("timing constraints are intersecting with candidate");
            return false;
        }
        if(!checkCoursesOfSameSemester(courseSession, candidate)){
            log.debug("other course of same semester intersecting");
            return false;
        }
        return true;
    }

    /**
     * Checks if the room capacity is greater or equals the courseSession's number of participants. It also checks that
     * the room capacity is not too large, as we don't want to assign a courseSession with e.g. 25 participants to a
     * room with a capacity of 300 people.
     *
     * @param courseSession to be checked
     * @param availabilityMatrix of the room to be checked
     * @return true if the check was successful, false if not
     */
    protected boolean checkRoomCapacity(CourseSession courseSession, AvailabilityMatrix availabilityMatrix){
        return availabilityMatrix.getCapacity() >= courseSession.getNumberOfParticipants()
                && availabilityMatrix.getCapacity() / 2 <= courseSession.getNumberOfParticipants();
    }

    /**
     * Checks if the candidate's timing of assignment intersects with one of the courseSession's timing constraints.
     * @param courseSession to be checked
     * @param candidate to be checked
     * @return true if there is no intersection, false if there is.
     */
    protected boolean checkTimingConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        Timing timing = AvailabilityMatrix.toTiming(candidate);
        List<Timing> timingConstraints = courseSession.getTimingConstraints();
        if(timingConstraints != null){
            for(Timing timingConstraint : courseSession.getTimingConstraints()){
                if(timing.intersects(timingConstraint)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the candidate's timing intersects with the timing of any other courseSession already assigned that is
     * from the same semester.
     *
     * @param courseSession to be checked
     * @param candidate to be checked
     * @return true if there is no intersection with any other courseSession of the same semester, false if there is
     * at least one intersection
     */
    protected boolean checkCoursesOfSameSemester(CourseSession courseSession, Candidate candidate){
        for(AvailabilityMatrix availabilityMatrix : allAvailabilityMatrices){
            if(availabilityMatrix.semesterIntersects(candidate, courseSession)){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks already assigned courseSessions for collisions
     * @param timeTable to be checked
     * @return a list of courseSessions that are in collision
     */

    public List<CourseSession> collisionCheck(TimeTable timeTable){
        if(!this.timeTable.equals(timeTable)){
            setTimeTable(timeTable);
        }

        List<CourseSession> collisionCandidates = timeTable.getAssignedCourseSessions();
        List<CourseSession> collisions = new ArrayList<>();
        for(CourseSession courseSession : collisionCandidates){
            if(!checkCoursesOfSameSemester(courseSession, AvailabilityMatrix.toCandidate(courseSession))){
                collisions.add(courseSession);
            }
        }

        return collisions;
    }


}