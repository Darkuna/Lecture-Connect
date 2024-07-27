package com.example.demo.scheduling;

import com.example.demo.exceptions.roomTable.NotEnoughSpaceAvailableException;
import com.example.demo.models.*;
import com.example.demo.services.TimingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class Scheduler {
    private final int MAX_NUMBER_OF_TRIES = 10;
    //TODO: fix usePreferredOnly
    private boolean usePreferredOnly = true;
    private List<AvailabilityMatrix> availabilityMatricesOfRoomsWithComputers;
    private List<AvailabilityMatrix> availabilityMatricesOfRoomsWithoutComputers;
    private List<AvailabilityMatrix> allAvailabilityMatrices;
    private List<CourseSession> courseSessionsWithComputersNeeded;
    private List<CourseSession> courseSessionsWithoutComputersNeeded;
    private final Random random = new Random(System.currentTimeMillis());
    private Queue<Candidate> candidateQueue;
    private final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final Map<CourseSession, Timing> readyForAssignmentSet = new HashMap<>();
    int numberOfCourseSessions;

    private final TimingService timingService;

    public Scheduler(TimingService timingService) {
        this.timingService = timingService;
    }

    public void setTimeTable(TimeTable timeTable){
        availabilityMatricesOfRoomsWithComputers = new ArrayList<>();
        availabilityMatricesOfRoomsWithoutComputers = new ArrayList<>();
        allAvailabilityMatrices = new ArrayList<>();
        courseSessionsWithoutComputersNeeded = new ArrayList<>();
        courseSessionsWithComputersNeeded = new ArrayList<>();
        for(RoomTable roomTable : timeTable.getRoomTablesWithComputersAvailable()){
            availabilityMatricesOfRoomsWithComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        for(RoomTable roomTable : timeTable.getRoomTablesWithoutComputersAvailable()){
            availabilityMatricesOfRoomsWithoutComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        this.courseSessionsWithComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithComputersNeeded());
        this.courseSessionsWithoutComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithoutComputersNeeded());
        this.candidateQueue = new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot));
        this.usePreferredOnly = true;
    }

    public void assignUnassignedCourseSessions(){
        //Assign courseSessionsWithComputersNeeded
        log.info("Processing courseSessions that don't need computers ...");
        assignCourseSessions(courseSessionsWithComputersNeeded, availabilityMatricesOfRoomsWithComputers);
        log.info("Finished processing courseSessions that don't need computers");

        //Assign courseSessionsWithoutComputersNeeded
        log.info("Processing courseSessions that need computers ...");
        assignCourseSessions(courseSessionsWithoutComputersNeeded, availabilityMatricesOfRoomsWithoutComputers);
        log.info("Finished processing courseSessions that need computers");
    }

    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        int numberOfTries = 0;
        usePreferredOnly = true;
        numberOfCourseSessions = courseSessions.size();
        List<CourseSession> singleCourseSessions;
        List<CourseSession> groupCourseSessions;
        List<CourseSession> splitCourseSessions;

        if(!checkPreConditions(courseSessions, availabilityMatrices)){
            log.error("preconditions failed");
            return;
        }

        do {
            if(numberOfTries != 0){
                availabilityMatrices.replaceAll(availabilityMatrix -> new AvailabilityMatrix(availabilityMatrix.getRoomTable()));
            }

            singleCourseSessions = filterAndSortSingleCourseSessions(courseSessions);
            groupCourseSessions =filterAndSortGroupCourseSessions(courseSessions);
            splitCourseSessions = filterAndSortSplitCourseSessions(courseSessions);

            processSingleCourseSessions(singleCourseSessions, availabilityMatrices);
            processGroupCourseSessions(groupCourseSessions, availabilityMatrices);
            processSplitCourseSessions(splitCourseSessions, availabilityMatrices);

            if(numberOfTries >= MAX_NUMBER_OF_TRIES){
                log.error("Assignment of courseSessions {} computers failed after {} tries",
                        courseSessions.getFirst().isComputersNecessary() ? "with" : "without", numberOfTries);
                break;
            }
            numberOfTries++;
        }
        while(!finalizeAssignment());
    }

    private boolean checkPreConditions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        log.info("Starting precondition checks ...");
        if(!checkAvailableTime(courseSessions, availabilityMatrices)){
            log.error("Not enough time available to assign all courseSessions");
            return false;
        }
        else{
            log.info("+ Available time check successful");
        }
        if(!checkAvailableTimePerSemester(courseSessions, availabilityMatrices)){
            log.error("Not enough time available to assign all courseSessions of each semester without intersecting courseSessions");
            return false;
        }
        else{
            log.info("+ Available time per semester check successful");
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
                usePreferredOnly = false;
            }
        }
        return true;
    }

    private boolean checkAvailableTimePerSemester(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        return true;
    }

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
            usePreferredOnly = false;
        }
        return true;
    }

    private List<CourseSession> filterAndSortSingleCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(c -> !c.isSplitCourse() && !c.isGroupCourse())
                .sorted(Comparator.comparingInt(CourseSession::getDuration).reversed()
                        .thenComparing(CourseSession::getNumberOfParticipants).reversed())
                .collect(Collectors.toList());
    }

    private List<CourseSession> filterAndSortGroupCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(CourseSession::isGroupCourse)
                .sorted(Comparator.comparingInt(CourseSession::getDuration).reversed()
                        .thenComparing(CourseSession::getCourseId))
                .collect(Collectors.toList());
    }

    private List<CourseSession> filterAndSortSplitCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(CourseSession::isSplitCourse)
                .sorted(Comparator.comparing(CourseSession::getCourseId)
                        .thenComparingInt(CourseSession::getDuration))
                .collect(Collectors.toList());
    }

    private boolean finalizeAssignment() {
        if(readyForAssignmentSet.size() == numberOfCourseSessions){
            for(CourseSession courseSession : readyForAssignmentSet.keySet()){
                Timing timing = readyForAssignmentSet.get(courseSession);
                timing = timingService.createTiming(timing);
                courseSession.setTiming(timing);
                courseSession.setAssigned(true);
            }
            readyForAssignmentSet.clear();
            return true;
        }
        else{
            for(CourseSession courseSession : readyForAssignmentSet.keySet()){
                courseSession.setRoomTable(null);
            }
            readyForAssignmentSet.clear();
            return false;
        }
    }

    private void processGroupCourseSessions(List<CourseSession> groupCourseSessions, List<AvailabilityMatrix> availabilityMatrices){
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

            // assign all courseSessions of the group to the list
            for(int i = 0; i < numberOfGroups; i++){
                candidateToAssign = currentCandidates.get(i);
                courseSessionToAssign = currentCourseSessions.get(i);
                Timing timing = candidateToAssign.getAvailabilityMatrix().assignCourseSession(candidateToAssign, courseSessionToAssign);
                courseSessionToAssign.setRoomTable(candidateToAssign.getAvailabilityMatrix().getRoomTable());
                readyForAssignmentSet.put(courseSessionToAssign, timing);
            }
            currentCourseSessions.clear();
            currentCandidates.clear();
            usePreferredOnly = true;
        }
    }

    private void processSplitCourseSessions(List<CourseSession> splitCourseSessions, List<AvailabilityMatrix> availabilityMatrices){
        List<CourseSession> currentCourseSessions = new ArrayList<>();
        String groupId;
        while (!splitCourseSessions.isEmpty()){
            groupId = splitCourseSessions.getFirst().getCourseId();
            while(!splitCourseSessions.isEmpty() && splitCourseSessions.getFirst().getCourseId().equals(groupId)){
                currentCourseSessions.add(splitCourseSessions.removeFirst());
            }
        }
    }

    private void processSingleCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        Candidate currentCandidate;
        int number_of_tries;

        // For each courseSession
        for(CourseSession courseSession : courseSessions){
            number_of_tries = 0;
            log.debug("Choosing CourseSession {} for assignment", courseSession.getName());
            // If queue is empty or all the current courseSession needs candidates of different duration
            if(candidateQueue.isEmpty() || courseSession.getDuration() != candidateQueue.peek().getDuration()){
                // Fill the queue with candidates of appropriate duration
                fillQueue(availabilityMatrices, courseSession, usePreferredOnly);

            }

            // Find placement candidate for courseSession
            do{
                if(number_of_tries >= 10000){
                    if(usePreferredOnly){
                        log.debug("Switching to other free time for assignment of courseSession {}", courseSession.getName());
                        usePreferredOnly = false;
                        number_of_tries = 0;
                    }
                    else{
                        throw new NotEnoughSpaceAvailableException("failed assignment");
                    }
                }
                //refill the queue if no fitting candidate in queue
                if(candidateQueue.isEmpty()){
                    fillQueue(availabilityMatrices, courseSession, usePreferredOnly);
                }
                //select possible candidate for placement
                currentCandidate = candidateQueue.poll();
                log.debug("Selecting candidate {} for assignment", currentCandidate);
                number_of_tries++;
            }
            while(!checkConstraintsFulfilled(courseSession, Objects.requireNonNull(currentCandidate)));

            //assign courseSession
            log.debug("Successfully assigned CourseSession {} to {}", courseSession.getName(), currentCandidate);
            Timing timing = currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, courseSession);
            courseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
            readyForAssignmentSet.put(courseSession, timing);
            usePreferredOnly = true;
        }

    }

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

    private boolean checkRoomCapacity(CourseSession courseSession, AvailabilityMatrix availabilityMatrix){
        return availabilityMatrix.getCapacity() >= courseSession.getNumberOfParticipants()
                    && availabilityMatrix.getCapacity() / 2 <= courseSession.getNumberOfParticipants();
    }

    private boolean checkTimingConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        Timing timing = AvailabilityMatrix.toTiming(candidate);
        for(Timing timingConstraint : courseSession.getTimingConstraints()){
            if(timing.intersects(timingConstraint)){
                return false;
            }
        }
        return true;
    }

    private boolean checkCoursesOfSameSemester(CourseSession courseSession, Candidate candidate){
        for(AvailabilityMatrix availabilityMatrix : allAvailabilityMatrices){
            if(availabilityMatrix.semesterIntersects(candidate, courseSession)){
                return false;
            }
        }
        return true;
    }

    private void fillQueue(List<AvailabilityMatrix> availabilityMatrices, CourseSession courseSession, boolean preferredOnly){
        Candidate firstCandidate = null;
        Candidate randomCandidate = null;
        // remove all candidates with different durations when refilling queue
        candidateQueue.stream().filter(c -> c.getDuration() == courseSession.getDuration()).forEach(c -> {});
        // always fill the queue with the first available and one random candidate
        for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
            if(checkRoomCapacity(courseSession, availabilityMatrix)){
                do {
                    try{
                        firstCandidate = availabilityMatrix.getEarliestAvailableSlotForDuration(courseSession.getDuration(), preferredOnly);
                        randomCandidate = availabilityMatrix.getRandomAvailableSlot(courseSession.getDuration(), preferredOnly);
                    }
                    catch(Exception e){
                        break;
                    }
                } while (AvailabilityMatrix.toTiming(randomCandidate).intersects(AvailabilityMatrix.toTiming(firstCandidate)));
                if(firstCandidate != null){
                    candidateQueue.add(firstCandidate);
                }
                if(randomCandidate != null){
                    candidateQueue.add(randomCandidate);
                }
            }
        }
        if(candidateQueue.isEmpty() && !preferredOnly){
            throw new NotEnoughSpaceAvailableException("No candidates found");
        }
        else if(candidateQueue.isEmpty()){
            fillQueue(availabilityMatrices, courseSession, false);
        }
    }
}
