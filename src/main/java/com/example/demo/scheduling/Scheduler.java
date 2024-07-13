package com.example.demo.scheduling;

import com.example.demo.exceptions.roomTable.NoEnoughSpaceAvailableException;
import com.example.demo.models.*;

import java.util.*;
import java.util.stream.Collectors;

public class Scheduler {
    private final List<AvailabilityMatrix> availabilityMatricesOfRoomsWithComputers;
    private final List<AvailabilityMatrix> availabilityMatricesOfRoomsWithoutComputers;
    private final List<AvailabilityMatrix> allAvailabilityMatrices;
    private List<CourseSession> courseSessionsWithComputerNeeded;
    private List<CourseSession> courseSessionsWithoutComputerNeeded;
    private final Random random = new Random(System.currentTimeMillis());

    private final Queue<Candidate> candidateQueue;

    public Scheduler(TimeTable timeTable){
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
        this.candidateQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.getPosition().getSlot()));
    }

    public void assignUnassignedCourseSessions(){
        int totalTimeNeededNoComputers = 0;
        int totalTimeNeededComputers = 0;
        int totalTimeAvailableComputers = 0;
        int totalTimeAvailableNoComputers = 0;
        for(CourseSession courseSession : courseSessionsWithoutComputerNeeded){
            totalTimeNeededNoComputers += courseSession.getDuration();
        }
        for(CourseSession courseSession : courseSessionsWithComputerNeeded){
            totalTimeNeededComputers += courseSession.getDuration();
        }
        for(AvailabilityMatrix availabilityMatrix : availabilityMatricesOfRoomsWithComputers){
            totalTimeAvailableComputers += (int) availabilityMatrix.getTotal_available_time();
        }
        for(AvailabilityMatrix availabilityMatrix : availabilityMatricesOfRoomsWithoutComputers){
            totalTimeAvailableNoComputers += (int) availabilityMatrix.getTotal_available_time();
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

        //Assign courseSessionsWithoutComputersNeeded
        assignCourseSessions(courseSessionsWithoutComputerNeeded, availabilityMatricesOfRoomsWithoutComputers);
        //Assign courseSessionsWithComputersNeeded
        assignCourseSessions(courseSessionsWithComputerNeeded, availabilityMatricesOfRoomsWithComputers);
    }

    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        //Process courseSessions with computer necessary

        List<CourseSession> groupCourseSessions;
        List<CourseSession> splitCourseSessions;

        //Create own list of all group courseSessions and order by duration and courseId
        groupCourseSessions = courseSessions.stream()
                .filter(CourseSession::isGroupCourse)
                .collect(Collectors.toList());

        groupCourseSessions.sort(Comparator.comparingInt(CourseSession::getDuration)
                .thenComparing(CourseSession::getCourseId));
        // remove them from list of all courseSessions
        courseSessions.removeAll(groupCourseSessions);

        //Create own list of all split courseSessions and order by duration and courseId
        splitCourseSessions = courseSessions.stream()
                .filter(CourseSession::isSplitCourse)
                .sorted(Comparator.comparingInt(CourseSession::getDuration)
                        .thenComparing(CourseSession::getCourseId))
                .collect(Collectors.toList());

        // remove them from list of all courseSessions
        courseSessions.removeAll(splitCourseSessions);

        //Order other courseSessions duration and numberOfParticipants
        courseSessions = courseSessions.stream().sorted((o1, o2) -> {
            if(o1.getDuration() != o2.getDuration()){
                return o2.getDuration() - o1.getDuration();
            }
            return o2.getNumberOfParticipants() - o1.getNumberOfParticipants();
        }).collect(Collectors.toList());

        // start the assignment
        processSingleCourseSessions(courseSessions, availabilityMatrices);
        processGroupCourseSessions(groupCourseSessions, availabilityMatrices);
        //processSplitCourseSessions(splitCourseSessions, availabilityMatrices);
    }

    private boolean checkConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        if(!checkRoomCapacity(courseSession, candidate)){
            System.out.println("room capacity exceeded");
            return false;
        }
        if(!checkTimingConstraintsFulfilled(courseSession, candidate)){
            System.out.println("timing constraints are intersecting with candidate");
            return false;
        }
        if(!checkCoursesOfSameSemester(courseSession, candidate)){
            System.out.println("other course of same semester intersecting");
            return false;
        }
        return true;
    }

    private boolean checkRoomCapacity(CourseSession courseSession, Candidate candidate){
        return candidate.getAvailabilityMatrix().getCapacity() >= courseSession.getNumberOfParticipants()
                && candidate.getAvailabilityMatrix().getCapacity() / 2 <= courseSession.getNumberOfParticipants();
    }

    private boolean checkTimingConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        Timing timing = AvailabilityMatrix.toTiming(candidate.getPosition(), candidate.getDuration());
        for(Timing timingConstraint : courseSession.getTimingConstraints()){
            if(timing.intersects(timingConstraint)){
                return false;
            }
        }
        return true;
    }

    private boolean checkCoursesOfSameSemester(CourseSession courseSession, Candidate candidate){
        for(AvailabilityMatrix availabilityMatrix : allAvailabilityMatrices){
            if(availabilityMatrix.semesterIntersects(candidate.getPosition(), candidate.getDuration(), courseSession)){
                return false;
            }
        }
        return true;
    }

    private void processGroupCourseSessions(List<CourseSession> groupCourseSessions, List<AvailabilityMatrix> availabilityMatrices){
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
                    currentCandidates.addAll(availabilityMatrix.getPossibleCandidatesOfDay(dayOfAssignment, currentCourseSessions.getFirst().getDuration()));
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
                Timing timing = candidateToAssign.getAvailabilityMatrix().assignCourseSession(candidateToAssign.getPosition(),
                        candidateToAssign.getDuration(), courseSessionToAssign);
                courseSessionToAssign.setAssigned(true);
                courseSessionToAssign.setTiming(timing);
            }
            currentCourseSessions.clear();
        }
    }

    private void processSplitCourseSessions(List<CourseSession> splitCourseSessions, List<AvailabilityMatrix> availabilityMatrices){

    }

    private void processSingleCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        Candidate currentCandidate;
        int currentDuration = 0;
        int newDuration;
        CourseSession currentCourseSession;

        // While there are still unassigned courseSessions
        while(!courseSessions.isEmpty()){
            newDuration = courseSessions.getFirst().getDuration();
            // If all unassigned courseSessions of the same durations are processed
            if(newDuration != currentDuration){
                // Get the first available slots for the new duration
                fillQueue(availabilityMatrices, newDuration);
                currentDuration = newDuration;
            }

            //Select courseSession
            currentCourseSession = courseSessions.removeFirst();
            System.out.printf("Choosing CourseSession %s for assignment\n", currentCourseSession.getName());

            // find placement for courseSession
            do{
                if(candidateQueue.isEmpty()){
                    fillQueue(availabilityMatrices, currentDuration);
                }
                //select possible candidate for placement
                currentCandidate = candidateQueue.poll();
                System.out.printf("selecting candidate %s for assignment\n", currentCandidate);
            }
            while(!checkConstraintsFulfilled(currentCourseSession, currentCandidate));

            //assign courseSession
            System.out.printf("Successfully assigned CourseSession %s to %s\n", currentCourseSession.getName(), currentCandidate);
            Timing finalTiming = currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate.getPosition(), currentCandidate.getDuration(), currentCourseSession);
            System.out.println("__________");
            currentCourseSession.setAssigned(true);
            currentCourseSession.setTiming(finalTiming);
        }
    }

    private void fillQueue(List<AvailabilityMatrix> availabilityMatrices, int duration){
        // remove all candidates with different durations when refilling queue
        candidateQueue.stream().filter(c -> c.getDuration() == duration).forEach(c -> {});
        // always fill the queue with the first available and one random candidate
        for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
            try{
                candidateQueue.add(new Candidate(availabilityMatrix, availabilityMatrix.getEarliestAvailableSlotForDuration(duration), duration));
                candidateQueue.add(new Candidate(availabilityMatrix, availabilityMatrix.getRandomAvailableSlot(duration), duration));
            }
            catch(NoEnoughSpaceAvailableException ignored){}
        }
    }
}
