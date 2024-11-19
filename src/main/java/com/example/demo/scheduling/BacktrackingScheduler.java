package com.example.demo.scheduling;

import com.example.demo.exceptions.scheduler.AssignmentFailedException;
import com.example.demo.exceptions.scheduler.NoCandidatesForCourseSessionException;
import com.example.demo.exceptions.scheduler.PreconditionFailedException;
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
    private final Logger log = LoggerFactory.getLogger(BacktrackingScheduler.class);
    private TimeTable timeTable;
    Stack<AssignmentStackEntry> assignmentStack;

    private final TimingService timingService;
    private final CourseSessionService courseSessionService;
    private final ConcurrentCourseLimiter<String> concurrentGroupCourseLimiter;
    private final ConcurrentCourseLimiter<Integer> concurrentElectiveCourseLimiter;

    public BacktrackingScheduler(TimingService timingService, CourseSessionService courseSessionService) {
        this.timingService = timingService;
        this.courseSessionService = courseSessionService;
        this.concurrentGroupCourseLimiter = new ConcurrentCourseLimiter<>(2);
        this.concurrentElectiveCourseLimiter = new ConcurrentCourseLimiter<>(7);
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
    public void assignUnassignedCourseSessions() {
        try {
            log.info("> Processing courseSessions that don't need computers ...");
            assignCourseSessions(courseSessionsWithoutComputersNeeded, availabilityMatricesOfRoomsWithoutComputers);
            log.info("Finished processing courseSessions that don't need computers");

            log.info("> Processing courseSessions that need computers ...");
            assignCourseSessions(courseSessionsWithComputersNeeded, availabilityMatricesOfRoomsWithComputers);
            log.info("Finished processing courseSessions that need computers");
        } catch (PreconditionFailedException exception) {
            log.error("Error while checking preconditions: {}", exception.getMessage());
            throw exception;
        } catch (NoCandidatesForCourseSessionException exception) {
            log.error("Error while searching for assignment candidates: {}", exception.getMessage());
            throw exception;
        } catch (AssignmentFailedException exception) {
            log.error("Error while processing assignment: {}", exception.getMessage());
            throw exception;
        }
    }

    /**
     * This method first checks the preconditions, then splits the courseSessions into single, group and split
     * courseSessions and processes them. At the end, it checks if there is an assignment candidate for all courseSessions.
     * If yes, the courseSessions are assigned, if not
     *
     * @param courseSessions to be processed
     * @param availabilityMatrices to be used for assigning the courseSessions
     */
    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        Map<CourseSession, List<Candidate>> possibleCandidatesForCourseSessions = new HashMap<>();
        List<CourseSession> sortedCourseSessions;

        log.info("Starting precondition checks ...");
        checkPreConditions(courseSessions, availabilityMatrices);
        log.info("Precondition checks successful");

        sortedCourseSessions = sortCourseSessions(courseSessions);

        prepareCandidatesForCourseSessions(possibleCandidatesForCourseSessions, sortedCourseSessions, availabilityMatrices);

        log.info("Starting assignment");
        processAssignment(possibleCandidatesForCourseSessions);
        log.info("Finished processing assignment.");
    }

    /**
     * Filters and sorts a list of courseSessions to obtain only group courseSessions sorted descending by duration and
     * ascending by groupID.
     * @param courseSessions to be filtered and sorted
     * @return sorted list of group courseSessions
     */
    private List<CourseSession> sortCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .sorted(Comparator.comparingInt(CourseSession::getDuration).reversed()
                        .thenComparing(CourseSession::getNumberOfParticipants).reversed()
                        .thenComparing(CourseSession::getCourseId))
                .collect(Collectors.toList());
    }

    private int createKey(CourseSession courseSession){
        return courseSession.getStudyType().ordinal() * 100 + courseSession.getSemester();
    }

    /**
     * This method finds all possible candidates for the group courseSessions and collects them in a map
     * @param map to collect courseSessions and their corresponding candidates
     * @param courseSessions to be prepared
     * @param availabilityMatrices to find possible candidates in
     */
    private void prepareCandidatesForCourseSessions(Map<CourseSession, List<Candidate>> map, List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        for(CourseSession courseSession: courseSessions){
            if(courseSession.isElective()){
                concurrentElectiveCourseLimiter.initGroup(createKey(courseSession));
            }
            if(courseSession.isGroupCourse()){
                concurrentGroupCourseLimiter.initGroup(courseSession.getCourseId());
            }
            List<Candidate> candidates = new ArrayList<>();
            for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                candidates.addAll(availabilityMatrix.getAllAvailableCandidates(courseSession));
            }
            List<Candidate> filteredCandidates = candidates.stream()
                    .filter(c -> checkConstraintsFulfilled(courseSession, c))
                    .sorted(Comparator.comparing(Candidate::isPreferredSlots).reversed()
                            .thenComparingInt(Candidate::getSlot))
                    .collect(Collectors.toList());
            if(filteredCandidates.isEmpty()){
                throw new NoCandidatesForCourseSessionException(String.format("No assignment candidates available for CourseSession %s", courseSession));
            }
            map.put(courseSession, filteredCandidates);
        }
    }

    /**
     * This method executes the backtracking algorithm to find an assignment for all courseSessions that fits all
     * constraints.
     * @param possibleCandidatesForCourseSessions map to start the backtracking algorithm from
     */
    private void processAssignment(Map<CourseSession, List<Candidate>> possibleCandidatesForCourseSessions) {
        final int initialCourseSessionSize = possibleCandidatesForCourseSessions.size();

        if (initialCourseSessionSize == 0) {
            return;
        }

        Stack<AssignmentState> stack = new Stack<>();
        AssignmentState currentState = new AssignmentState(possibleCandidatesForCourseSessions, 0);
        Map<CourseSession, List<Candidate>> currentCourseSessionMap;
        int currentIndex;
        int numberOfCandidates;
        Map<CourseSession, List<Candidate>> filteredCourseSessions;

        // Push first state
        stack.push(currentState);

        while (true) {
            currentCourseSessionMap = currentState.possibleCandidateMap;
            currentIndex = currentState.index;

            System.out.printf("State: %d, Index %d%n",
                    initialCourseSessionSize - currentCourseSessionMap.size(), currentIndex);

            if(initialCourseSessionSize - currentCourseSessionMap.size() == 20){
                int x = 0;
            }

            // Find CourseSession with the fewest candidates
            Map<CourseSession, List<Candidate>> finalCurrentCourseSessionMap = currentCourseSessionMap;
            CourseSession currentCourseSession = currentCourseSessionMap.keySet().stream()
                    .min(Comparator.comparingInt((CourseSession c) -> finalCurrentCourseSessionMap.get(c).size())
                            .thenComparingLong(CourseSession::getId))
                    .orElseThrow();
            numberOfCandidates = currentCourseSessionMap.get(currentCourseSession).size();

            // If all candidates for the current session have been tried, backtrack
            if (currentIndex >= numberOfCandidates) {
                if (stack.isEmpty()) {
                    throw new AssignmentFailedException("Assignment not possible after trying all combinations");
                }
                unassignLatestEntry(); // Revert the last assignment
                currentState = stack.pop(); // Go back to the previous state
                currentState.index++; // Try the next candidate in the previous state
                continue;
            }

            // Assign the current candidate
            Candidate currentCandidate = currentCourseSessionMap.get(currentCourseSession).get(currentIndex);
            assignEntry(currentCourseSession, currentCandidate);

            // Filter candidates for the new state
            filteredCourseSessions = filterCandidates(currentCourseSessionMap, currentCourseSession, currentCandidate);

            // If any list is empty after filtering, backtrack
            if (filteredCourseSessions == null) {
                unassignLatestEntry();
                currentState.index++; // Move to the next candidate in the current state
                continue;
            }

            // If no more sessions need to be assigned, finish the assignment
            if (filteredCourseSessions.isEmpty()) {
                break;
            }

            // Push the new state and move forward
            AssignmentState newState = new AssignmentState(filteredCourseSessions, 0);
            stack.push(currentState); // Save the current state
            currentState = newState; // Update to the new state
        }

        // Complete the assignment
        finishAssignment();
    }


    /**
     * This method removes the latest entry from the assignmentStack, the AvailabilityMatrix and the groupAssignmentMap
     */
    private void unassignLatestEntry(){
        AssignmentStackEntry entry = assignmentStack.pop();
        entry.candidate.clearInAvailabilityMatrix();
        if(entry.courseSession.isGroupCourse()){
            concurrentGroupCourseLimiter.removeEntry(entry.courseSession.getCourseId(), entry.candidate);
        }
        else if(entry.courseSession.isElective()){
            concurrentElectiveCourseLimiter.removeEntry(createKey(entry.courseSession), entry.candidate);
        }
    }

    private void assignEntry(CourseSession currentCourseSession, Candidate currentCandidate){
        currentCandidate.assignToCourseSession(currentCourseSession);
        assignmentStack.push(new AssignmentStackEntry(currentCourseSession, currentCandidate));
        if(currentCourseSession.isGroupCourse()){
            concurrentGroupCourseLimiter.addEntry(currentCourseSession.getCourseId(), currentCandidate);
        }
        else if(currentCourseSession.isElective()){
            concurrentElectiveCourseLimiter.addEntry(createKey(currentCourseSession), currentCandidate);
        }

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
        CourseSession cs;
        boolean groupCourseLimitExceeded;
        boolean electiveCourseLimitExceeded;
        List<Candidate> candidates;

        for (Map.Entry<CourseSession, List<Candidate>> entry : possibleCandidatesForCourseSessions.entrySet()) {
            cs = entry.getKey();
            groupCourseLimitExceeded = cs.isGroupCourse() && concurrentGroupCourseLimiter.isLimitExceeded(cs.getCourseId(), currentCandidate);
            electiveCourseLimitExceeded = cs.isElective() && concurrentElectiveCourseLimiter.isLimitExceeded(createKey(currentCourseSession), currentCandidate);
            candidates = entry.getValue();

            // if same cs, ignore
            if (cs.equals(currentCourseSession)) {
                continue;
            }

            //filter
            //remove all intersecting candidates
            if((cs.isFromSameDegreeAndSemester(currentCourseSession) && !cs.isFromSameCourse(currentCourseSession))
                    || groupCourseLimitExceeded
                    || electiveCourseLimitExceeded){
                candidates = candidates.stream()
                        .filter(c -> !c.intersects(currentCandidate))
                        .collect(Collectors.toList());
            }
            //remove all candidates at the same day
            else if(cs.isFromSameCourse(currentCourseSession) && cs.isSplitCourse() && currentCourseSession.isSplitCourse()){
                candidates = candidates.stream()
                        .filter((c) -> (!c.hasSameDay(currentCandidate)))
                        .collect(Collectors.toList());
            }
            //remove only intersecting cs in the same room
            else{
                candidates = candidates.stream()
                        .filter(c -> !c.intersects(currentCandidate) || !c.isInSameRoom(currentCandidate))
                        .collect(Collectors.toList());
            }

            // sort
            // make sure that cs a
            if(cs.isGroupCourse()){
                candidates = candidates.stream()
                        .sorted(Comparator.comparing(Candidate::isPreferredSlots).reversed().
                                thenComparingInt(c -> Math.abs(c.getDay() - currentCandidate.getDay())).
                                thenComparingInt(Candidate::getSlot))
                        .collect(Collectors.toList());
            }
            else if(cs.isSplitCourse()){
                candidates = candidates.stream()
                        .sorted(Comparator.comparing(Candidate::isPreferredSlots).reversed().
                                thenComparingInt(c -> Math.abs(c.getDay() - currentCandidate.getDay())).
                                thenComparingInt(Candidate::getSlot))
                        .collect(Collectors.toList());
            }
            else{
                candidates = candidates.stream()
                        .sorted(Comparator.comparing(Candidate::isPreferredSlots).reversed().
                                thenComparingInt((Candidate::getSlot)))
                        .collect(Collectors.toList());
            }
            if(candidates.isEmpty()){
                return null;
            }
            filteredCandidates.put(cs, candidates);
        }
        return filteredCandidates;
    }

    /**
     * Checks preconditions for a certain list of courseSessions and availabilityMatrices before starting assignment.
     * Precondition checks are used to determine in advance if the assignment is even possible considering time needed
     * and time available.
     *
     * @param courseSessions to be checked
     * @param availabilityMatrices to be checked
     */
    private void checkPreConditions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        if(!checkAvailableTime(courseSessions, availabilityMatrices)){
            throw new PreconditionFailedException("Not enough time available to assign all courseSessions");
        }
        else{
            log.info("+ Available time check successful");
        }
        if(!checkAvailableTimePerRoomCapacity(courseSessions, availabilityMatrices)){
            throw new PreconditionFailedException("Not enough time available to assign all courseSessions based on their numberOfParticipants");
        }
        else{
            log.info("+ Available time per capacity check successful");
        }
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
    private boolean checkAvailableTimePerRoomCapacity(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
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
    private boolean checkAvailableTime(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
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
    private boolean checkConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
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
    private boolean checkRoomCapacity(CourseSession courseSession, AvailabilityMatrix availabilityMatrix){
        return availabilityMatrix.getCapacity() >= courseSession.getNumberOfParticipants()
                && availabilityMatrix.getCapacity() / 2 <= courseSession.getNumberOfParticipants();
    }

    /**
     * Checks if the candidate's timing of assignment intersects with one of the courseSession's timing constraints.
     * @param courseSession to be checked
     * @param candidate to be checked
     * @return true if there is no intersection, false if there is.
     */
    private boolean checkTimingConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
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
    private boolean checkCoursesOfSameSemester(CourseSession courseSession, Candidate candidate){
        if(courseSession.isElective()){
            return true;
        }
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
     * @return a map of courseSessions that are in collision
     */
    public Map<CourseSession, List<CollisionType>> collisionCheck(TimeTable timeTable){
        if(!this.timeTable.equals(timeTable)){
            setTimeTable(timeTable);
        }

        List<CourseSession> collisionCandidates = timeTable.getAssignedCourseSessions();
        Map<CourseSession, List<CollisionType>> collisionMap = new HashMap<>();

        for(CourseSession courseSession : collisionCandidates){
            List<CollisionType> collisions = new ArrayList<>();
            // check room capacity collision
            if(courseSession.getRoomTable().getCapacity() < courseSession.getNumberOfParticipants()){
                collisions.add(CollisionType.ROOM_CAPACITY);
            }
            // check computer necessary/available collision
            if(courseSession.getRoomTable().isComputersAvailable() != courseSession.isComputersNecessary()){
                collisions.add(CollisionType.ROOM_COMPUTERS);
            }
            // check courseSession's timing constraints collision
            for(Timing timingConstraint : courseSession.getTimingConstraints()){
                if(timingConstraint.intersects(courseSession.getTiming())){
                    collisions.add(CollisionType.COURSE_TIMING_CONSTRAINTS);
                    break;
                }
            }
            // check intersecting courses of same semester collision
            if(!courseSession.isElective()){
                for(CourseSession cs : collisionCandidates){
                    if(cs.equals(courseSession)){
                        continue;
                    }
                    if(courseSession.getTiming().intersects(cs.getTiming()) &&
                        !cs.isElective() &&
                        !cs.isFromSameCourse(courseSession) &&
                        cs.isFromSameDegreeAndSemester(courseSession)){
                        collisions.add(CollisionType.SEMESTER_INTERSECTION);
                        break;
                    }
                }
            }

            if(!collisions.isEmpty()){
                collisionMap.put(courseSession, collisions);
            }
        }
        return collisionMap;
    }


}
