package com.lecture.coordinator.services;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("session")
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room createRoom(String id, int capacity, boolean computersAvailable, List<Timing> timingConstraints){
        Room newRoom = new Room();
        newRoom.setId(id);
        newRoom.setCapacity(capacity);
        newRoom.setComputersAvailable(computersAvailable);
        newRoom.setTimingConstraints(timingConstraints);
        return roomRepository.save(newRoom);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room updateRoom(Room room, String id, int capacity, boolean computersAvailable, List<Timing> timingConstraints){
        room.setId(id);
        room.setCapacity(capacity);
        room.setComputersAvailable(computersAvailable);
        room.setTimingConstraints(timingConstraints);
        return roomRepository.save(room);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteRoom(Room room){
        //TODO: delete Room and unassign all courses that are assigned to this room
    }

}
