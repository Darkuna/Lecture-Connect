package com.example.demo.scheduling;

import com.example.demo.models.AvailabilityMatrix;
import com.example.demo.models.CourseSession;
import com.example.demo.models.RoomTable;
import com.example.demo.models.TimeTable;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private TimeTable timeTable;
    private List<AvailabilityMatrix> availabilityMatrices;
    private List<CourseSession> courseSessions;

    public Scheduler(TimeTable timeTable){
        this.timeTable = timeTable;
        availabilityMatrices = new ArrayList<>();
        for(RoomTable roomTable : timeTable.getRoomTables()){
            availabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        this.courseSessions = timeTable.getCourseSessions();
    }

    public void assignUnassignedCourseSessions(){

    }


}
