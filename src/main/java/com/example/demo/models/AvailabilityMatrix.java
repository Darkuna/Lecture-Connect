package com.example.demo.models;

import com.example.demo.constants.TimingConstants;
import com.example.demo.exceptions.roomTable.NotEnoughSpaceAvailableException;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.TimingType;
import com.example.demo.scheduling.Candidate;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private void initializeTimingConstraints() {
        int dayIndex;
        int startSlot;
        int endSlot;

        for (Timing timing : roomTable.getTimingConstraints()) {
            dayIndex = timing.getDay().ordinal();
            startSlot = timeToSlotIndex(timing.getStartTime());
            endSlot = timeToSlotIndex(timing.getEndTime());

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

            for (int slot = startSlot; slot < endSlot; slot++) {
                if(matrix[dayIndex][slot] == CourseSession.PREFERRED){
                    totalAvailablePreferredTime -= DURATION_PER_SLOT;
                }
                matrix[dayIndex][slot] = courseSession;
            }
            totalAvailableTime -= timing.getDuration();
        }
    }

    public Candidate getEarliestAvailableSlotForDuration(int minutes, boolean preferredOnly) {
        int numberOfSlots = minutes / DURATION_PER_SLOT;
        if(preferredOnly){
            for (int i = 0; i < SLOTS_PER_DAY; i++) {
                for (int j = 0; j < DAYS_IN_WEEK; j++) {
                    if (matrix[j][i] == CourseSession.PREFERRED) {
                        if(isSlotsAvailable(j,i,numberOfSlots, true)) {
                            return new Candidate(this, j, i, minutes, true);
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < SLOTS_PER_DAY; i++) {
                for (int j = 0; j < DAYS_IN_WEEK; j++) {
                    if (matrix[j][i] == null || matrix[j][i] == CourseSession.PREFERRED) {
                        if(isSlotsAvailable(j,i,numberOfSlots, false)) {
                            return new Candidate(this, j, i, minutes, false);
                        }
                    }
                }
            }
        }
        throw new NotEnoughSpaceAvailableException("Not enough space in availability matrix");
    }

    public Candidate getRandomAvailableSlot(int minutes, boolean preferredOnly) {
        Random random = new Random();
        int numberOfSlots = minutes / DURATION_PER_SLOT;
        int numberOfTries = 0;
        int dayIndex = 0;
        int slotIndex = 0;
        boolean randomSlotFound = false;
        boolean preferredSlot;

        if(preferredOnly){
            while (!randomSlotFound) {
                dayIndex = random.nextInt(DAYS_IN_WEEK);
                slotIndex = random.nextInt(SLOTS_PER_DAY - numberOfSlots - 8);
                if (matrix[dayIndex][slotIndex] == CourseSession.PREFERRED) {
                    randomSlotFound = isSlotsAvailable(dayIndex, slotIndex, numberOfSlots, true);
                }
                numberOfTries++;
                if(numberOfTries >= 50) {
                    throw new NotEnoughSpaceAvailableException("No random candidate found");
                }
            }
            preferredSlot = true;
        }
        else{
            while (!randomSlotFound) {
                dayIndex = random.nextInt(DAYS_IN_WEEK);
                slotIndex = random.nextInt(SLOTS_PER_DAY - numberOfSlots - 8);
                if (matrix[dayIndex][slotIndex] == null || matrix[dayIndex][slotIndex] == CourseSession.PREFERRED) {
                    randomSlotFound = isSlotsAvailable(dayIndex, slotIndex, numberOfSlots, false);
                }
                numberOfTries++;
                if(numberOfTries >= 50) {
                    throw new NotEnoughSpaceAvailableException("No random candidate found");
                }
            }
            preferredSlot = false;
        }

        return new Candidate(this, dayIndex, slotIndex, minutes, preferredSlot);
    }

    public List<Candidate> getPossibleCandidatesOfDay(int dayOfAssignment, int duration, boolean preferredOnly) {
        List<Candidate> candidates = new ArrayList<>();
        boolean interrupted;
        int numberOfSlots = duration / DURATION_PER_SLOT;
        if(preferredOnly){
            for (int i = 0; i <= SLOTS_PER_DAY - numberOfSlots; i++) {
                if (matrix[dayOfAssignment][i] == CourseSession.PREFERRED) {
                    interrupted = false;
                    for (int j = i; j < i + numberOfSlots; j++) {
                        if (matrix[dayOfAssignment][j] != CourseSession.PREFERRED) {
                            i = j;
                            interrupted = true;
                            break;
                        }
                    }
                    if (!interrupted) {
                        candidates.add(new Candidate(this, dayOfAssignment, i, duration, true));
                        i += numberOfSlots - 1;
                    }
                }
            }
        }
        else{
            for (int i = 0; i <= SLOTS_PER_DAY - numberOfSlots; i++) {
                if (matrix[dayOfAssignment][i] == null || matrix[dayOfAssignment][i] == CourseSession.PREFERRED) {
                    interrupted = false;
                    for (int j = i; j < i + numberOfSlots; j++) {
                        if (matrix[dayOfAssignment][j] != null && matrix[dayOfAssignment][j] != CourseSession.PREFERRED) {
                            i = j;
                            interrupted = true;
                            break;
                        }
                    }
                    if (!interrupted) {

                        candidates.add(new Candidate(this, dayOfAssignment, i, duration, false));
                        i += numberOfSlots - 1;
                    }
                }
            }
        }
        return candidates;
    }

    private boolean isSlotsAvailable(int dayIndex, int slotIndex, int numberOfSlots, boolean preferredOnly) {
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
            if (i >= SLOTS_PER_DAY) {
                return true;
            }
            if (matrix[candidate.getDay()][i] != null &&
                    matrix[candidate.getDay()][i].getSemester() == courseSession.getSemester() &&
                    !matrix[candidate.getDay()][i].isSamePS(courseSession)) {
                return true;
            }
        }
        return false;
    }

    private static int timeToSlotIndex(LocalTime time) {
        time = time.minusHours(START_TIME.getHour());
        time = time.minusMinutes(START_TIME.getMinute());
        return (time.getHour() * 60 + time.getMinute()) / 15;
    }

    public void assignCourseSession(Candidate candidate, CourseSession courseSession) {
        for (int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
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
        return new Candidate(courseSession.getRoomTable().getAvailabilityMatrix(),
                courseSession.getTiming().getDay().ordinal(),timeToSlotIndex(courseSession.getTiming().getStartTime()),
                (int) courseSession.getTiming().getDuration(), true);
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
}


