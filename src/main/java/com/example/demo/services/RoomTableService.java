package com.example.demo.services;

import com.example.demo.dto.RoomTableDTO;
import com.example.demo.models.*;
import com.example.demo.repositories.RoomTableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Service class for managing room tables.
 * This includes creating, deleting, and loading room tables, as well as handling their associations with
 * rooms and timetables.
 */
@Service
@Scope("session")
public class RoomTableService {
    @Autowired
    private RoomTableRepository roomTableRepository;
    @Autowired
    private CourseSessionService courseSessionService;
    @Autowired
    private TimingService timingService;

    /**
     * Creates a room table for a specified room and timetable, initializing its availability matrix based
     * on the room's timing constraints.
     *
     * @param timeTable The timetable associated with the new room table.
     * @param room The room associated with the new room table.
     * @return The newly created and saved RoomTable object.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public RoomTable createRoomTableFromRoom(TimeTable timeTable, Room room){
        RoomTable roomTable = new RoomTable();
        roomTable.setRoom(room);
        roomTable.setTimeTable(timeTable);
        roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(room.getTimingConstraints()));

        return roomTableRepository.save(roomTable);
    }

    /**
     * Deletes a specified room table from the database, unassigning any course sessions and
     * deleting its timing constraints beforehand.
     *
     * @param roomTable The RoomTable object to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteRoomTable(RoomTable roomTable){
        courseSessionService.unassignCourseSessions(roomTable.getAssignedCourseSessions());
        timingService.deleteTimingConstraints(roomTable.getTimingConstraints());
        roomTable.setTimingConstraints(null);
        roomTableRepository.delete(roomTable);
    }

    /**
     * Loads a room table by its ID, supplementing it with assigned course sessions and timing constraints.
     * Also initializes the availability matrix
     *
     * @param id The ID of the room table.
     * @return The RoomTable object associated with the given ID.
     * @throws EntityNotFoundException If no room table is found for the provided ID.
     */
    public RoomTable loadRoomTableByID(long id){
        RoomTable roomTable =  roomTableRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("RoomTable not found for ID: " + id));
        roomTable.setAssignedCourseSessions(courseSessionService.loadAllAssignedToRoomTable(roomTable));
        roomTable.setTimingConstraints(timingService.loadTimingConstraintsOfRoomTable(roomTable));
        roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(roomTable.getTimingConstraints()));
        return roomTable;
    }

    /**
     * Loads all room tables associated with a specific room, each supplemented with assigned course sessions
     * and timing constraints.
     * The availability matrix is not initialized, since this method is only used for loading all room tables
     * for cascading deletion when deleting a room object.
     *
     * @param room The room for which to load room tables.
     * @return A list of RoomTable objects associated with the given room.
     */
    public List<RoomTable> loadRoomTableByRoom(Room room){
        List<RoomTable> roomTables = roomTableRepository.findAllByRoom(room);
        for(RoomTable roomTable : roomTables){
            roomTable.setAssignedCourseSessions(courseSessionService.loadAllAssignedToRoomTable(roomTable));
            roomTable.setTimingConstraints(timingService.loadTimingConstraintsOfRoomTable(roomTable));
        }
        return roomTables;
    }

    /**
     * Loads all room tables associated with a specific timetable, each supplemented with assigned course sessions,
     * timing constraints, and an updated availability matrix.
     *
     * @param timeTable The timetable for which to load room tables.
     * @return A list of RoomTable objects associated with the given timetable.
     */
    public List<RoomTable> loadAllOfTimeTable(TimeTable timeTable){
        List<RoomTable> roomTables = roomTableRepository.findAllByTimeTable(timeTable);
        for(RoomTable roomTable : roomTables){
            roomTable.setAssignedCourseSessions(courseSessionService.loadAllAssignedToRoomTable(roomTable));
            roomTable.setTimingConstraints(timingService.loadTimingConstraintsOfRoomTable(roomTable));
            roomTable.setAvailabilityMatrix(initializeAvailabilityMatrix(roomTable.getTimingConstraints()));
        }
        return roomTables;
    }

    /**
     * Initializes an availability matrix for a room table based on its timing constraints.
     *
     * @param timingConstraints The list of timing constraints for the room table.
     * @return An initialized AvailabilityMatrix object.
     */
    private AvailabilityMatrix initializeAvailabilityMatrix(List<Timing> timingConstraints){
        return new AvailabilityMatrix(Objects.requireNonNullElseGet(timingConstraints, List::of));
    }

    /**
     * Converts a RoomTable object into a RoomTableDTO object
     *
     * @param roomTable to be converted
     * @return RoomTableDTO object
     */
    public RoomTableDTO toDTO(RoomTable roomTable) {
        RoomTableDTO dto = new RoomTableDTO();
        dto.setId(roomTable.getId());
        dto.setRoomId(roomTable.getRoom().getId());
        return dto;
    }

    /**
     * Converts a RoomTableDTO object into a RoomTable object
     *
     * @param dto to be converted
     * @return RoomTable object
     */
    public RoomTable fromDTO(RoomTableDTO dto) {
        RoomTable roomTable = new RoomTable();
        roomTable.setId(dto.getId());
        return roomTable;
    }
}
