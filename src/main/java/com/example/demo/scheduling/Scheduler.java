package com.example.demo.scheduling;

import com.example.demo.exceptions.roomTable.NoSpaceAvailableException;
import com.example.demo.models.*;

import java.util.*;
import java.util.stream.Collectors;

public class Scheduler {
    private final List<AvailabilityMatrix> availabilityMatricesOfRoomsWithComputers;
    private final List<AvailabilityMatrix> availabilityMatricesOfRoomsWithoutComputers;
    private final List<AvailabilityMatrix> allAvailabilityMatrices;
    private List<CourseSession> courseSessionsWithComputerNeeded;
    private List<CourseSession> courseSessionsWithoutComputerNeeded;

    private Queue<Candidate> candidateQueue;
    private CourseSession currentCourseSession;
    private int currentDuration = 0;
    private Candidate currentCandidate;

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

        System.out.println("available no comp: " + totalTimeAvailableNoComputers);
        System.out.println("available comp: " + totalTimeAvailableComputers);
        System.out.println("needed no comp: " + totalTimeNeededNoComputers);
        System.out.println("needed comp: " + totalTimeNeededComputers);

        //Assign courseSessionsWithoutComputersNeeded
        assignCourseSessions(courseSessionsWithoutComputerNeeded, availabilityMatricesOfRoomsWithoutComputers);
        //Assign courseSessionsWithComputersNeeded
        assignCourseSessions(courseSessionsWithComputerNeeded, availabilityMatricesOfRoomsWithComputers);
    }


    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        //Process courseSessions with computer necessary
        int newDuration;

        //Order them by semester and duration
        courseSessions = courseSessions.stream().sorted((o1, o2) -> {
            if(o1.getCourse().getDuration() != o2.getCourse().getDuration()){
                return o2.getCourse().getDuration() - o1.getCourse().getDuration();
            }
            return o2.getCourse().getNumberOfParticipants() - o1.getCourse().getNumberOfParticipants();
        }).collect(Collectors.toList());

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

            while(true){
                if(candidateQueue.isEmpty()){
                    fillQueue(availabilityMatrices, currentDuration);
                }
                //select possible candidate for placement
                currentCandidate = candidateQueue.poll();
                System.out.printf("selecting candidate %s for assignment\n", currentCandidate);

                if(!checkRoomCapacity()){
                    System.out.println("room capacity exceeded");
                    continue;
                }
                if(!checkRoomCapacity()){
                    System.out.println("room capacity exceeded");
                    continue;
                }
                if(!checkTimingConstraintsFulfilled()){
                    System.out.println("timing constraints are intersecting with candidate");
                    continue;
                }
                if(!checkCoursesOfSameSemester()){
                    System.out.println("other course of same semester intersecting");
                    continue;
                }
                if(!checkGroupCourse()){
                    System.out.println("group course");
                    continue;
                }
                if(!checkSplitCourse()){
                    System.out.println("split course");
                    continue;
                }
                break;
            }

            //assign courseSession
            System.out.printf("Successfully assigned CourseSession %s to %s\n", currentCourseSession.getName(), currentCandidate);
            Timing finalTiming = currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate.getPosition(), currentDuration, currentCourseSession);
            System.out.println("__________");
            currentCourseSession.setAssigned(true);
            currentCourseSession.setTiming(finalTiming);
        }
    }

    private boolean checkRoomCapacity(){
        return currentCandidate.getAvailabilityMatrix().getCapacity() >= currentCourseSession.getCourse().getNumberOfParticipants();
    }

    private boolean checkTimingConstraintsFulfilled(){
        Timing timing = AvailabilityMatrix.toTiming(currentCandidate.getPosition(), currentDuration);
        for(Timing timingConstraint : currentCourseSession.getTimingConstraints()){
            if(timing.intersects(timingConstraint)){
                return false;
            }
        }
        return true;
    }

    private boolean checkCoursesOfSameSemester(){
        for(AvailabilityMatrix availabilityMatrix : allAvailabilityMatrices){
            if(availabilityMatrix.semesterIntersects(currentCandidate.getPosition(), currentDuration, currentCourseSession.getCourse().getSemester())){
                return false;
            }
        }
        return true;
    }

    private boolean checkGroupCourse(){
        return true;
    }

    private boolean checkSplitCourse(){
        return true;
    }

    private void fillQueue(List<AvailabilityMatrix> availabilityMatrices, int duration){
        candidateQueue.stream().filter(c -> c.getDuration() == duration).forEach(c -> {});
        for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
            try{
                candidateQueue.add(new Candidate(availabilityMatrix, availabilityMatrix.getEarliestAvailableSlotForDuration(duration), duration));
                candidateQueue.add(new Candidate(availabilityMatrix, availabilityMatrix.getRandomAvailableSlot(duration), duration));
            }
            catch(NoSpaceAvailableException ignored){}
        }
    }
}
