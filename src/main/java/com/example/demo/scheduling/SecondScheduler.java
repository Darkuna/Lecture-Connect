package com.example.demo.scheduling;

import com.example.demo.exceptions.scheduler.AssignmentFailedException;
import com.example.demo.models.*;
import com.example.demo.services.CourseSessionService;
import com.example.demo.services.TimingService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class SecondScheduler extends Scheduler {
    int numberOfRecursionSteps = 0;

    public SecondScheduler(TimingService timingService, CourseSessionService courseSessionService) {
        super(timingService, courseSessionService);
    }

    /**
     * Starts the assignment algorithm for all unassigned courseSessions of a timeTable. First, all courseSessions that
     * don't need rooms with computers are processed, then all courseSessions that need computer rooms.
     */
    @Transactional
    public void assignUnassignedCourseSessions(){
        numberOfRecursionSteps = 0;
        log.info("> Processing courseSessions that need computers ...");
        assignCourseSessions(courseSessionsWithComputersNeeded, availabilityMatricesOfRoomsWithComputers);
        log.info("Finished processing courseSessions that need computers");
        log.info("> Processing courseSessions that don't need computers ...");
        assignCourseSessions(courseSessionsWithoutComputersNeeded, availabilityMatricesOfRoomsWithoutComputers);
        log.info("Finished processing courseSessions that don't need computers");
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
        numberOfCourseSessions = courseSessions.size();
        List<CourseSession> singleCourseSessions;
        List<CourseSession> groupCourseSessions;
        List<CourseSession> splitCourseSessions;

        if(!checkPreConditions(courseSessions, availabilityMatrices)){
            log.error("preconditions failed");
            return;
        }

        singleCourseSessions = filterAndSortSingleCourseSessions(courseSessions);
        groupCourseSessions =filterAndSortGroupCourseSessions(courseSessions);
        splitCourseSessions = filterAndSortSplitCourseSessions(courseSessions);

        prepareSingleCourseSessions(possibleCandidatesForCourseSessions, singleCourseSessions, availabilityMatrices);
        prepareGroupCourseSessions(possibleCandidatesForCourseSessions, groupCourseSessions, availabilityMatrices);
        prepareSplitCourseSessions(possibleCandidatesForCourseSessions, splitCourseSessions, availabilityMatrices);

        log.info("Starting assignment");
        try {
            if (processAssignment(possibleCandidatesForCourseSessions)) {
                log.info("Finished processing assignment after {} recursion steps", numberOfRecursionSteps);
                numberOfRecursionSteps = 0;
            }
        } catch (AssignmentFailedException e) {
            log.error(e.getMessage());
        }

        if(readyToFinalize()){
            finalizeAssignment();
            log.info("Finished assignment.");
        }
        else{
            resetReadyForAssignmentSet();
            log.info("Reset for some reason");
        }
    }


    private boolean processAssignment(Map<CourseSession, List<Candidate>> possibleCandidatesForCourseSessions) {
        numberOfRecursionSteps++;
        if(numberOfRecursionSteps > 500){
            throw new AssignmentFailedException("Failed after 500 recursion steps");
        }
        log.info("map entries currently processed: {}", possibleCandidatesForCourseSessions.size());

        // If no more course sessions, assignment is complete
        if (possibleCandidatesForCourseSessions.isEmpty()) {
            return true;
        }

        // Sort CourseSessions based on the number of available candidates (smallest first)
        CourseSession currentCourseSession = possibleCandidatesForCourseSessions.keySet().stream()
                .min(Comparator.comparingInt(c -> possibleCandidatesForCourseSessions.get(c).size()))
                .orElseThrow();

        // Get the list of candidates for this course session
        List<Candidate> currentCandidates = new ArrayList<>(possibleCandidatesForCourseSessions.get(currentCourseSession));

        // Backup the current state of the map
        Map<CourseSession, List<Candidate>> backup = new HashMap<>(possibleCandidatesForCourseSessions);

        // Try each candidate for the current course session
        for (Candidate currentCandidate : currentCandidates) {
            // Assign the current candidate
            currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, currentCourseSession);
            currentCourseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
            readyForAssignmentSet.put(currentCourseSession, currentCandidate);

            // Filter all candidate lists for other course sessions
            Map<CourseSession, List<Candidate>> filteredCandidates = new HashMap<>();
            boolean isFeasible = true;

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
                        candidates = candidates.stream()
                                .filter(c -> !c.intersects(currentCandidate) || !c.isInSameRoom(currentCandidate))
                                .collect(Collectors.toList());
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
                        .filter(c -> !c.intersects(currentCandidate))
                        .collect(Collectors.toList());

                if (candidates.isEmpty()) {
                    isFeasible = false;
                    break;
                }
                filteredCandidates.put(cs, candidates);
            }

            if (isFeasible) {
                // Recursively attempt to assign remaining sessions
                if (processAssignment(filteredCandidates)) {
                    return true;
                }
            }

            // Undo the assignment
            currentCandidate.getAvailabilityMatrix().clearCandidate(currentCandidate);
            currentCourseSession.setRoomTable(null);
            readyForAssignmentSet.remove(currentCourseSession);
        }

        // If no valid assignment was found, reset the map and try again
        possibleCandidatesForCourseSessions.clear();
        possibleCandidatesForCourseSessions.putAll(backup);
        return false;
    }

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

    private void prepareGroupCourseSessions(Map<CourseSession, List<Candidate>> map, List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        String currentGroup;
        if(courseSessions == null || courseSessions.isEmpty()){
            return;
        }
        currentGroup = courseSessions.getFirst().getCourseId();
        int day = 0;
        for(CourseSession courseSession: courseSessions){
            List<Candidate> candidates = new ArrayList<>();
            if(!courseSession.getCourseId().equals(currentGroup)){
                currentGroup = courseSession.getCourseId();
                day = day == 4 ?  0 : day + 1;
            }
            for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                candidates.addAll(availabilityMatrix.getAllAvailableCandidates(courseSession));
            }
            List<Candidate> filteredCandidates = candidates.stream()
                    .filter(c -> checkConstraintsFulfilled(courseSession, c))
                    .collect(Collectors.toList());
            map.put(courseSession, filteredCandidates);
        }
    }


    private void prepareSplitCourseSessions(Map<CourseSession, List<Candidate>> map, List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
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
     * Filters and sorts a list of courseSessions to obtain only single courseSessions sorted descending by duration and
     * descending by numberOfParticipants.
     * @param courseSessions to be filtered and sorted
     * @return sorted list of single courseSessions
     */
    private List<CourseSession> filterAndSortSingleCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(c -> !c.isSplitCourse() && !c.isGroupCourse())
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
     * Filters and sorts a list of courseSessions to obtain only split courseSessions sorted ascending by courseID and
     * descending by duration.
     * @param courseSessions to be filtered and sorted
     * @return sorted list of split courseSessions
     */
    private List<CourseSession> filterAndSortSplitCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(CourseSession::isSplitCourse)
                .sorted(Comparator.comparing(CourseSession::getCourseId)
                        .thenComparingInt(CourseSession::getDuration).reversed())
                .collect(Collectors.toList());
    }
}
