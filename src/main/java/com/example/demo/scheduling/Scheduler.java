package com.example.demo.scheduling;

import com.example.demo.models.*;
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
    protected Map<CourseSession, Candidate> readyForAssignmentSet = new HashMap<>();
    protected TimeTable timeTable;
    protected int numberOfCourseSessions;
    Stack<AssignmentStackEntry> assignmentStack;

    protected final TimingService timingService;
    protected final CourseSessionService courseSessionService;

    public Scheduler(TimingService timingService, CourseSessionService courseSessionService) {
        this.timingService = timingService;
        this.courseSessionService = courseSessionService;
    }

    public abstract void assignUnassignedCourseSessions();

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
            AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(roomTable);
            availabilityMatricesOfRoomsWithComputers.add(availabilityMatrix);
            allAvailabilityMatrices.add(availabilityMatrix);
        }
        for(RoomTable roomTable : timeTable.getRoomTablesWithoutComputersAvailable()){
            AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(roomTable);
            availabilityMatricesOfRoomsWithoutComputers.add(availabilityMatrix);
            allAvailabilityMatrices.add(availabilityMatrix);
        }
        this.courseSessionsWithComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithComputersNeeded());
        this.courseSessionsWithoutComputersNeeded = new ArrayList<>(timeTable.getUnassignedCourseSessionsWithoutComputersNeeded());
        this.candidateQueue = new PriorityQueue<>(Comparator.comparingInt(Candidate::getSlot));
        this.usePreferredOnly = true;
        this.assignmentStack = new Stack<>();
    }

    /**
     * Checks preconditions for a certain list of courseSessions and availabilityMatrices before starting assignment.
     * Precondition checks are used to determine in advance if the assignment is even possible considering time needed
     * and time available.
     *
     * @param courseSessions to be checked
     * @param availabilityMatrices to be checked
     * @return true if all checks were successful, false if at least one check failed
     */
    protected boolean checkPreConditions(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        log.info("Starting precondition checks ...");
        if(!checkAvailableTime(courseSessions, availabilityMatrices)){
            log.error("Not enough time available to assign all courseSessions");
            return false;
        }
        else{
            log.info("+ Available time check successful");
        }
        if(!checkAvailableTimePerRoomCapacity(courseSessions, availabilityMatrices)){
            log.error("- Not enough time available to assign all courseSessions based on their numberOfParticipants");
            return false;
        }
        else{
            log.info("+ Available time per capacity check successful");
        }
        log.info("Precondition checks successful");
        return true;
    }

    /**
     * Checks if there is enough available time for all numbers of participants. Therefore, it begins with the largest
     * number of participants n and checks if there is enough available time in availabilityMatrices with capacity >= n.
     * This continues for all numbers of participants in descending order.
     *
     * @param courseSessions to be checked
     * @param availabilityMatrices to be checked
     * @return true if there is enough available time for all numbers of participants, false if at least one does not
     * have enough space
     */
    protected boolean checkAvailableTimePerRoomCapacity(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        Set<Integer> numbersOfParticipants = new HashSet<>();
        List<Integer> numbersOfParticipantsSorted = new ArrayList<>();
        long totalTimeAvailable;
        long totalTimeNeeded;
        long totalPreferredTimeAvailable;
        for(CourseSession courseSession : courseSessions){
            numbersOfParticipants.add(courseSession.getNumberOfParticipants());
            numbersOfParticipantsSorted = numbersOfParticipants.stream().sorted().toList();
        }
        for(Integer number : numbersOfParticipantsSorted){
            totalTimeNeeded = courseSessions.stream()
                    .filter(c -> c.getNumberOfParticipants() >= number)
                    .mapToLong(CourseSession::getDuration)
                    .sum();
            totalTimeAvailable = availabilityMatrices.stream()
                    .filter(a -> a.getCapacity() >= number)
                    .mapToLong(AvailabilityMatrix::getTotalAvailableTime)
                    .sum();
            totalPreferredTimeAvailable = availabilityMatrices.stream()
                    .filter(a -> a.getCapacity() >= number)
                    .mapToLong(AvailabilityMatrix::getTotalAvailableTime)
                    .sum();
            if (totalTimeAvailable < totalTimeNeeded) {
                return false;
            }
            if(totalPreferredTimeAvailable < totalTimeNeeded){
                log.info("There is not enough preferred time available for courseSessions with {} or more participants", number);
            }
        }
        return true;
    }

    /**
     * Checks if there is enough total available time to assign all courseSessions to the availabilityMatrices.
     *
     * @param courseSessions to be checked
     * @param availabilityMatrices to be checked
     * @return true if there is enough space to assign all courseSessions, false if not
     */
    protected boolean checkAvailableTime(List<CourseSession> courseSessions, List<AvailabilityMatrix> availabilityMatrices) {
        int totalTimeNeeded = 0;
        int totalTimeAvailable = 0;
        int totalPreferredTimeAvailable = 0;

        for(CourseSession courseSession : courseSessions){
            totalTimeNeeded += courseSession.getDuration();
        }
        for(AvailabilityMatrix availabilityMatrix : availabilityMatrices){
            totalTimeAvailable += (int) availabilityMatrix.getTotalAvailableTime();
            totalPreferredTimeAvailable += (int) availabilityMatrix.getTotalAvailablePreferredTime();
        }

        if(totalTimeNeeded > totalTimeAvailable){
            return false;
        }

        if(totalTimeNeeded > totalPreferredTimeAvailable){
            log.warn("There is not enough space reserved for COMPUTER_SCIENCE. " +
                            "{} more minutes will be used from other free space",
                    totalTimeNeeded - totalPreferredTimeAvailable);
        }
        return true;
    }

    /**
     * Checks if all constraints are fulfilled to assign a courseSession to a certain candidate.
     * @param courseSession to be checked
     * @param candidate where the courseSession might be assigned
     * @return true if all checks are successful, false if at least one was not
     */
    protected boolean checkConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        if(!checkRoomCapacity(courseSession, candidate.getAvailabilityMatrix())){
            log.debug("room capacity of candidate is not fitting courseSession");
            return false;
        }
        if(!checkTimingConstraintsFulfilled(courseSession, candidate)){
            log.debug("timing constraints are intersecting with candidate");
            return false;
        }
        if(!checkCoursesOfSameSemester(courseSession, candidate)){
            log.debug("other course of same semester intersecting");
            return false;
        }
        return true;
    }

    /**
     * Checks if the room capacity is greater or equals the courseSession's number of participants. It also checks that
     * the room capacity is not too large, as we don't want to assign a courseSession with e.g. 25 participants to a
     * room with a capacity of 300 people.
     *
     * @param courseSession to be checked
     * @param availabilityMatrix of the room to be checked
     * @return true if the check was successful, false if not
     */
    protected boolean checkRoomCapacity(CourseSession courseSession, AvailabilityMatrix availabilityMatrix){
        return availabilityMatrix.getCapacity() >= courseSession.getNumberOfParticipants()
                && availabilityMatrix.getCapacity() / 2 <= courseSession.getNumberOfParticipants();
    }

    /**
     * Checks if the candidate's timing of assignment intersects with one of the courseSession's timing constraints.
     * @param courseSession to be checked
     * @param candidate to be checked
     * @return true if there is no intersection, false if there is.
     */
    protected boolean checkTimingConstraintsFulfilled(CourseSession courseSession, Candidate candidate){
        Timing timing = AvailabilityMatrix.toTiming(candidate);
        List<Timing> timingConstraints = courseSession.getTimingConstraints();
        if(timingConstraints != null){
            for(Timing timingConstraint : courseSession.getTimingConstraints()){
                if(timing.intersects(timingConstraint)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the candidate's timing intersects with the timing of any other courseSession already assigned that is
     * from the same semester.
     *
     * @param courseSession to be checked
     * @param candidate to be checked
     * @return true if there is no intersection with any other courseSession of the same semester, false if there is
     * at least one intersection
     */
    protected boolean checkCoursesOfSameSemester(CourseSession courseSession, Candidate candidate){
        for(AvailabilityMatrix availabilityMatrix : allAvailabilityMatrices){
            if(availabilityMatrix.semesterIntersects(candidate, courseSession)){
                return false;
            }
        }
        return true;
    }

    protected boolean readyToFinalize(){
        return readyForAssignmentSet.size() == numberOfCourseSessions;
    }

    protected void resetReadyForAssignmentSet(){
        for(CourseSession courseSession : readyForAssignmentSet.keySet()){
            courseSession.setRoomTable(null);
            Candidate candidate = readyForAssignmentSet.get(courseSession);
            candidate.getAvailabilityMatrix().clearCandidate(candidate);
        }
        readyForAssignmentSet.clear();
    }

    /**
     * Finalizes the assignment by creating all timings and assigning them to the courseSessions.
     */
    protected void finalizeAssignment() {
        Set<CourseSession> courseSessionsToAssign = readyForAssignmentSet.keySet();
        for(CourseSession courseSession : readyForAssignmentSet.keySet()){
            Timing timing = AvailabilityMatrix.toTiming(readyForAssignmentSet.get(courseSession));
            timing = timingService.createTiming(timing);
            courseSession.setTiming(timing);
            courseSession.setAssigned(true);
        }
        courseSessionService.saveAll(courseSessionsToAssign);
        readyForAssignmentSet.clear();
    }



    /**
     * Checks already assigned courseSessions for collisions
     * @param timeTable to be checked
     * @return a list of courseSessions that are in collision
     */
    /* TODO: fix
    public List<CourseSession> collisionCheck(TimeTable timeTable){
        if(!this.timeTable.equals(timeTable)){
            setTimeTable(timeTable);
        }
        List<CourseSession> collisionCandidates = timeTable.getAssignedCourseSessions();
        List<CourseSession> collisions = new ArrayList<>();
        for(CourseSession courseSession : collisionCandidates){
            if(!checkCoursesOfSameSemester(courseSession, AvailabilityMatrix.toCandidate(courseSession))){
                collisions.add(courseSession);
            }
        }
        return collisions;
    }

     */
}
