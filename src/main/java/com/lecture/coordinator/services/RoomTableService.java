package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
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
    @Autowired
    private TimingService timingService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public RoomTable createRoomTableFromRoom(TimeTable timeTable, Room room){
        RoomTable roomTable = new RoomTable();
        roomTable.setRoom(room);
        roomTable.setTimeTable(timeTable);
        roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(room.getTimingConstraints()));

        return roomTableRepository.save(roomTable);
    }
    public RoomTable loadRoomTableByRoom(Room room){
        return roomTableRepository.findRoomTableByRoom(room);
    }

    public List<RoomTable> loadRoomTablesOfTimeTable(TimeTable timeTable){
        List<RoomTable> roomTables = roomTableRepository.findAllByTimeTable(timeTable);
        for(RoomTable roomTable : roomTables){
            List<CourseSession> assignedCourseSessions = courseSessionService.loadAllAssignedToRoomTableInTimeTable(timeTable, roomTable);
            roomTable.setAssignedCourseSessions(assignedCourseSessions);
            List<Timing> timingConstraints = timingService.loadTimingConstraintsOfRoom(roomTable.getRoom());
            roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(timingConstraints));
        }
        return roomTables;
    }

    private AvailabilityMatrix initializeAvailabilityMatrix(List<Timing> timingConstraints){
        if(timingConstraints != null){
            return new AvailabilityMatrix(timingConstraints);
        }
        return new AvailabilityMatrix(List.of());
    }
}
