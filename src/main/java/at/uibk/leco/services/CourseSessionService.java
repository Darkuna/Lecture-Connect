package at.uibk.leco.services;

import at.uibk.leco.models.*;
import at.uibk.leco.models.enums.ChangeType;
import at.uibk.leco.models.enums.CourseType;
import at.uibk.leco.repositories.CourseSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing course sessions.
 * Enables the creation, assignment, fixation, and deletion of course sessions, along with their association to
 * roomTables and timeTables.
 */
@Service
@Scope("session")
public class CourseSessionService {

    private final CourseSessionRepository courseSessionRepository;
    private final TimingService timingService;
    private final GlobalTableChangeService globalTableChangeService;
    private static final Logger log = LoggerFactory.getLogger(CourseSessionService.class);

    @Autowired
    public CourseSessionService(CourseSessionRepository courseSessionRepository, TimingService timingService, GlobalTableChangeService globalTableChangeService) {
        this.courseSessionRepository = courseSessionRepository;
        this.timingService = timingService;
        this.globalTableChangeService = globalTableChangeService;
    }

    /**
     * Creates course sessions for a given course based on its split and group settings.
     *
     * @param course The course for which sessions are to be created.
     * @return A list of created course sessions.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> createCourseSessionsFromCourse(TimeTable timeTable, Course course){
        log.info("Course: split: {}, nrGroups: {}, splitTimes: {}", course.isSplit(), course.getNumberOfGroups(), course.getSplitTimes());
        List<CourseSession> courseSessions = new ArrayList<>();
        boolean isSplitCourse = course.isSplit();
        boolean hasGroups = course.getNumberOfGroups() > 1 ||
                course.getCourseType().equals(CourseType.PS) ||
                course.getCourseType().equals(CourseType.SL);
        int numberOfCourseSessionsToCreate = hasGroups ? course.getNumberOfGroups() : (isSplitCourse ? course.getSplitTimes().size() : 1);

        for(int i = 0; i < numberOfCourseSessionsToCreate; i++){
            CourseSession courseSession = new CourseSession();
            courseSession.setAssigned(false);
            courseSession.setFixed(false);
            courseSession.setElective(course.isElective());
            courseSession.setLecturer(course.getLecturer());
            courseSession.setNumberOfParticipants(course.getNumberOfParticipants());
            courseSession.setSemester(course.getSemester());
            courseSession.setComputersNecessary(course.isComputersNecessary());
            courseSession.setTimingConstraints(course.getTimingConstraints());
            courseSession.setCourseId(course.getId());
            courseSession.setTimeTable(timeTable);
            courseSession.setStudyType(course.getStudyType());

            if(isSplitCourse){
                courseSession.setDuration(course.getSplitTimes().get(i));
                courseSession.setName(course.getCourseType() + " " + course.getName() + " - Split " + (i+1));
            } else if (hasGroups){
                courseSession.setDuration(course.getDuration());
                courseSession.setName(course.getCourseType() + " " + course.getName() + " - Group " + (i+1));
            } else {
                courseSession.setDuration(course.getDuration());
                courseSession.setName(course.getCourseType() + " " + course.getName());
            }
            courseSessions.add(courseSession);
        }
        courseSessions = courseSessionRepository.saveAll(courseSessions);
        log.info("Created {} courseSessions from course {}", courseSessions.size(), course.getName());
        return courseSessions;
    }

    /**
     * Deletes a course session, unassigning it first if necessary.
     *
     * @param courseSession The course session to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteCourseSession(CourseSession courseSession){
        if(courseSession.getTiming() != null){
            timingService.deleteTiming(courseSession.getTiming());
        }
        courseSessionRepository.delete(courseSession);
        log.info("Deleted course session {}", courseSession.getName());
    }

    /**
     * Unassigns multiple course sessions.
     *
     * @param courseSessions List of course sessions to unassign.
     * @return The list of unassigned course sessions.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> unassignCourseSessions(List<CourseSession> courseSessions){
        for (CourseSession courseSession : courseSessions) {
            courseSession.setRoomTable(null);
            Timing timing = courseSession.getTiming();
            courseSession.setTiming(null);
            timingService.deleteTiming(timing);
            courseSession.setAssigned(false);
        }
        log.info("Unassigned {} course sessions", courseSessions.size());
        return courseSessionRepository.saveAll(courseSessions);
    }

    /**
     * Loads all course sessions assigned to a specific room table.
     *
     * @param roomTable The room table the course session are assigned to.
     * @return The list of course sessions assigned to the room table.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> loadAllAssignedToRoomTable(RoomTable roomTable){

        List<CourseSession> courseSessions = courseSessionRepository.findAllByRoomTable(roomTable);
        log.debug("Loaded all courseSessions assigned to roomTable {} ({})", roomTable.getRoomId(),
                courseSessions.size());
        for(CourseSession courseSession : courseSessions){
            courseSession.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(courseSession.getCourseId()));
        }
        return courseSessions;
    }

    /**
     * Loads all course sessions associated to a specific timetable.
     *
     * @param timeTable The timetable the course sessions are associated to.
     * @return The list of course sessions from a specific timetable.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> loadAllFromTimeTable(TimeTable timeTable){
        List<CourseSession> courseSessions = courseSessionRepository.findAllByTimeTable(timeTable);
        for(CourseSession courseSession : courseSessions){
            courseSession.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(courseSession.getCourseId()));
        }
        log.info("Loaded all courseSessions from timeTable {} ({})", timeTable, courseSessions.size());
        return courseSessions;
    }

    /**
     * Loads a course session from a given id.
     *
     * @param id ID of the course session to be loaded.
     * @return The course session.
     * @throws EntityNotFoundException if the course session could not be found in the database.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession loadCourseSessionByID(long id){
        CourseSession courseSession = courseSessionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CourseSession not found for ID: " + id));
        log.info("Loaded course session {}", courseSession.getName());
        courseSession.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(courseSession.getCourseId()));
        return courseSession;
    }

    /**
     * Method to save all entries in a list of courseSessions.
     * @param courseSessions list of CourseSession objects
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void saveAll(List<CourseSession> courseSessions) {
        courseSessionRepository.saveAll(courseSessions);
    }

    /**
     * Method to update a courseSession by comparing the database version with the new version received from frontend
     * @param timeTable the courseSession is assigned to
     * @param newCourseSession courseSession that contains the latest changes
     * @param original courseSession from the database
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void updateCourseSession(TimeTable timeTable, CourseSession newCourseSession, CourseSession original) {
        if(newCourseSession.wasUnsassigned(original)){
            globalTableChangeService.create(ChangeType.UNASSIGN_COURSE, timeTable, String.format("Course %s was unassigned from room %s at %s",
                    newCourseSession.getName(), original.getRoomTable(), original.getTiming()));
        }
        else if(newCourseSession.wasAssigned(original)){
            globalTableChangeService.create(ChangeType.ASSIGN_COURSE, timeTable, String.format("Course %s was assigned to room %s at %s",
                    newCourseSession.getName(), newCourseSession.getRoomTable(), newCourseSession.getTiming()));
        }
        else if(newCourseSession.wasMoved(original)){
            if(newCourseSession.getRoomTable().equals(original.getRoomTable())){
                globalTableChangeService.create(ChangeType.MOVE_COURSE, timeTable, String.format("Course %s was moved from %s to %s",
                        newCourseSession.getName(), original.getTiming(), newCourseSession.getTiming()));
            }
            else{
                globalTableChangeService.create(ChangeType.MOVE_COURSE, timeTable, String.format("Course %s was moved from room '%s' at %s to '%s' at %s",
                        newCourseSession.getName(), original.getRoomTable(), original.getTiming(), newCourseSession.getRoomTable(), newCourseSession.getTiming()));
            }
        }
        if(newCourseSession.wasFixed(original)){
            globalTableChangeService.create(ChangeType.FIX_COURSE, timeTable, String.format("Course %s was fixed in room %s at %s",
                    newCourseSession.getName(), newCourseSession.getRoomTable(), newCourseSession.getTiming()));
        }

        if(original.getTiming() == null){
            original.setTiming(timingService.createTiming(newCourseSession.getTiming()));
        }
        else if(!original.getTiming().equals(newCourseSession.getTiming())){
            Timing toDelete = original.getTiming();
            if(newCourseSession.getTiming() != null){
                original.setTiming(timingService.createTiming(newCourseSession.getTiming()));
            }
            else{
                original.setTiming(null);
            }

            timingService.deleteTiming(toDelete);
        }
        original.setFixed(newCourseSession.isFixed());
        original.setRoomTable(newCourseSession.getRoomTable());
        original.setAssigned(newCourseSession.isAssigned());

        courseSessionRepository.save(original);

    }

    public void createCourseSession(CourseSession courseSession, TimeTable timeTable) {
        CourseSession newCourseSession = new CourseSession();
        newCourseSession.setName(courseSession.getName());
        newCourseSession.setRoomTable(courseSession.getRoomTable());
        newCourseSession.setAssigned(courseSession.isAssigned());
        newCourseSession.setFixed(courseSession.isFixed());
        if(courseSession.getTiming() != null){
            Timing timing = timingService.createTiming(courseSession.getTiming());
            newCourseSession.setTiming(timing);
            globalTableChangeService.create(ChangeType.ASSIGN_COURSE, timeTable,
                    String.format("Course '%s' was assigned in room %s at %s", courseSession.getName(),
                            courseSession.getRoomTable(), courseSession.getTiming()));
        }

        CourseSession toCopy = courseSessionRepository.findFirstByCourseIdAndTimeTable(courseSession.getCourseId(), timeTable);
        newCourseSession.setCourseId(toCopy.getCourseId());
        newCourseSession.setSemester(toCopy.getSemester());
        newCourseSession.setLecturer(toCopy.getLecturer());
        newCourseSession.setStudyType(toCopy.getStudyType());
        newCourseSession.setDuration(toCopy.getDuration());
        newCourseSession.setTimeTable(toCopy.getTimeTable());
        newCourseSession.setNumberOfParticipants(toCopy.getNumberOfParticipants());
        newCourseSession.setElective(toCopy.isElective());
        newCourseSession.setComputersNecessary(toCopy.isComputersNecessary());

        courseSessionRepository.save(newCourseSession);
    }

    /**
     * This copy method is only used in unit tests
     * @param courseSession to be copied
     * @return copy of courseSession
     */
    public CourseSession copyCourseSession(CourseSession courseSession){
        CourseSession copy = new CourseSession(courseSession);
        if(courseSession.getTiming() != null){
            copy.setTiming(timingService.createTiming(courseSession.getTiming()));
        }
        return copy;
    }
}
