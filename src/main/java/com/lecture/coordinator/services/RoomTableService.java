package com.lecture.coordinator.services;

import com.lecture.coordinator.model.CourseSession;
import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.RoomTable;
import com.lecture.coordinator.repositories.RoomTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("session")
public class RoomTableService {
    @Autowired
    private RoomTableRepository roomTableRepository;

    public RoomTable createRoomTableFromRoom(Room room){
        RoomTable roomTable = new RoomTable();
        roomTable.setRoom(room);

        //TODO: Think of a way to use a room's timingContraints to create the available time in a RoomTable object

        return roomTableRepository.save(roomTable);
    }

    public List<CourseSession> loadAssignedCourses(RoomTable roomTable){
        //TODO: Load all courses assigned to the room assigned to the roomTable
        return null;
    }
}
