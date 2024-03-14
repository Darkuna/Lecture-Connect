package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class TimeTableService {
    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private RoomTableService roomTableService;

    public TimeTable createTimeTable(Semester semester, int year, List<Room> rooms, List<Course> courses){
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semester);
        timeTable.setYear(year);
        timeTable.setRooms(rooms);
        timeTable.setCourses(courses);

        return timeTableRepository.save(timeTable);
    }

    public void createRoomTables(TimeTable timeTable){
        List<RoomTable> roomTables = new ArrayList<>();
        for(Room room : timeTable.getRooms()){
            RoomTable roomTable = roomTableService.createRoomTableFromRoom(room);
            roomTable.setTimeTable(timeTable);
            roomTables.add(roomTable);
        }
        timeTable.setRoomTables(roomTables);
    }

    public void loadCourseSessions(TimeTable timeTable){
        timeTable.setCourseSessions(timeTable.getCourses().stream()
                .flatMap(course -> course.getCourseSessions().stream())
                .collect(Collectors.toList()));
    }
}
