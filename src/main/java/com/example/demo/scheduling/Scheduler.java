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



    CourseSession currentCourseSession;
    int currentDuration = 0;
    Candidate currentCandidate;

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

        //Assign courseSessionsWithComputersNeeded
        assignCourseSessions(courseSessionsWithComputerNeeded, availabilityMatricesOfRoomsWithComputers);
        //Assign courseSessionsWithoutComputersNeeded
        assignCourseSessions(courseSessionsWithoutComputerNeeded, availabilityMatricesOfRoomsWithoutComputers);
    }


    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        //Process courseSessions with computer necessary
        Random rand = new Random();
        Queue<Candidate> candidateQueue = new PriorityQueue<>(new Comparator<Candidate>() {
            @Override
            public int compare(Candidate o1, Candidate o2) {
                return o1.getPosition().getSlot() - o2.getPosition().getSlot();
            }
        });
        int newDuration;

        //Order them by semester and duration
        courseSessions = courseSessions.stream().sorted((o1, o2) -> {
            if(o1.getCourse().getDuration() != o2.getCourse().getDuration()){
                return o1.getCourse().getDuration() - o2.getCourse().getDuration();
            }
            return o1.getCourse().getSemester() - o2.getCourse().getSemester();
        }).collect(Collectors.toList());

        // While there are still unassigned courseSessions
        while(!courseSessions.isEmpty()){
            newDuration = courseSessions.getFirst().getDuration();
            // If all unassigned courseSessions of the same durations are processed
            if(newDuration != currentDuration){
                // Get the first available slots for the new duration
                for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                    try{
                        candidateQueue.add(new Candidate(availabilityMatrix, availabilityMatrix.getEarliestAvailableSlotForDuration(newDuration)));
                    }
                    catch(NoSpaceAvailableException e){

                    }

                }
                System.out.println(candidateQueue);
                currentDuration = newDuration;
            }

            //Select courseSession
            currentCourseSession = courseSessions.removeFirst();

            do {
                //select possible candidate for placement
                currentCandidate = candidateQueue.poll();
                System.out.println(currentCandidate);
                try{
                    candidateQueue.add(new Candidate(currentCandidate.getAvailabilityMatrix(),
                            currentCandidate.getAvailabilityMatrix().getNextAvailableSlotForDurationAfterSlot(currentDuration, currentCandidate.getPosition())));
                }
                catch(Exception e){}

            } while (!checkRoomCapacity() || !checkTimingConstraintsFulfilled() || !checkCoursesOfSameSemester()
                        || checkGroupCourse() || checkSplitCourse());

            //assign courseSession
            Timing finalTiming = currentCandidate.getAvailabilityMatrix().assignCourseSession(currentCandidate.getPosition(), currentDuration, currentCourseSession);
            currentCourseSession.setAssigned(true);
            currentCourseSession.setTiming(finalTiming);
            try{
                candidateQueue.add(new Candidate(currentCandidate.getAvailabilityMatrix(),
                        currentCandidate.getAvailabilityMatrix().getEarliestAvailableSlotForDuration(currentDuration)));
            }
            catch(NoSpaceAvailableException e){

            }
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
}
