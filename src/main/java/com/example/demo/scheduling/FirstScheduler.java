package com.example.demo.scheduling;

import com.example.demo.exceptions.roomTable.NotEnoughSpaceAvailableException;
import com.example.demo.models.*;
import com.example.demo.services.CourseSessionService;
import com.example.demo.services.TimingService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.example.demo.exceptions.scheduler.AssignmentFailedException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class FirstScheduler extends Scheduler {

    public FirstScheduler(TimingService timingService, CourseSessionService courseSessionService) {
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
        final int MAX_NUMBER_OF_TRIES = 100;
        int numberOfTries = 0;
        numberOfCourseSessions = courseSessions.size();
        List<CourseSession> singleCourseSessions;
        List<CourseSession> groupCourseSessions;
        List<CourseSession> splitCourseSessions;

        if(!checkPreConditions(courseSessions, availabilityMatrices)){
            log.error("preconditions failed");
            return;
        }

        do {
            singleCourseSessions = filterAndSortSingleCourseSessions(courseSessions);
            groupCourseSessions =filterAndSortGroupCourseSessions(courseSessions);
            splitCourseSessions = filterAndSortSplitCourseSessions(courseSessions);

            Collections.shuffle(availabilityMatrices);

            try{
                processSingleCourseSessions(singleCourseSessions, availabilityMatrices);
                processGroupCourseSessions(groupCourseSessions, availabilityMatrices);
                processSplitCourseSessions(splitCourseSessions, availabilityMatrices);
            }
            catch(Exception e){
                log.error(e.getMessage());
                resetReadyForAssignmentSet();
            }


            if(numberOfTries >= MAX_NUMBER_OF_TRIES){
                throw new AssignmentFailedException(String.format("Assignment of courseSessions %s computers failed after %d tries",
                        courseSessions.getFirst().isComputersNecessary() ? "with" : "without", numberOfTries));
            }
            numberOfTries++;

            if(readyToFinalize()){
                finalizeAssignment();
                log.info("Finished assignment after {} tries", numberOfTries);
                break;
            }
            else{
                resetReadyForAssignmentSet();
            }
        }
        while(true);
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

    /**
     * Finds a candidate for a certain courseSession from a list of availabilityMatrices
     * @param courseSession to find a candidate for
     * @param availabilityMatrices where the candidate is searched
     * @param dayFilter additional parameter for split courseSessions to exclude candidates of a specific day
     * @return candidate fulfilling all constraints for successful assignment
     */
    public Candidate findCandidateForCourseSession(CourseSession courseSession, List<AvailabilityMatrix> availabilityMatrices, int dayFilter){
        int numberOfTries = 0;
        Candidate currentCandidate;
        fillQueue(availabilityMatrices, courseSession, usePreferredOnly);
        if(dayFilter != -1){
            candidateQueue = candidateQueue.stream()
                    .filter(c -> c.getDay() != dayFilter)
                    .collect(Collectors.toCollection(() -> new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot))));
            }

        do{
            if(numberOfTries >= 10000){
                if(usePreferredOnly){
                    log.debug("Switching to other free time for assignment of courseSession {}", courseSession.getName());
                    usePreferredOnly = false;
                    numberOfTries = 0;
                }
                else{
                    throw new NotEnoughSpaceAvailableException("failed assignment");
                }
            }
            //refill the queue if no fitting candidate in queue
            if(candidateQueue.isEmpty()){
                fillQueue(availabilityMatrices, courseSession, usePreferredOnly);
                if(dayFilter != -1){
                    candidateQueue = candidateQueue.stream()
                            .filter(c -> c.getDay() != dayFilter)
                            .collect(Collectors.toCollection(() -> new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot))));
                }
            }
            //select possible candidate for placement
            currentCandidate = candidateQueue.poll();
            if(currentCandidate == null){
                continue;
            }
            log.debug("Selecting candidate {} for assignment", currentCandidate);
            numberOfTries++;
        }
        while(!checkConstraintsFulfilled(courseSession, Objects.requireNonNull(currentCandidate)));

        Timing timing = AvailabilityMatrix.toTiming(currentCandidate);

        candidateQueue = candidateQueue.stream()
                .filter(c -> !AvailabilityMatrix.toTiming(c).intersects(timing))
                .collect(Collectors.toCollection(() -> new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot))));

        return currentCandidate;
    }

    /**
     * This method processes all single courseSessions by trying to find an assignment candidate for all courseSessions
     * in the list. If a candidate for a certain courseSession is found, the courseSession is assigned in the candidate's
     * availabilityMatrix.
     *
     * @param singleCourseSessions to be processed
     * @param availabilityMatrices to be searched for candidates
     */
    private void processSingleCourseSessions(List<CourseSession> singleCourseSessions, List<AvailabilityMatrix> availabilityMatrices){
        if(singleCourseSessions.isEmpty()){
            log.info("> > There are no single courses to assign.");
            return;
        } else {
            log.info("> > Trying to assign {} single course sessions ...", singleCourseSessions.size());
        }
        Candidate currentCandidate;

        // For each courseSession
        for(CourseSession courseSession : singleCourseSessions){
            log.debug("Choosing CourseSession {} for assignment", courseSession.getName());
            currentCandidate = findCandidateForCourseSession(courseSession, availabilityMatrices, -1);

            //assign courseSession
            log.debug("Successfully assigned CourseSession {} to {}", courseSession.getName(), currentCandidate);
            currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, courseSession);
            courseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
            readyForAssignmentSet.put(courseSession, currentCandidate);
        }
        log.info("> > Finished assigning single course sessions.");
    }

    /**
     * This method processes all group courseSessions. For all courseSessions of the same group, it tries to find
     * enough assignment candidates to assign them on the same day. If this is not possible, it adds candidates of the
     * next day and so on, until enough candidates are found to assign of courseSessions of that group.
     *
     * @param groupCourseSessions to be processed
     * @param availabilityMatrices to be searched for candidates
     */
    private void processGroupCourseSessions(List<CourseSession> groupCourseSessions, List<AvailabilityMatrix> availabilityMatrices){
        if(groupCourseSessions.isEmpty()){
            log.info("> > There are no group courses to assign.");
            return;
        } else {
            log.info("> > Trying to assign {} group course sessions ...", groupCourseSessions.size());
        }

        List<Candidate> currentCandidates = new ArrayList<>();
        List<AvailabilityMatrix> availabilityMatricesToConsider;
        String groupId;
        int dayOfAssignment;
        List<CourseSession> currentCourseSessions = new ArrayList<>();
        int numberOfGroups;
        Candidate candidateToAssign;
        CourseSession courseSessionToAssign;

        // While there are still unassigned courseSessions
        while(!groupCourseSessions.isEmpty()){
            groupId = groupCourseSessions.getFirst().getCourseId();
            while(!groupCourseSessions.isEmpty() && groupCourseSessions.getFirst().getCourseId().equals(groupId)){
                currentCourseSessions.add(groupCourseSessions.removeFirst());
            }
            availabilityMatricesToConsider = availabilityMatrices.stream()
                    .filter(a -> checkRoomCapacity(currentCourseSessions.getFirst(),a))
                    .toList();
            numberOfGroups = currentCourseSessions.size();
            dayOfAssignment = random.nextInt(5);

            for(int i = 0; i < 10; i++){
                if(i == 5 && usePreferredOnly){
                    usePreferredOnly = false;
                }
                else if (i == 5){
                    throw new NotEnoughSpaceAvailableException("Not enough space available to assign all groups of course " + groupId);
                }
                // find a list of possible candidates for a certain day
                for(AvailabilityMatrix availabilityMatrix : availabilityMatricesToConsider){
                    currentCandidates.addAll(availabilityMatrix.getPossibleCandidatesOfDay(dayOfAssignment,
                            currentCourseSessions.getFirst().getDuration(), usePreferredOnly));
                }
                // filter the list with checkConstraintsFulfilled()
                currentCandidates = currentCandidates.stream()
                        .filter(c -> checkConstraintsFulfilled(currentCourseSessions.getFirst(), c))
                        .collect(Collectors.toList());
                // check if filtered list <= numberOfGroups
                if(currentCandidates.size() >= numberOfGroups){
                    // if yes, try another random day
                    break;
                }
                dayOfAssignment = dayOfAssignment == 4 ? 0 : dayOfAssignment + 1;
            }

            if(currentCandidates.size() < numberOfGroups){
                throw new NotEnoughSpaceAvailableException("Not enough space available to assign all groups of course " + groupId);
            }

            Collections.shuffle(currentCandidates);

            // assign all courseSessions of the group to the list
            for(int i = 0; i < numberOfGroups; i++){
                candidateToAssign = currentCandidates.get(i);
                courseSessionToAssign = currentCourseSessions.get(i);
                candidateToAssign.getAvailabilityMatrix().assignCourseSession(candidateToAssign, courseSessionToAssign);
                courseSessionToAssign.setRoomTable(candidateToAssign.getAvailabilityMatrix().getRoomTable());
                readyForAssignmentSet.put(courseSessionToAssign, candidateToAssign);
            }
            currentCourseSessions.clear();
            currentCandidates.clear();
            usePreferredOnly = true;
        }
        log.info("> > Finished assigning group course sessions.");
    }

    /**
     * This method processes all split courseSessions. All first splits are processed like single courseSessions, while
     * the second splits are processed with the additional variable dayFilter, that ensures that potential assignment
     * candidates for the second splits are not located on the same day as the candidate of the corresponding first split.
     *
     * @param splitCourseSessions to be processed
     * @param availabilityMatrices to be searched for candidates
     */
    private void processSplitCourseSessions(List<CourseSession> splitCourseSessions, List<AvailabilityMatrix> availabilityMatrices){
        Candidate currentCandidate;
        Map<String, Integer> courseIdToDayMap = new HashMap<>();

        if(splitCourseSessions.isEmpty()){
            log.info("> > There are no split courses to assign.");
            return;
        } else {
            log.info("> > Trying to assign {} split course sessions ...", splitCourseSessions.size());
        }
        List<CourseSession> firstSplits = splitCourseSessions.stream()
                        .filter(c -> c.getName().contains("Split 1"))
                        .toList();

        for(CourseSession courseSession : firstSplits){
            currentCandidate = findCandidateForCourseSession(courseSession, availabilityMatrices, -1);
            //assign courseSession
            log.debug("Successfully assigned Split 1 of CourseSession {} to {}", courseSession.getName(), currentCandidate);
            currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, courseSession);
            courseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
            readyForAssignmentSet.put(courseSession, currentCandidate);

            courseIdToDayMap.put(courseSession.getCourseId(), currentCandidate.getDay());

            usePreferredOnly = true;
        }

        splitCourseSessions.removeAll(firstSplits);

        for(CourseSession courseSession : splitCourseSessions){
            int dayFilter = courseIdToDayMap.getOrDefault(courseSession.getCourseId(), -1);
            currentCandidate = findCandidateForCourseSession(courseSession, availabilityMatrices, dayFilter);
            //assign courseSession
            log.debug("Successfully assigned Split 2 of CourseSession {} to {}", courseSession.getName(), currentCandidate);
            currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, courseSession);
            courseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
            readyForAssignmentSet.put(courseSession, currentCandidate);
            usePreferredOnly = true;
        }
        log.info("> > Finished assigning split course sessions.");
    }

    /**
     * Fills a queue with possible candidates for the assignment of single courseSessions. The queue always stores two
     * candidates of each availabilityMatrix, one earliest and one random candidate.
     *
     * @param availabilityMatrices to collect candidates from
     * @param courseSession for duration and numberOfParticipants
     * @param preferredOnly to determine if only preferred slots or also empty slots may be considered
     */
    private void fillQueue(List<AvailabilityMatrix> availabilityMatrices, CourseSession courseSession, boolean preferredOnly){
        // remove all candidates with different durations when refilling queue
        candidateQueue = candidateQueue.stream()
                .filter(c -> c.getDuration() == courseSession.getDuration())
                .collect(Collectors.toCollection(() -> new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot))));
        List<Candidate> candidates;
        // always fill the queue with the first available and one random candidate
        for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
            if(checkRoomCapacity(courseSession, availabilityMatrix)){
                try{
                    candidates = availabilityMatrix.getEarliestAvailableSlotsForDuration(courseSession.getDuration(), preferredOnly);
                    candidateQueue.addAll(candidates);
                }
                catch(Exception ignored){
                }
            }
        }

        candidateQueue = candidateQueue.stream()
                .filter(c -> checkConstraintsFulfilled(courseSession,c))
                .collect(Collectors.toCollection(() -> new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot))));

        if(candidateQueue.isEmpty() && !preferredOnly){
            throw new NotEnoughSpaceAvailableException("No candidates found for courseSession " + courseSession.getName());
        }
        else if(candidateQueue.isEmpty()){
            fillQueue(availabilityMatrices, courseSession, false);
        }
    }
}
