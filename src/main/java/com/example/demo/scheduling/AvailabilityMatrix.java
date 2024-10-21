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

            for (int slot = startSlot; slot < endSlot - 1; slot++) {
                if(matrix[dayIndex][slot] == CourseSession.PREFERRED){
                    totalAvailablePreferredTime -= DURATION_PER_SLOT;
                }
                matrix[dayIndex][slot] = courseSession;
            }
            totalAvailableTime -= timing.getDuration();
        }
    }

    private boolean isSlotsAvailable(int dayIndex, int slotIndex, int numberOfSlots, boolean preferredOnly) {
        if(slotIndex + numberOfSlots >= SLOTS_PER_DAY){
            return false;
        }
        if(preferredOnly){
            for (int i = slotIndex; i < slotIndex + numberOfSlots; i++) {
                if (matrix[dayIndex][i] != CourseSession.PREFERRED) {
                    return false;
                }
            }
        }
        else{
            for (int i = slotIndex; i < slotIndex + numberOfSlots; i++) {
                if (matrix[dayIndex][i] != null && matrix[dayIndex][i] != CourseSession.PREFERRED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean semesterIntersects(Candidate candidate, CourseSession courseSession) {
        //guarantees that collision check works as expected
        if(matrix[candidate.getDay()][candidate.getSlot()] != null &&
                matrix[candidate.getDay()][candidate.getSlot()] != CourseSession.BLOCKED &&
                matrix[candidate.getDay()][candidate.getSlot()] != CourseSession.PREFERRED &&
                matrix[candidate.getDay()][candidate.getSlot()].equals(courseSession)){
            return false;
        }
        for (int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
            if (matrix[candidate.getDay()][i] != null &&
                    matrix[candidate.getDay()][i].getSemester() == courseSession.getSemester() &&
                    matrix[candidate.getDay()][i].getStudyType().equals(courseSession.getStudyType()) &&
                    !matrix[candidate.getDay()][i].isFromSameCourse(courseSession)) {
                return true;
            }
        }
        return false;
    }

    public void assignCourseSession(Candidate candidate, CourseSession courseSession) {
        for (int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
            if(matrix[candidate.getDay()][i] != null && matrix[candidate.getDay()][i] != CourseSession.PREFERRED){
                throw new RuntimeException("Slot already assigned");
            }
            matrix[candidate.getDay()][i] = courseSession;
        }
        totalAvailableTime -= candidate.getDuration();
        if(candidate.isPreferredSlots()){
            totalAvailablePreferredTime -= candidate.getDuration();
        }
    }

    public void clearCandidate(Candidate candidate) {
        CourseSession courseSession = candidate.isPreferredSlots() ? CourseSession.PREFERRED : null;
        for(int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
            matrix[candidate.getDay()][i] = courseSession;
        }
        totalAvailableTime += candidate.getDuration();
        if(candidate.isPreferredSlots()){
            totalAvailablePreferredTime += candidate.getDuration();
        }
    }

    public void addTimingConstraint(Timing timing) {
        int startSlot = timeToSlotIndex(timing.getStartTime());
        int endSlot = timeToSlotIndex(timing.getEndTime());
        int day = timing.getDay().ordinal();

        for(int i = startSlot; i < endSlot; i++) {
            matrix[day][i] = CourseSession.BLOCKED;
        }
        totalAvailableTime -= timing.getDuration();
    }

    public static Timing toTiming(Candidate candidate) {
        Timing timing = new Timing();
        int minutes = candidate.getSlot() * DURATION_PER_SLOT;
        LocalTime startTime = START_TIME.plusMinutes(minutes);

        timing.setStartTime(startTime);
        timing.setEndTime(startTime.plusMinutes(candidate.getDuration()));
        timing.setDay(Day.values()[candidate.getDay()]);
        return timing;
    }

    public static Candidate toCandidate(CourseSession courseSession) {
        /* TODO: fix
        return new Candidate(courseSession.getRoomTable().getAvailabilityMatrix(),
                courseSession.getTiming().getDay().ordinal(),timeToSlotIndex(courseSession.getTiming().getStartTime()),
                (int) courseSession.getTiming().getDuration(), true);

         */
        return null;
    }

    public static int timeToSlotIndex(LocalTime time) {
        return (int) Duration.between(START_TIME, time).toMinutes() * 4 / 60;
    }

    public List<Candidate> getAllAvailableCandidates(CourseSession courseSession) {
        int numberOfSlots = courseSession.getDuration() / DURATION_PER_SLOT;
        List<Candidate> possibleCandidates = new ArrayList<>();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            for (int j = 0; j < SLOTS_PER_DAY; j++) {
                if (matrix[i][j] == CourseSession.PREFERRED || matrix[i][j] == null) {
                    if (isSlotsAvailable(i, j, numberOfSlots, true)) {
                        possibleCandidates.add(new Candidate(this, i, j, courseSession.getDuration(), true));
                    }
                    else if (isSlotsAvailable(i, j, numberOfSlots, false)) {
                        possibleCandidates.add(new Candidate(this, i, j, courseSession.getDuration(), false));
                    }
                }
            }
        }
        return possibleCandidates;
    }

    /*
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int time = START_TIME.getHour();
        String mark;
        sb.append(this.getRoomTable().getRoomId());
        sb.append(String.format("\nTotal time available: %d, Preferred time available: %d", totalAvailableTime, totalAvailablePreferredTime));
        sb.append("\n");
        sb.append(String.format("      | %20.20s | %20.20s | %20.20s | %20.20s | %20.20s |\n",
                "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"));
        for (int i = 0; i < SLOTS_PER_DAY; i++) {
            if (i % 4 == 0) {
                sb.append(String.format("%2d Uhr", time));
                time += 1;
            } else {
                sb.append("      ");
            }
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                if (matrix[j][i] == null) {
                    mark = " ";
                } else if (matrix[j][i] == CourseSession.BLOCKED) {
                    mark = "BLOCK";
                } else if (matrix[j][i] == CourseSession.PREFERRED) {
                    mark = "PREFERRED";
                } else {
                    mark = matrix[j][i].getName();
                }
                sb.append(String.format("| %20.20s ", mark));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
    */
}


