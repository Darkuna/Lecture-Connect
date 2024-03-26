package com.lecture.coordinator.services;

import com.lecture.coordinator.model.CourseSession;
import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.model.Timing;
import com.lecture.coordinator.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Scope("session")
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TimingService timingService;

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
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room updateRoom(Room room, int capacity, boolean computersAvailable, List<Timing> timingConstraints){
        room.setCapacity(capacity);
        room.setComputersAvailable(computersAvailable);
        room.setTimingConstraints(timingConstraints);
        return roomRepository.save(room);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteRoom(Room room){
        roomRepository.delete(room);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Room> loadAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        for(Room room : rooms){
            //TODO: Are the timing constraints necessary for room list?
            room.setTimingConstraints(timingService.loadTimingConstraintsOfRoom(room));
        }
        return rooms;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room loadRoomByID(String id){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        Room room = null;
        if(optionalRoom.isPresent()){
            room = optionalRoom.get();
            room.setTimingConstraints(timingService.loadTimingConstraintsOfRoom(room));
        }
        return room;
    }

    public void deleteMultipleRooms(List<Room> selectedRooms) {
        for(Room room : selectedRooms){
            deleteRoom(room);
        }
    }
}
