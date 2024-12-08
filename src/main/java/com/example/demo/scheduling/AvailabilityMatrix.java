package com.example.demo.scheduling;

import com.example.demo.constants.TimingConstants;
import com.example.demo.models.CourseSession;
import com.example.demo.models.RoomTable;
import com.example.demo.models.Timing;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.TimingType;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents availability and allocation of time slots
 * for course sessions in a room over a working week. The AvailabilityMatrix supports
 * assigning, clearing, and checking availability of time slots, as well as handling
 * timing constraints and preferences for course sessions.
 *
 * The matrix tracks the availability of each time slot for specific days, allowing
 * course sessions to be scheduled based on constraints like blocked times and preferred
 * times. It also provides utility methods to calculate available candidates for scheduling
 * and to convert between various scheduling models.
 */
@Getter
public class AvailabilityMatrix {
    private static final int DURATION_PER_SLOT = 15;
    private static final int DAYS_IN_WEEK = 5;
    private static final LocalTime START_TIME = TimingConstants.START_TIME;
    private static final LocalTime END_TIME = TimingConstants.END_TIME;
    private static final int SLOTS_PER_DAY = (int) Duration.between(START_TIME, END_TIME).toMinutes() / 60 * 4;

    private long totalAvailableTime = 5 * Duration.between(START_TIME, END_TIME).toMinutes();
    private long totalAvailablePreferredTime = 0;
    private final int capacity;
    private final boolean computersAvailable;
    private final RoomTable roomTable;

    private final CourseSession[][] matrix;

    public AvailabilityMatrix(RoomTable roomTable) {
        this.matrix = new CourseSession[DAYS_IN_WEEK][SLOTS_PER_DAY];
        this.capacity = roomTable.getCapacity();
        this.computersAvailable = roomTable.isComputersAvailable();
        this.roomTable = roomTable;

        // initialize timingConstraints
        if (roomTable.getTimingConstraints() != null) {
            initializeTimingConstraints();
        }
        // initialize assigned courseSessions
        if(roomTable.getAssignedCourseSessions() != null) {
            initializeAssignedCourseSessions();
        }
    }

    /**
     * Constructor for testing purposes
     */
    public AvailabilityMatrix(List<Timing> timingConstraints, List<CourseSession> courseSessions) {
        this.matrix = new CourseSession[DAYS_IN_WEEK][SLOTS_PER_DAY];
        this.capacity = 0;
        this.computersAvailable = true;
        this.roomTable = new RoomTable();
        roomTable.setTimingConstraints(timingConstraints);
        roomTable.setAssignedCourseSessions(courseSessions);

        // initialize timingConstraints
        if (timingConstraints != null) {
            initializeTimingConstraints();
        }
        // initialize assigned courseSessions
        if(courseSessions != null) {
            initializeAssignedCourseSessions();
        }
    }

    /**
     * This method initializes slots with timing constraints BLOCKED and COMPUTER_SCIENCE
     */
    private void initializeTimingConstraints() {
        int dayIndex;
        int startSlot;
        int endSlot;

        for (Timing timing : roomTable.getTimingConstraints()) {
            dayIndex = timing.getDay().ordinal();
            startSlot = timeToSlotIndex(timing.getStartTime());
            endSlot = timeToSlotIndex(timing.getEndTime());
            if(endSlot >= timeToSlotIndex(END_TIME)) {
                endSlot --;
            }

            if(timing.getTimingType().equals(TimingType.BLOCKED)) {
                for (int slot = startSlot; slot < endSlot; slot++) {
                    matrix[dayIndex][slot] = CourseSession.BLOCKED;
                }
                totalAvailableTime -= timing.getDuration();
            }
            else if(timing.getTimingType().equals(TimingType.COMPUTER_SCIENCE)){
                for (int slot = startSlot; slot < endSlot; slot++) {
                    matrix[dayIndex][slot] = CourseSession.PREFERRED;
                }
                totalAvailablePreferredTime += timing.getDuration();
            }
        }
    }

    /**
     * This method initializes slots that are already assigned
     */
    private void initializeAssignedCourseSessions() {
        int dayIndex;
        int startSlot;
        int endSlot;
        Timing timing;

        for (CourseSession courseSession : roomTable.getAssignedCourseSessions()){
            timing = courseSession.getTiming();
            dayIndex = timing.getDay().ordinal();
            startSlot = timeToSlotIndex(timing.getStartTime());
            endSlot = timeToSlotIndex(timing.getEndTime());

            for (int slot = startSlot; slot < endSlot; slot++) {
                if(matrix[dayIndex][slot] == CourseSession.PREFERRED){
                    totalAvailablePreferredTime -= DURATION_PER_SLOT;
                }
                matrix[dayIndex][slot] = courseSession;
            }
            totalAvailableTime -= timing.getDuration();
        }
    }

    /**
     * This method checks if specific slots are available in the matrix
     * @param dayIndex index of the weekday starting at 0 for monday until 4 for friday
     * @param slotIndex index of the slot starting at 0
     * @param numberOfSlots number of slots to be checked starting at slotIndex

     * @return ratio of preferred slots, if the slots are available, -1.0f if not
     */
    private float isSlotsAvailable(int dayIndex, int slotIndex, int numberOfSlots) {
        int preferredSlotsCounter = 0;
        if(slotIndex + numberOfSlots >= SLOTS_PER_DAY){
            return -1.0f;
        }
        for (int i = slotIndex; i < slotIndex + numberOfSlots; i++) {
            if (matrix[dayIndex][i] != null && matrix[dayIndex][i] != CourseSession.PREFERRED) {
                return -1.0f;
            }
            if(matrix[dayIndex][i] == CourseSession.PREFERRED){
                preferredSlotsCounter++;
            }
        }

        return ((float) preferredSlotsCounter) / numberOfSlots;
    }

