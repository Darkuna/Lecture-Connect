package com.lecture.coordinator.services;

import com.lecture.coordinator.model.CourseSession;
import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.RoomTable;
import com.lecture.coordinator.model.TimeTable;
import com.lecture.coordinator.repositories.RoomTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("session")
public class RoomTableService {
    @Autowired
    private RoomTableRepository roomTableRepository;
    @Autowired
    private CourseSessionService courseSessionService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public RoomTable createRoomTableFromRoom(TimeTable timeTable, Room room){
        RoomTable roomTable = new RoomTable();
        roomTable.setRoom(room);
        roomTable.setTimeTable(timeTable);

        //TODO: Think of a way to use a room's timingConstraints to create the available time in a RoomTable object

        return roomTableRepository.save(roomTable);
    }

    public List<CourseSession> loadAssignedCourses(RoomTable roomTable){
        return courseSessionService.loadAllAssignedToRoom(roomTable.getRoom());
    }

    public RoomTable loadRoomTableByRoom(Room room){
        return roomTableRepository.findRoomTableByRoom(room);
    }
}
