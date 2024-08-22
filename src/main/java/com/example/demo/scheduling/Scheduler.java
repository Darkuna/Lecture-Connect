package com.example.demo.scheduling;

import com.example.demo.models.AvailabilityMatrix;
import com.example.demo.models.CourseSession;
import com.example.demo.models.RoomTable;
import com.example.demo.models.TimeTable;
import com.example.demo.services.CourseSessionService;
import com.example.demo.services.TimingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("session")
public abstract class Scheduler {
    protected boolean usePreferredOnly;
    protected List<AvailabilityMatrix> availabilityMatricesOfRoomsWithComputers;
    protected List<AvailabilityMatrix> availabilityMatricesOfRoomsWithoutComputers;
    protected List<AvailabilityMatrix> allAvailabilityMatrices;
    protected List<CourseSession> courseSessionsWithComputersNeeded;
    protected List<CourseSession> courseSessionsWithoutComputersNeeded;
    protected final Random random = new Random(System.currentTimeMillis());
    protected Queue<Candidate> candidateQueue;
    protected final Logger log = LoggerFactory.getLogger(Scheduler.class);
    protected final Map<CourseSession, Candidate> readyForAssignmentSet = new HashMap<>();
    protected TimeTable timeTable;
    protected int numberOfCourseSessions;

    protected final TimingService timingService;
    protected final CourseSessionService courseSessionService;

    public Scheduler(TimingService timingService, CourseSessionService courseSessionService) {
        this.timingService = timingService;
        this.courseSessionService = courseSessionService;
    }

    /**
     * Initializes the Scheduler with a certain timeTable.
     * The courseSessions are split into ones that need a room with computers and others that don't need a
     * room with computers. Also, the availabilityMatrices are split into ones of computer rooms and others
     * of rooms without computers.
     * @param timeTable to initialize Scheduler with
     */
    public void setTimeTable(TimeTable timeTable){
        this.timeTable = timeTable;
        availabilityMatricesOfRoomsWithComputers = new ArrayList<>();
        availabilityMatricesOfRoomsWithoutComputers = new ArrayList<>();
        allAvailabilityMatrices = new ArrayList<>();
        courseSessionsWithoutComputersNeeded = new ArrayList<>();
        courseSessionsWithComputersNeeded = new ArrayList<>();
        for(RoomTable roomTable : timeTable.getRoomTablesWithComputersAvailable()){
            availabilityMatricesOfRoomsWithComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        for(RoomTable roomTable : timeTable.getRoomTablesWithoutComputersAvailable()){
            availabilityMatricesOfRoomsWithoutComputers.add(roomTable.getAvailabilityMatrix());
            allAvailabilityMatrices.add(roomTable.getAvailabilityMatrix());
        }
        this.courseSessionsWithComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithComputersNeeded());
        this.courseSessionsWithoutComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithoutComputersNeeded());
        this.candidateQueue = new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot));
        this.usePreferredOnly = true;
    }

    public abstract void assignUnassignedCourseSessions();
    public abstract List<CourseSession> collisionCheck(TimeTable timeTable);
}
