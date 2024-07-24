package com.example.demo.scheduling;

import com.example.demo.exceptions.roomTable.NoEnoughSpaceAvailableException;
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
    private final int NUMBER_OF_TRIES = 10;
    private List<AvailabilityMatrix> availabilityMatricesOfRoomsWithComputers;
    private List<AvailabilityMatrix> availabilityMatricesOfRoomsWithoutComputers;
    private List<AvailabilityMatrix> allAvailabilityMatrices;
    private List<CourseSession> courseSessionsWithComputerNeeded;
    private List<CourseSession> courseSessionsWithoutComputerNeeded;
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
        courseSessionsWithoutComputerNeeded = new ArrayList<>();
        courseSessionsWithComputerNeeded = new ArrayList<>();
        for(RoomTable roomTable : timeTable.getRoomTablesWithComputersAvailable()){
            availabilityMatricesOfRoomsWithComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        for(RoomTable roomTable : timeTable.getRoomTablesWithoutComputersAvailable()){
            availabilityMatricesOfRoomsWithoutComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        this.courseSessionsWithComputerNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithComputersNeeded());
        this.courseSessionsWithoutComputerNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithoutComputersNeeded());
        this.candidateQueue = new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot));
    }

    public void assignUnassignedCourseSessions(){
        int totalTimeNeededNoComputers = 0;
        int totalTimeNeededComputers = 0;
        int totalTimeAvailableComputers = 0;
        int totalTimeAvailableNoComputers = 0;
        int totalTimePreferredAvailableComputers = 0;
        int totalTimePreferredAvailableNoComputers = 0;
        boolean usePreferredSlotsForComputers = true;
        boolean usePreferredSlotsForNoComputers = true;

        for(CourseSession courseSession : courseSessionsWithoutComputerNeeded){
            totalTimeNeededNoComputers += courseSession.getDuration();
        }
        for(CourseSession courseSession : courseSessionsWithComputerNeeded){
            totalTimeNeededComputers += courseSession.getDuration();
        }
        for(AvailabilityMatrix availabilityMatrix : availabilityMatricesOfRoomsWithComputers){
            totalTimeAvailableComputers += (int) availabilityMatrix.getTotalAvailableTime();
            totalTimePreferredAvailableComputers += (int) availabilityMatrix.getTotalAvailablePreferredTime();
        }
        for(AvailabilityMatrix availabilityMatrix : availabilityMatricesOfRoomsWithoutComputers){
            totalTimeAvailableNoComputers += (int) availabilityMatrix.getTotalAvailableTime();
            totalTimePreferredAvailableNoComputers += (int) availabilityMatrix.getTotalAvailablePreferredTime();
        }

        //check pre-constraints
        if(totalTimeNeededComputers > totalTimeAvailableComputers){
            throw new NoEnoughSpaceAvailableException(String.format("%d more minutes needed for courses with computers necessary",
                    totalTimeNeededComputers - totalTimeAvailableComputers));
        }
        if(totalTimeNeededNoComputers > totalTimeAvailableNoComputers){
            throw new NoEnoughSpaceAvailableException(String.format("%d more minutes needed for courses with computers necessary",
                    totalTimeNeededNoComputers - totalTimeAvailableNoComputers));
        }
        if(totalTimeNeededComputers > totalTimePreferredAvailableComputers){
            log.warn("There is not enough space reserved for COMPUTER_SCIENCE " +
                            "for courses with computers needed. {} more minutes will be used from other free space",
                    totalTimeNeededNoComputers - totalTimePreferredAvailableComputers);
            usePreferredSlotsForComputers = false;
        }
        if(totalTimeNeededNoComputers > totalTimePreferredAvailableNoComputers){
            log.warn("There is not enough space reserved for COMPUTER_SCIENCE " +
                            "for courses without computers needed. {} more minutes will be used from other free space",
                    totalTimeNeededNoComputers - totalTimePreferredAvailableNoComputers);
            usePreferredSlotsForNoComputers = false;
        }

        //Assign courseSessionsWithoutComputersNeeded
        assignCourseSessions(courseSessionsWithoutComputerNeeded, availabilityMatricesOfRoomsWithoutComputers,
                usePreferredSlotsForNoComputers);
        //Assign courseSessionsWithComputersNeeded
        assignCourseSessions(courseSessionsWithComputerNeeded, availabilityMatricesOfRoomsWithComputers,
                usePreferredSlotsForComputers);
    }

    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices, boolean preferredOnly){
        int numberOfTries = 0;
        numberOfCourseSessions = courseSessions.size();
        List<CourseSession> singleCourseSessions;
        List<CourseSession> groupCourseSessions;
        List<CourseSession> splitCourseSessions;

        do {
            singleCourseSessions = filterAndSortSingleCourseSessions(courseSessions);
            groupCourseSessions =filterAndSortGroupCourseSessions(courseSessions);
            splitCourseSessions = filterAndSortSplitCourseSessions(courseSessions);

            processSingleCourseSessions(singleCourseSessions, availabilityMatrices, preferredOnly);
            processGroupCourseSessions(groupCourseSessions, availabilityMatrices, preferredOnly);
            processSplitCourseSessions(splitCourseSessions, availabilityMatrices, preferredOnly);

            if(numberOfTries >= NUMBER_OF_TRIES){
                log.error("Assignment of courseSessions {} computers failed",
                        courseSessions.getFirst().isComputersNecessary() ? "with" : "without");
                break;
            }
            numberOfTries++;
        }
        while(!finalizeAssignment());
    }

    private List<CourseSession> filterAndSortSingleCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(c -> !c.isSplitCourse() && !c.isGroupCourse())
                .sorted(Comparator.comparingInt(CourseSession::getDuration)
                        .thenComparing(CourseSession::getNumberOfParticipants))
                .collect(Collectors.toList());
    }

    private List<CourseSession> filterAndSortGroupCourseSessions(List<CourseSession> courseSessions){
        return courseSessions.stream()
                .filter(CourseSession::isGroupCourse)
                .sorted(Comparator.comparingInt(CourseSession::getDuration)
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

    private void processGroupCourseSessions(List<CourseSession> groupCourseSessions, List<AvailabilityMatrix> availabilityMatrices, boolean preferredOnly){
        List<Candidate> currentCandidates = new ArrayList<>();
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
            numberOfGroups = currentCourseSessions.size();
            dayOfAssignment = random.nextInt(5);

            for(int i = 0; i < 5; i++){
                // find a list of possible candidates for a certain day
                for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                    currentCandidates.addAll(availabilityMatrix.getPossibleCandidatesOfDay(dayOfAssignment,
                            currentCourseSessions.getFirst().getDuration(), preferredOnly));
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
        }
    }

    private void processSplitCourseSessions(List<CourseSession> splitCourseSessions, List<AvailabilityMatrix> availabilityMatrices, boolean preferredOnly){
        List<CourseSession> currentCourseSessions = new ArrayList<>();
        String groupId;
        while (!splitCourseSessions.isEmpty()){
            groupId = splitCourseSessions.getFirst().getCourseId();
            while(!splitCourseSessions.isEmpty() && splitCourseSessions.getFirst().getCourseId().equals(groupId)){
                currentCourseSessions.add(splitCourseSessions.removeFirst());
            }
        }
    }

    private void processSingleCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices, boolean preferredOnly){
        Candidate currentCandidate;

        // For each courseSession
        for(CourseSession courseSession : courseSessions){
            log.debug("Choosing CourseSession {} for assignment", courseSession.getName());
            // If queue is empty or all the current courseSession needs candidates of different duration
            if(candidateQueue.isEmpty() || courseSession.getDuration() != candidateQueue.peek().getDuration()){
                // Fill the queue with candidates of appropriate duration
                fillQueue(availabilityMatrices, courseSession.getDuration(), preferredOnly);

            }

            // Find placement candidate for courseSession
            do{
                //refill the queue if no fitting candidate in queue
                if(candidateQueue.isEmpty()){
                    fillQueue(availabilityMatrices, courseSession.getDuration(), preferredOnly);
                }
                //select possible candidate for placement
                currentCandidate = candidateQueue.poll();
                log.debug("Selecting candidate {} for assignment", currentCandidate);
            }
            while(!checkConstraintsFulfilled(courseSession, currentCandidate));

            //assign courseSession
            log.debug("Successfully assigned CourseSession {} to {}", courseSession.getName(), currentCandidate);
            Timing finalTiming = currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, courseSession);
            readyForAssignmentSet.put(courseSession, finalTiming);
            //courseSession.setAssigned(true);
            //courseSession.setTiming(timingService.createTiming(finalTiming));
            courseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
        }
    }

    private boolean checkConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        if(!checkRoomCapacity(courseSession, candidate)){
            log.debug("room capacity exceeded");
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

    private boolean checkRoomCapacity(CourseSession courseSession, Candidate candidate){
        return candidate.getAvailabilityMatrix().getCapacity() >= courseSession.getNumberOfParticipants()
                && candidate.getAvailabilityMatrix().getCapacity() / 2 <= courseSession.getNumberOfParticipants();
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

    private void fillQueue(List<AvailabilityMatrix> availabilityMatrices, int duration, boolean preferredOnly){
        // remove all candidates with different durations when refilling queue
        candidateQueue.stream().filter(c -> c.getDuration() == duration).forEach(c -> {});
        // always fill the queue with the first available and one random candidate
        for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
            try{
                candidateQueue.add(availabilityMatrix.getEarliestAvailableSlotForDuration(duration, preferredOnly));
                candidateQueue.add(availabilityMatrix.getRandomAvailableSlot(duration, preferredOnly));
            }
            catch(NoEnoughSpaceAvailableException ignored){}
        }
    }
}
