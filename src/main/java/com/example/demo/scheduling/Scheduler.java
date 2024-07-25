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
            throw new NotEnoughSpaceAvailableException(String.format("%d more minutes needed for courses with computers necessary",
                    totalTimeNeededComputers - totalTimeAvailableComputers));
        }
        if(totalTimeNeededNoComputers > totalTimeAvailableNoComputers){
            throw new NotEnoughSpaceAvailableException(String.format("%d more minutes needed for courses with computers necessary",
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

        log.info("TotalTimeNeededNoComputers = {}", totalTimeNeededNoComputers);
        log.info("TotalTimeNeededComputers = {}", totalTimeNeededComputers);
        log.info("TotalTimeAvailableNoComputers = {}", totalTimeAvailableNoComputers);
        log.info("TotalTimePreferredAvailableComputers = {}", totalTimePreferredAvailableComputers);
        log.info("TotalTimePreferredAvailableNoComputers = {}", totalTimePreferredAvailableNoComputers);
        log.info("TotalTimeAvailableComputers = {}", totalTimeAvailableComputers);

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
            if(numberOfTries != 0){
                availabilityMatrices.replaceAll(availabilityMatrix -> new AvailabilityMatrix(availabilityMatrix.getRoomTable()));
            }

            singleCourseSessions = filterAndSortSingleCourseSessions(courseSessions);
            for(CourseSession courseSession : singleCourseSessions){
                System.out.println(courseSession.getNumberOfParticipants());
            }
            groupCourseSessions =filterAndSortGroupCourseSessions(courseSessions);
            splitCourseSessions = filterAndSortSplitCourseSessions(courseSessions);

            processSingleCourseSessions(singleCourseSessions, availabilityMatrices, preferredOnly);
            processGroupCourseSessions(groupCourseSessions, availabilityMatrices, preferredOnly);
            processSplitCourseSessions(splitCourseSessions, availabilityMatrices, preferredOnly);

            if(numberOfTries >= MAX_NUMBER_OF_TRIES){
                log.error("Assignment of courseSessions {} computers failed after {} tries",
                        courseSessions.getFirst().isComputersNecessary() ? "with" : "without", numberOfTries);
                break;
            }
            numberOfTries++;
        }
        while(!finalizeAssignment());
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
            log.info("Choosing CourseSession {} for assignment", courseSession.getName());
            // If queue is empty or all the current courseSession needs candidates of different duration
            if(candidateQueue.isEmpty() || courseSession.getDuration() != candidateQueue.peek().getDuration()){
                // Fill the queue with candidates of appropriate duration
                fillQueue(availabilityMatrices, courseSession, preferredOnly);

            }

            // Find placement candidate for courseSession
            do{
                //refill the queue if no fitting candidate in queue
                if(candidateQueue.isEmpty()){
                    fillQueue(availabilityMatrices, courseSession, preferredOnly);
                }
                //select possible candidate for placement
                currentCandidate = candidateQueue.poll();
                log.info("Selecting candidate {} for assignment", currentCandidate);
            }
            while(!checkConstraintsFulfilled(courseSession, currentCandidate));

            //assign courseSession
            log.info("Successfully assigned CourseSession {} to {}", courseSession.getName(), currentCandidate);
            Timing timing = currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate, courseSession);
            courseSession.setRoomTable(currentCandidate.getAvailabilityMatrix().getRoomTable());
            readyForAssignmentSet.put(courseSession, timing);
        }
    }

    private boolean checkConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        if(!checkRoomCapacity(courseSession, candidate.getAvailabilityMatrix())){
            log.info("room capacity exceeded cs: {}, cand: {}", courseSession.getNumberOfParticipants(), candidate.getAvailabilityMatrix().getCapacity());
            return false;
        }
        if(!checkTimingConstraintsFulfilled(courseSession, candidate)){
            log.info("timing constraints are intersecting with candidate");
            return false;
        }
        if(!checkCoursesOfSameSemester(courseSession, candidate)){
            log.info("other course of same semester intersecting");
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
        if(candidateQueue.isEmpty()){
            throw new NotEnoughSpaceAvailableException("No candidates found");
        }
    }
}
