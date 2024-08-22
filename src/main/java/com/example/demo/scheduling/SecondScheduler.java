package com.example.demo.scheduling;

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

    public SecondScheduler(TimingService timingService, CourseSessionService courseSessionService) {
        super(timingService, courseSessionService);
    }

    /**
     * Starts the assignment algorithm for all unassigned courseSessions of a timeTable. First, all courseSessions that
     * don't need rooms with computers are processed, then all courseSessions that need computer rooms.
     */
    @Transactional
    public void assignUnassignedCourseSessions(){
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

        processAssignment(possibleCandidatesForCourseSessions);

        if(readyToFinalize()){
            finalizeAssignment();
            log.info("Finished assignment.");
        }
        else{
            resetReadyForAssignmentSet();
        }
    }

    private void processAssignment(Map<CourseSession, List<Candidate>> possibleCandidatesForCourseSessions) {
        //backup of original
        Map<CourseSession, List<Candidate>> backup = new HashMap<>(possibleCandidatesForCourseSessions);
        Candidate currentCandidate;
        CourseSession currentCourseSession;
        List<CourseSession> courseSessions;
        List<Candidate> currentCandidates;

        do{
            //sort
            Map<CourseSession, List<Candidate>> finalPossibleCandidatesForCourseSessions = possibleCandidatesForCourseSessions;
            courseSessions = possibleCandidatesForCourseSessions.keySet().stream()
                    .sorted(Comparator.comparingInt(c -> finalPossibleCandidatesForCourseSessions.get(c).size()).reversed())
                    .collect(Collectors.toList());
            //choose first
            currentCourseSession = courseSessions.removeFirst();
            currentCandidates = possibleCandidatesForCourseSessions.get(currentCourseSession)
                    .stream().sorted(Comparator.comparingInt(Candidate::getSlot))
                    .collect(Collectors.toList());
            //get random index
            int x = possibleCandidatesForCourseSessions.get(currentCourseSession).size();
            if(x == 0){
                possibleCandidatesForCourseSessions = backup;
                backup = new HashMap<>(backup);
                resetReadyForAssignmentSet();
            }
            //choose random
            currentCandidate = currentCandidates.getFirst();
            //assign random
            currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, currentCourseSession);
            currentCourseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
            readyForAssignmentSet.put(currentCourseSession, currentCandidate);

            for(CourseSession courseSession : courseSessions){
                currentCandidates = possibleCandidatesForCourseSessions.get(courseSession);
                Candidate finalCurrentCandidate = currentCandidate;
                currentCandidates = currentCandidates.stream()
                        .filter(c -> !c.intersects(finalCurrentCandidate))
                        .sorted(Comparator.comparingInt(Candidate::getSlot))
                        .collect(Collectors.toList());
                possibleCandidatesForCourseSessions.put(courseSession, currentCandidates);
                if(currentCandidates.isEmpty()){
                    possibleCandidatesForCourseSessions = backup;
                    backup = new HashMap<>(backup);
                    resetReadyForAssignmentSet();
                    log.info("another round");
                }
            }
            possibleCandidatesForCourseSessions.remove(currentCourseSession);
        }
        while(!courseSessions.isEmpty());
    }

    private void prepareSingleCourseSessions(Map<CourseSession, List<Candidate>> map, List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        for(CourseSession courseSession : courseSessions){
            List<Candidate> candidates = new ArrayList<>();
            for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                candidates.addAll(availabilityMatrix.getAllAvailableCandidates(courseSession));
            }
            candidates.stream().filter(c -> checkConstraintsFulfilled(courseSession, c))
                    .collect(Collectors.toList());
            map.put(courseSession, candidates);
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
