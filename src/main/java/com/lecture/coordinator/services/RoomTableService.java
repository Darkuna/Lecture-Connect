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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteRoomTable(RoomTable roomTable){
        System.out.println(roomTable.getTimingConstraints());
        courseSessionService.unassignCourseSessions(roomTable.getAssignedCourseSessions());
        timingService.deleteTimingConstraints(roomTable.getTimingConstraints());
        roomTable.setTimingConstraints(null);
        roomTableRepository.delete(roomTable);
    }

    public RoomTable loadRoomTableByID(long id){
        RoomTable roomTable =  roomTableRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("RoomTable not found for ID: " + id));
        roomTable.setAssignedCourseSessions(courseSessionService.loadAllAssignedToRoomTable(roomTable));
        roomTable.setTimingConstraints(timingService.loadTimingConstraintsOfRoomTable(roomTable));
        roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(roomTable.getTimingConstraints()));
        return roomTable;
    }

    public List<RoomTable> loadAllOfTimeTable(TimeTable timeTable){
        List<RoomTable> roomTables = roomTableRepository.findAllByTimeTable(timeTable);
        for(RoomTable roomTable : roomTables){
            roomTable.setAssignedCourseSessions(courseSessionService.loadAllAssignedToRoomTable(roomTable));
            roomTable.setTimingConstraints(timingService.loadTimingConstraintsOfRoomTable(roomTable));
            roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(roomTable.getTimingConstraints()));
        }
        return roomTables;
    }

    private AvailabilityMatrix initializeAvailabilityMatrix(List<Timing> timingConstraints){
        return new AvailabilityMatrix(Objects.requireNonNullElseGet(timingConstraints, List::of));
    }
}
