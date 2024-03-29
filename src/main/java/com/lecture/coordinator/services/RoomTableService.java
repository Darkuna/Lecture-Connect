package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.RoomTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

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

    public RoomTable loadRoomTableByID(long id){
        return roomTableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("RoomTable not found for ID: " + id));
    }

    public List<RoomTable> loadAllOfTimeTable(TimeTable timeTable){
        List<RoomTable> roomTables = roomTableRepository.findAllByTimeTable(timeTable);
        for(RoomTable roomTable : roomTables){
            List<CourseSession> assignedCourseSessions = courseSessionService.loadAllAssignedToRoomTable(roomTable);
            roomTable.setAssignedCourseSessions(assignedCourseSessions);
            List<Timing> timingConstraints = timingService.loadTimingConstraintsOfRoomTable(roomTable);
            roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(timingConstraints));
        }
        return roomTables;
    }

    private AvailabilityMatrix initializeAvailabilityMatrix(List<Timing> timingConstraints){
        return new AvailabilityMatrix(Objects.requireNonNullElseGet(timingConstraints, List::of));
    }
}
