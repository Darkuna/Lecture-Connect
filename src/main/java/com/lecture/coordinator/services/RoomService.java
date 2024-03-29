package com.lecture.coordinator.services;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Scope("session")
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TimingService timingService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room createRoom(String id, int capacity, boolean computersAvailable){
        Room newRoom = new Room();
        newRoom.setId(id);
        newRoom.setCapacity(capacity);
        newRoom.setComputersAvailable(computersAvailable);
        return roomRepository.save(newRoom);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room updateRoom(Room room, int capacity, boolean computersAvailable){
        room.setCapacity(capacity);
        room.setComputersAvailable(computersAvailable);
        return roomRepository.save(room);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteRoom(Room room){
        roomRepository.delete(room);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Room> loadAllRooms(){
        return roomRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room loadRoomByID(String id) {
        return roomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Room not found for ID: " + id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteMultipleRooms(List<Room> selectedRooms) {
        for(Room room : selectedRooms){
            deleteRoom(room);
        }
    }
}
