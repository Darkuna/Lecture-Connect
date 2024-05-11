package com.example.demo.services;

import com.example.demo.models.Room;
import com.example.demo.models.RoomTable;
import com.example.demo.repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for managing rooms within the system.
 * It handles the creation, update, and deletion of rooms, as well as fetching room details and lists of rooms.
 */
@Service
@Scope("session")
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTableService roomTableService;

    /**
     * Creates a new room and saves it to the database.
     *
     * @param id The unique identifier for the room.
     * @param capacity The capacity of the room.
     * @param computersAvailable Flag indicating whether the room has computers available.
     * @return The newly created and saved Room object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room createRoom(String id, int capacity, boolean computersAvailable){
        Room newRoom = new Room();
        newRoom.setId(id);
        newRoom.setCapacity(capacity);
        newRoom.setComputersAvailable(computersAvailable);
        return roomRepository.save(newRoom);
    }

    /**
     * Saves a provided room entity to the database.
     *
     * @param room The Room object to be saved.
     * @return The saved Room object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Updates the details of an existing room entity in the database.
     *
     * @param room The room entity to update.
     * @param capacity The new capacity of the room.
     * @param computersAvailable The new availability state of computers in the room.
     * @return The updated Room object.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room updateRoom(Room room, int capacity, boolean computersAvailable){
        room.setCapacity(capacity);
        room.setComputersAvailable(computersAvailable);
        return roomRepository.save(room);
    }

    /**
     * Deletes a specified room from the database.
     * Before deletion, it ensures that all associated room tables are also deleted.
     *
     * @param room The Room object to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteRoom(Room room){
        List<RoomTable> roomTables = roomTableService.loadRoomTableByRoom(room);
        for(RoomTable roomTable : roomTables){
            roomTableService.deleteRoomTable(roomTable);
        }
        roomRepository.delete(room);
    }

    /**
     * Loads all rooms stored in the database.
     *
     * @return A list of Room objects.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Room> loadAllRooms(){
        return roomRepository.findAll();
    }

    /**
     * Loads a room by its ID.
     *
     * @param id The ID of the room.
     * @return The Room object associated with the given ID.
     * @throws EntityNotFoundException If no room is found with the provided ID.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room loadRoomByID(String id) {
        return roomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Room not found for ID: " + id));
    }

    /**
     * Deletes multiple rooms from the database based on a provided list of Room objects.
     * Each room in the list is individually deleted along with its associated room tables.
     *
     * @param selectedRooms A list of Room objects to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteMultipleRooms(List<String> roomIds) {
        List<Room> roomsToBeDeleted = roomRepository.findAllById(roomIds);
        for(Room room : roomsToBeDeleted){
            deleteRoom(room);
        }
    }
}
