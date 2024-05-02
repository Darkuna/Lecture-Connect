package com.example.demo.controllers;

import com.example.demo.dto.RoomDTO;
import com.example.demo.models.Room;
import com.example.demo.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@Scope("session")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO roomDto) {
        Room room = convertToEntity(roomDto);
        Room savedRoom = roomService.createRoom(room);
        return ResponseEntity.ok(convertToDto(savedRoom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable String id, @RequestBody RoomDTO roomDto) {
        Room existingRoom = roomService.loadRoomByID(id);
        copyDtoToEntity(roomDto, existingRoom);
        Room updatedRoom = roomService.updateRoom(existingRoom, roomDto.getCapacity(), roomDto.isComputersAvailable());
        return ResponseEntity.ok(convertToDto(updatedRoom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        Room room = roomService.loadRoomByID(id);
        roomService.deleteRoom(room);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<Room> rooms = roomService.loadAllRooms();
        return ResponseEntity.ok(rooms.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable String id) {
        Room room = roomService.loadRoomByID(id);
        return ResponseEntity.ok(convertToDto(room));
    }

    private RoomDTO convertToDto(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setCapacity(room.getCapacity());
        dto.setComputersAvailable(room.isComputersAvailable());
        return dto;
    }

    private Room convertToEntity(RoomDTO dto) {
        Room room = new Room();
        room.setId(dto.getId());
        room.setCapacity(dto.getCapacity());
        room.setComputersAvailable(dto.isComputersAvailable());
        return room;
    }

    private void copyDtoToEntity(RoomDTO dto, Room entity) {
        entity.setCapacity(dto.getCapacity());
        entity.setComputersAvailable(dto.isComputersAvailable());
    }
}

