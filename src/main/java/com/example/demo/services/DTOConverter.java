package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.models.*;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.Semester;
import com.example.demo.models.enums.Status;
import com.example.demo.models.enums.TimingType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class DTOConverter {
    /**
     * Converts a Course object into a CourseDTO object
     *
     * @param courseSession to be converted
     * @return CourseSessionDTO object
     */
    public CourseSessionDTO toCourseSessionDTO(CourseSession courseSession) {
        CourseSessionDTO dto = new CourseSessionDTO();
        dto.setId(courseSession.getId());
        dto.setName(courseSession.getName());
        dto.setAssigned(courseSession.isAssigned());
        dto.setFixed(courseSession.isFixed());
        if(courseSession.getRoomTable() != null){
            dto.setRoomTableId(courseSession.getRoomTable().getId());
        }
        dto.setDuration(courseSession.getDuration());
        if(courseSession.getTiming() != null){
            dto.setTiming(toTimingDTO(courseSession.getTiming()));
        }
        return dto;
    }
    /**
     * Converts a CourseDTO object into a Course object
     *
     * @param dto to be converted
     * @return CourseSession object
     */
    public CourseSession toCourseSession(CourseSessionDTO dto) {
        CourseSession courseSession = new CourseSession();
        courseSession.setId(dto.getId());
        courseSession.setName(dto.getName());
        courseSession.setAssigned(dto.isAssigned());
        courseSession.setFixed(dto.isFixed());
        courseSession.setDuration(dto.getDuration());
        courseSession.setTiming(toTiming(dto.getTiming()));
        return courseSession;
    }

    /**
     * Converts a Course object into a CourseDTO object
     *
     * @param course to be converted
     * @return CourseDTO object
     */
    public CourseDTO toCourseDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setCourseType(course.getCourseType());
        dto.setLecturer(course.getLecturer());
        dto.setSemester(course.getSemester());
        dto.setDuration(course.getDuration());
        dto.setNumberOfParticipants(course.getNumberOfParticipants());
        dto.setComputersNecessary(course.isComputersNecessary());
        dto.setTimingConstraints(course.getTimingConstraints().stream()
                .map(this::toTimingDTO)
                .collect(Collectors.toList()));
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        return dto;
    }

    /**
     * Converts a CourseDTO object into a Course object
     *
     * @param dto to be converted
     * @return Course object
     */
    public Course toCourse(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setCourseType(dto.getCourseType());
        course.setLecturer(dto.getLecturer());
        course.setSemester(dto.getSemester());
        course.setDuration(dto.getDuration());
        course.setNumberOfParticipants(dto.getNumberOfParticipants());
        course.setComputersNecessary(dto.isComputersNecessary());
        course.setTimingConstraints(dto.getTimingConstraints().stream()
                .map(this::toTiming)
                .collect(Collectors.toList()));
        return course;
    }

    /**
     * Converts a Room object into a RoomDTO object
     *
     * @param room to be converted
     * @return RoomDTO object
     */
    public RoomDTO toRoomDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setCapacity(room.getCapacity());
        dto.setComputersAvailable(room.isComputersAvailable());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());
        return dto;
    }

    /**
     * Converts a RoomDTO object into a Room object
     *
     * @param dto to be converted
     * @return Room object
     */
    public Room toRoom(RoomDTO dto) {
        Room room = new Room();
        room.setId(dto.getId());
        room.setCapacity(dto.getCapacity());
        room.setComputersAvailable(dto.isComputersAvailable());
        room.setCreatedAt(dto.getCreatedAt());
        room.setUpdatedAt(dto.getUpdatedAt());
        room.setTimingConstraints(dto.getTimingConstraints());
        return room;
    }

    /**
     * Converts a RoomTable object into a RoomTableDTO object
     *
     * @param roomTable to be converted
     * @return RoomTableDTO object
     */
    public RoomTableDTO toRoomTableDTO(RoomTable roomTable) {
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
    public RoomTable toRoomTable(RoomTableDTO dto) {
        RoomTable roomTable = new RoomTable();
        roomTable.setId(dto.getId());
        return roomTable;
    }

    /**
     * Converts a TimeTable object into a TimeTableDTO object
     *
     * @param timeTable to be converted
     * @return TimeTableDTO object
     */
    public TimeTableDTO toTimeTableDTO(TimeTable timeTable) {
        TimeTableDTO dto = new TimeTableDTO();
        dto.setId(timeTable.getId());
        dto.setSemester(timeTable.getSemester().toString());
        dto.setYear(timeTable.getYear());
        dto.setStatus(timeTable.getStatus().toString());
        dto.setCreatedAt(timeTable.getCreatedAt());
        dto.setUpdatedAt(timeTable.getUpdatedAt());

        dto.setRoomTables(timeTable.getRoomTables().stream()
                .map(this::toRoomTableDTO)
                .collect(Collectors.toList()));

        dto.setCourseSessions(timeTable.getCourseSessions().stream()
                .map(this::toCourseSessionDTO)
                .collect(Collectors.toList()));

        return dto;
    }
    /**
     * Converts a TimeTableDTO object into a TimeTable object
     *
     * @param dto to be converted
     * @return TimeTable object
     */
    public TimeTable toTimeTable(TimeTableDTO dto) {
        TimeTable timeTable = new TimeTable();
        timeTable.setId(dto.getId());
        timeTable.setSemester(Semester.valueOf(dto.getSemester()));
        timeTable.setStatus(Status.valueOf(dto.getStatus()));
        timeTable.setYear(dto.getYear());
        timeTable.setCreatedAt(dto.getCreatedAt());
        timeTable.setUpdatedAt(dto.getUpdatedAt());

        timeTable.setRoomTables(dto.getRoomTables().stream()
                .map(this::toRoomTable)
                .collect(Collectors.toList()));

        timeTable.setCourseSessions(dto.getCourseSessions().stream()
                .map(this::toCourseSession)
                .collect(Collectors.toList()));

        return timeTable;
    }

    /**
     * Converts a Timing object into a TimingDTO object
     *
     * @param timing to be converted
     * @return TimingDTO object
     */
    public TimingDTO toTimingDTO(Timing timing) {
        TimingDTO timingDTO = new TimingDTO();
        timingDTO.setId(timing.getId());
        timingDTO.setStartTime(timing.getStartTime());
        timingDTO.setEndTime(timing.getEndTime());
        timingDTO.setDay(timing.getDay().toString());
        timingDTO.setTimingType(timing.getTimingType().toString());
        return timingDTO;
    }

    /**
     * Converts a TimingDTO object into a Timing object
     *
     * @param dto to be converted
     * @return Timing object
     */
    public Timing toTiming(TimingDTO dto) {
        Timing timing = new Timing();
        timing.setId(dto.getId());
        timing.setStartTime(dto.getStartTime());
        timing.setEndTime(dto.getEndTime());
        timing.setTimingType(TimingType.valueOf(dto.getTimingType()));
        if (dto.getDay() != null) {
            timing.setDay(Day.valueOf(dto.getDay()));
        }
        return timing;
    }

    public void copyCourseDtoToEntity(CourseDTO dto, Course entity) {
        entity.setName(dto.getName());
        entity.setCourseType(dto.getCourseType());
        entity.setLecturer(dto.getLecturer());
        entity.setSemester(dto.getSemester());
        entity.setDuration(dto.getDuration());
        entity.setNumberOfParticipants(dto.getNumberOfParticipants());
        entity.setComputersNecessary(dto.isComputersNecessary());
        List<Timing> timings = dto.getTimingConstraints().stream()
                .map(this::toTiming)
                .collect(Collectors.toList());
        entity.setTimingConstraints(timings);
    }

    public void copyRoomDtoToEntity(RoomDTO dto, Room entity) {
        entity.setCapacity(dto.getCapacity());
        entity.setComputersAvailable(dto.isComputersAvailable());
    }
}
