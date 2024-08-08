package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service class for managing rooms within the system.
 * It handles the creation, update, and deletion of rooms, as well as fetching room details and lists of rooms.
 */
@Service
@Scope("session")
public class RoomService {

    private final RoomRepository roomRepository;
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

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
        newRoom = roomRepository.save(newRoom);
        log.info("Created room with id {}", newRoom.getId());
        return newRoom;
    }

    /**
     * Saves a provided room entity to the database.
     *
     * @param room The Room object to be saved.
     * @return The saved Room object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Room createRoom(Room room) {
        room = roomRepository.save(room);
        log.info("Created room with id {}", room.getId());
        return room;
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
        log.info("Updated room with id {}", room.getId());
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
        roomRepository.delete(room);
        log.info("Deleted room with id {}", room.getId());
    }

    /**
     * Loads all rooms stored in the database.
     *
     * @return A list of Room objects.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Room> loadAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        log.info("Loaded all rooms ({})", rooms.size());
        return rooms;
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
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Room not found for ID: " + id));
        log.info("Loaded room with id {}", room.getId());
        return room;
    }

    /**
     * Deletes multiple rooms from the database based on a provided list of Room objects.
     * Each room in the list is individually deleted along with its associated room tables.
     *
     * @param roomIds A list of ids of Room objects to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteMultipleRooms(List<String> roomIds) {
        List<Room> roomsToBeDeleted = roomRepository.findAllById(roomIds);
        for(Room room : roomsToBeDeleted){
            deleteRoom(room);
        }
        log.info("Deleted {} rooms", roomIds.size());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Room> loadAllRoomsNotInTimeTable(TimeTable timeTable){
        List<Room> rooms = roomRepository.findAll();
        Set<String> roomIds = new HashSet<>();
        for(RoomTable roomTable : timeTable.getRoomTables()){
            roomIds.add(roomTable.getRoomId());
        }
        rooms = rooms.stream()
                .filter(r -> !roomIds.contains(r.getId()))
                .collect(Collectors.toList());
        return rooms;
    }
}
