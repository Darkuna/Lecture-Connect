package at.uibk.leco.services;

import at.uibk.leco.models.Room;
import at.uibk.leco.models.RoomTable;
import at.uibk.leco.models.TimeTable;
import at.uibk.leco.models.Timing;
import at.uibk.leco.repositories.RoomTableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing room tables.
 * This includes creating, deleting, and loading room tables, as well as handling their associations with
 * rooms and timetables.
 */
@Service
@Scope("session")
public class RoomTableService {

    private final RoomTableRepository roomTableRepository;
    private final CourseSessionService courseSessionService;
    private final TimingService timingService;
    private static final Logger log = LoggerFactory.getLogger(RoomTableService.class);

    public RoomTableService(RoomTableRepository roomTableRepository, CourseSessionService courseSessionService,
                            TimingService timingService) {
        this.roomTableRepository = roomTableRepository;
        this.courseSessionService = courseSessionService;
        this.timingService = timingService;
    }

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
        List<Timing> timingConstraints = new ArrayList<>();
        roomTable.setRoomId(room.getId());
        roomTable.setCapacity(room.getCapacity());
        roomTable.setComputersAvailable(room.isComputersAvailable());
        roomTable.setTimeTable(timeTable);
        for(Timing timing : room.getTimingConstraints()){
            timingConstraints.add(timingService.createTiming(timing));
        }
        for(Timing timingConstraint : timingConstraints){
            timingConstraint.setRoomTable(roomTable);
        }
        roomTable.setTimingConstraints(timingConstraints);
        roomTable = roomTableRepository.save(roomTable);
        log.info("Created roomTable for room {}", room.getId());
        return roomTable;
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
        log.info("Loaded roomTable with id {}", roomTable.getId());
        return roomTable;
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
        }
        log.info("Loaded all roomTables of timeTable {} ({})", timeTable.getId(), roomTables.size());
        return roomTables;
    }
}