    /**
     * This method checks if a specific assignment candidate intersects with one of the already assigned courses of the
     * same degree and semester
     * @param candidate to be checked
     * @param courseSession to be assigned to that candidate
     * @return true, if there is an intersection, false if not
     */
    public boolean semesterIntersects(Candidate candidate, CourseSession courseSession) {
        //guarantees that collision check works as expected
        if(matrix[candidate.getDay()][candidate.getSlot()] != null &&
                matrix[candidate.getDay()][candidate.getSlot()] != CourseSession.BLOCKED &&
                matrix[candidate.getDay()][candidate.getSlot()] != CourseSession.PREFERRED &&
                matrix[candidate.getDay()][candidate.getSlot()].equals(courseSession)){
            return false;
        }
        for (int i = candidate.getSlot(); i <= candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
            if (matrix[candidate.getDay()][i] != null &&
                    matrix[candidate.getDay()][i].getSemester() == courseSession.getSemester() &&
                    matrix[candidate.getDay()][i].getStudyType().equals(courseSession.getStudyType()) &&
                    !matrix[candidate.getDay()][i].isFromSameCourse(courseSession)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method assigns a new courseSession in the availability matrix using the spacial information saved in the
     * corresponding candidate
     * @param candidate with the assignment information
     * @param courseSession to be assigned
     */
    public void assignCourseSession(Candidate candidate, CourseSession courseSession) {
        for (int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
            if(matrix[candidate.getDay()][i] != null && matrix[candidate.getDay()][i] != CourseSession.PREFERRED){
                throw new RuntimeException("Slot already assigned");
            }
            matrix[candidate.getDay()][i] = courseSession;
        }
        totalAvailableTime -= candidate.getDuration();
        if(candidate.getPreferredRatio() >= 0){
            totalAvailablePreferredTime -= (long) ((long)candidate.getDuration() * candidate.getPreferredRatio());
        }
    }

    /**
     * This method removes the assignment of a specific courseSession using the information of its candidate.
     * @param candidate with the information about the assignment of the courseSession
     */
    public void clearCandidate(Candidate candidate) {
        Timing candidateTiming = toTiming(candidate);
        List<Timing> intersectingTimings;
        List<Integer> slotIndexes = new ArrayList<>();
        if(candidate.getPreferredRatio() > 0.0f){
            intersectingTimings = roomTable.getTimingConstraints().stream()
                    .filter(t -> t.intersects(candidateTiming)).toList();
            for(Timing timing : intersectingTimings){
                slotIndexes.addAll(getSlotIndexesOfTiming(timing));
            }
            for(int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
                if(slotIndexes.contains(i)){
                    matrix[candidate.getDay()][i] = CourseSession.PREFERRED;
                    totalAvailablePreferredTime += 15;
                }
                else{
                    matrix[candidate.getDay()][i] = null;
                }

            }
        }
        else{
            for(int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
                matrix[candidate.getDay()][i] = null;
            }
        }

        totalAvailableTime += candidate.getDuration();
    }

    private List<Integer> getSlotIndexesOfTiming(Timing timing) {
        List<Integer> slotIndexes = new ArrayList<>();
        for (int i = timeToSlotIndex(timing.getStartTime()); i <= timeToSlotIndex(timing.getEndTime()); i++) {
            slotIndexes.add(i);
        }
        return slotIndexes;
    }

    /**
     * This static method converts the spacial information of a candidate into a timing object
     * @param candidate with the timing information
     * @return a timing object
     */
    public static Timing toTiming(Candidate candidate) {
        Timing timing = new Timing();
        int minutes = candidate.getSlot() * DURATION_PER_SLOT;
        LocalTime startTime = START_TIME.plusMinutes(minutes);

        timing.setStartTime(startTime);
        timing.setEndTime(startTime.plusMinutes(candidate.getDuration()));
        timing.setDay(Day.values()[candidate.getDay()]);
        return timing;
    }

    /**
     * This method converts a specific time into the corresponding slot index in the matrix.
     * @param time to be converted into slot index
     * @return the corresponding slot index
     */
    public static int timeToSlotIndex(LocalTime time) {
        return (int) Duration.between(START_TIME, time).toMinutes() * 4 / 60;
    }

    /**
     * This method creates and returns a list of all assignment candidates available for a specific course session.
     * @param courseSession to search candidates for
     * @return a list of all available candidates
     */
    public List<Candidate> getAllAvailableCandidates(CourseSession courseSession) {
        int numberOfSlots = courseSession.getDuration() / DURATION_PER_SLOT;
        float preferredRatio;
        List<Candidate> possibleCandidates = new ArrayList<>();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            for (int j = 0; j < SLOTS_PER_DAY; j++) {
                if (matrix[i][j] == CourseSession.PREFERRED || matrix[i][j] == null) {
                    preferredRatio = isSlotsAvailable(i, j, numberOfSlots);
                    if (preferredRatio != -1.0f) {
                        possibleCandidates.add(new Candidate(this, i, j, courseSession.getDuration(), preferredRatio));
                    }
                }
            }
        }
        return possibleCandidates;
    }

}


