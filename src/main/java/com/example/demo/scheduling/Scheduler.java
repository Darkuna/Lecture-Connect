package com.example.demo.scheduling;

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
    AvailabilityMatrix currentAvailabilityMatrix;
    Pair currentPosition;
    int currentDuration = 0;

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

        Map<Pair, AvailabilityMatrix> firstAvailableSlots = new HashMap<>();
        List<Pair> keys = new ArrayList<>();
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
                    firstAvailableSlots.put(availabilityMatrix.getEarliestAvailableSlotForDuration(newDuration), availabilityMatrix);
                }
                currentDuration = newDuration;
                // Sort keys
                keys = firstAvailableSlots.keySet().stream().sorted((o1, o2) -> {
                    if(o1.getSlot() != o2.getSlot()){
                        return o1.getSlot() - o2.getSlot();
                    }
                    return o1.getDay() - o2.getDay();
                }).collect(Collectors.toList());
            }

            //Select courseSession
            currentCourseSession = courseSessions.removeFirst();

            do {
                //select possible availabilityMatrix and position for placement
                currentPosition = keys.removeFirst();
                currentAvailabilityMatrix = firstAvailableSlots.get(currentPosition);


                //check constraints
                // ist es ein gruppenkurs?
                // ist es ein gesplitteter Kurs?
            } while (!checkRoomCapacity() || !checkTimingConstraintsFulfilled() || !checkCoursesOfSameSemester());

            //assign courseSession
            Timing finalTiming = currentAvailabilityMatrix.assignCourseSession(currentPosition, currentDuration, currentCourseSession);
            currentCourseSession.setAssigned(true);
            currentCourseSession.setTiming(finalTiming);
            firstAvailableSlots.remove(currentPosition);
            firstAvailableSlots.put(currentAvailabilityMatrix.getEarliestAvailableSlotForDuration(currentDuration), currentAvailabilityMatrix);

            //Sort the keys again after new put operation
            keys = firstAvailableSlots.keySet().stream().sorted((o1, o2) -> {
                if(o1.getSlot() != o2.getSlot()){
                    return o1.getSlot() - o2.getSlot();
                }
                return o1.getDay() - o2.getDay();
            }).collect(Collectors.toList());
        }
    }



    private boolean checkRoomCapacity(){
        return currentAvailabilityMatrix.getCapacity() >= currentCourseSession.getCourse().getNumberOfParticipants();
    }

    private boolean checkTimingConstraintsFulfilled(){
        Timing timing = AvailabilityMatrix.toTiming(currentPosition, currentDuration);
        for(Timing timingConstraint : currentCourseSession.getTimingConstraints()){
            if(timing.intersects(timingConstraint)){
                return false;
            }
        }
        return true;
    }

    private boolean checkCoursesOfSameSemester(){
        for(AvailabilityMatrix availabilityMatrix : allAvailabilityMatrices){
            if(availabilityMatrix.semesterIntersects(currentPosition, currentDuration, currentCourseSession.getCourse().getSemester())){
                return false;
            }
        }
        return true;
    }

    private boolean checkGroupCourse(){
        return false;
    }

    private boolean checkSplitCourse(){
        return false;
    }
}
