package com.example.demo.scheduling;

import com.example.demo.models.*;

import java.util.*;

public class Scheduler {
    private final List<AvailabilityMatrix> availabilityMatricesOfRoomsWithComputers;
    private final List<AvailabilityMatrix> availabilityMatricesOfRoomsWithoutComputers;
    private final List<AvailabilityMatrix> allAvailabilityMatrices;
    private final List<CourseSession> courseSessionsWithComputerNeeded;
    private final List<CourseSession> courseSessionsWithoutComputerNeeded;

    CourseSession currentCourseSession;
    AvailabilityMatrix currentAvailabilityMatrix;
    Pair currentPosition;
    int currentDuration = 0;

    public Scheduler(TimeTable timeTable){
        availabilityMatricesOfRoomsWithComputers = new ArrayList<>();
        availabilityMatricesOfRoomsWithoutComputers = new ArrayList<>();
        allAvailabilityMatrices = new ArrayList<>();
        for(RoomTable roomTable : timeTable.getRoomTablesWithComputersAvailable()){
            availabilityMatricesOfRoomsWithComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        for(RoomTable roomTable : timeTable.getRoomTablesWithoutComputersAvailable()){
            availabilityMatricesOfRoomsWithoutComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        this.courseSessionsWithComputerNeeded = timeTable.getUnassignedCourseSessionsWithComputersNeeded();
        this.courseSessionsWithoutComputerNeeded = timeTable.getUnassignedCourseSessionsWithComputersNeeded();
    }

    public void assignUnassignedCourseSessions(){
        //Assign courseSessionsWithComputersNeeded
        assignCourseSessions(courseSessionsWithComputerNeeded, availabilityMatricesOfRoomsWithComputers);
        //Assign courseSessionsWithoutComputersNeeded
        assignCourseSessions(courseSessionsWithoutComputerNeeded, availabilityMatricesOfRoomsWithoutComputers);
    }


    private void assignCourseSessions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices){
        //Process courseSessions with computer necessary

        Map<Pair, AvailabilityMatrix> firstAvailables = new HashMap<>();
        List<Pair> keys = List.of();

        int newDuration;

        //Order them by semester and duration
        courseSessions = courseSessions.stream().sorted((o1, o2) -> {
            if(o1.getCourse().getDuration() != o2.getCourse().getDuration()){
                return o1.getCourse().getDuration() - o2.getCourse().getDuration();
            }
            return o1.getCourse().getSemester() - o2.getCourse().getSemester();
        }).toList();

        // While there are still unassigned courseSessions
        while(!courseSessions.isEmpty()){
            newDuration = courseSessions.getFirst().getDuration();
            // If all unassigned courseSessions of the same durations are processed
            if(newDuration != currentDuration){
                // Get the first available slots for the new duration
                for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
                    firstAvailables.put(availabilityMatrix.getEarliestAvailableSlotForDuration(newDuration), availabilityMatrix);
                }
                currentDuration = newDuration;
                // Sort keys
                keys = firstAvailables.keySet().stream().sorted((o1, o2) -> {
                    if(o1.getSlot() != o2.getSlot()){
                        return o1.getSlot() - o2.getSlot();
                    }
                    return o1.getDay() - o2.getDay();
                }).toList();
            }

            //Select courseSession
            currentCourseSession = courseSessions.removeFirst();

            while(true){
                //select possible availabilityMatrix and position for placement
                currentPosition = keys.removeFirst();
                currentAvailabilityMatrix = firstAvailables.get(currentPosition);


                //check constraints
                // ist es ein gruppenkurs?
                // ist es ein gesplitteter Kurs?
                if(checkRoomCapacity() && checkTimingConstraintsFulfilled() && checkCoursesOfSameSemester()){
                    break;
                }
            }

            //assign courseSession
            currentAvailabilityMatrix.assignCourseSession(currentPosition, currentDuration, currentCourseSession);
            firstAvailables.put(currentAvailabilityMatrix.getEarliestAvailableSlotForDuration(currentDuration), currentAvailabilityMatrix);

            //Sort the keys again after new put operation
            keys = firstAvailables.keySet().stream().sorted((o1, o2) -> {
                if(o1.getSlot() != o2.getSlot()){
                    return o1.getSlot() - o2.getSlot();
                }
                return o1.getDay() - o2.getDay();
            }).toList();
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
