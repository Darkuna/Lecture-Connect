package com.example.demo.models;

import com.example.demo.constants.TimingConstants;
import com.example.demo.exceptions.roomTable.NoSpaceAvailableException;
import com.example.demo.models.enums.Day;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

@Getter
public class AvailabilityMatrix {
    private static final int DURATION_PER_SLOT = 15;
    private static final int DAYS_IN_WEEK = 5;
    private static final LocalTime START_TIME = TimingConstants.START_TIME;
    private static final LocalTime END_TIME = TimingConstants.END_TIME;
    private static final int SLOTS_PER_DAY = (int) Duration.between(START_TIME, END_TIME).toMinutes() / 60 * 4;

    private long total_available_time = 5 * Duration.between(START_TIME, END_TIME).toMinutes();
    private final int capacity;
    private final boolean computersAvailable;
    private final RoomTable roomTable;

    private final CourseSession[][] matrix;

    public AvailabilityMatrix(RoomTable roomTable) {
        this.matrix = new CourseSession[DAYS_IN_WEEK][SLOTS_PER_DAY];
        this.capacity = roomTable.getRoom().getCapacity();
        this.computersAvailable = roomTable.getRoom().isComputersAvailable();
        this.roomTable = roomTable;

        // mark all time slots with timingConstraints as BLOCKED
        if (roomTable.getTimingConstraints() != null) {
            for (Timing timing : roomTable.getTimingConstraints()) {
                int dayIndex = timing.getDay().ordinal();
                int startSlot = timeToSlotIndex(timing.getStartTime());
                int endSlot = timeToSlotIndex(timing.getEndTime());

                for (int slot = startSlot; slot < endSlot; slot++) {
                    matrix[dayIndex][slot] = CourseSession.BLOCKED;
                }
                total_available_time -= timing.getDuration();
            }
        }
    }

    public Pair getEarliestAvailableSlotForDuration(int minutes) {
        int numberOfSlots = minutes / DURATION_PER_SLOT;
        for (int i = 0; i < SLOTS_PER_DAY; i++) {
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                if (matrix[j][i] == null) {
                    for (int k = 0; k < numberOfSlots; k++) {
                        if (matrix[j][k] != null) {
                            break;
                        }
                    }
                    return new Pair(j, i);
                }
            }
        }
        throw new NoSpaceAvailableException();
    }

    public Pair getNextAvailableSlotForDurationAfterSlot(int minutes, Pair slot) {
        int numberOfSlots = minutes / DURATION_PER_SLOT;
        for (int i = slot.getDay(); i < SLOTS_PER_DAY; i++) {
            for (int j = slot.getSlot() + numberOfSlots; j < DAYS_IN_WEEK; j++) {
                if (matrix[j][i] == null) {
                    for (int k = 0; k < numberOfSlots; k++) {
                        if (matrix[j][k] != null) {
                            break;
                        }
                    }
                    return new Pair(j, i);
                }
            }
        }
        throw new NoSpaceAvailableException();
    }

    public Pair getRandomAvailableSlot(int minutes) {
        Random random = new Random();
        int numberOfSlots = minutes / DURATION_PER_SLOT;
        int dayIndex = 0;
        int slotIndex = 0;
        boolean randomSlotFound = false;

        while (!randomSlotFound) {
            dayIndex = random.nextInt(DAYS_IN_WEEK);
            slotIndex = random.nextInt(SLOTS_PER_DAY - numberOfSlots - 8);
            if (matrix[dayIndex][slotIndex] == null) {
                randomSlotFound = isSlotsAvailable(dayIndex, slotIndex, numberOfSlots);
            }
        }
        return new Pair(dayIndex, slotIndex);
    }

    private boolean isSlotsAvailable(int dayIndex, int slotIndex, int numberOfSlots) {
        for (int i = slotIndex; i < slotIndex + numberOfSlots; i++) {
            if (matrix[dayIndex][i] != null) {
                return false;
            }
        }
        return true;
    }

    public boolean semesterIntersects(Pair position, int duration, CourseSession courseSession) {
        for (int i = position.getSlot(); i < position.getSlot() + duration / DURATION_PER_SLOT; i++) {
            if (i >= SLOTS_PER_DAY) {
                return true;
            }
            if (matrix[position.getDay()][i] != null &&
                    matrix[position.getDay()][i].getSemester() == courseSession.getSemester() &&
                    !matrix[position.getDay()][i].isSamePS(courseSession)) {
                return true;
            }
        }
        return false;
    }

    private int timeToSlotIndex(LocalTime time) {
        time = time.minusHours(START_TIME.getHour());
        time = time.minusMinutes(START_TIME.getMinute());
        return (time.getHour() * 60 + time.getMinute()) / 15;
    }

    public Timing assignCourseSession(Pair position, int duration, CourseSession courseSession) {
        for (int i = position.getSlot(); i < position.getSlot() + duration / DURATION_PER_SLOT; i++) {
            matrix[position.getDay()][i] = courseSession;
        }
        total_available_time -= duration;
        System.out.println(this.toString());
        return toTiming(position, duration);
    }

    public static Timing toTiming(Pair position, int duration) {
        Timing timing = new Timing();
        int minutes = position.getSlot() % 4 * DURATION_PER_SLOT;
        int hours = position.getSlot() / DURATION_PER_SLOT;
        switch (position.getDay()) {
            case 0:
                timing.setDay(Day.MONDAY);
                break;
            case 1:
                timing.setDay(Day.TUESDAY);
                break;
            case 2:
                timing.setDay(Day.WEDNESDAY);
                break;
            case 3:
                timing.setDay(Day.THURSDAY);
                break;
            case 4:
                timing.setDay(Day.FRIDAY);
                break;
        }
        LocalTime startTime = START_TIME.plusHours(hours).plusMinutes(minutes);
        timing.setStartTime(startTime);
        timing.setEndTime(startTime.plusMinutes(duration));
        return timing;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int time = START_TIME.getHour();
        String mark;
        sb.append(this.getRoomTable().getRoom().getId());
        sb.append("\n");
        sb.append(String.format("      | %20.20s | %20.20s | %20.20s | %20.20s | %20.20s |\n", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"));
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
                } else {
                    mark = matrix[j][i].getName();
                }
                sb.append(String.format("| %20.20s ", mark));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public void addTimingConstraint(Timing timing) {
        int startSlot = timeToSlotIndex(timing.getStartTime());
        int endSlot = timeToSlotIndex(timing.getEndTime());
        int day = timing.getDay().ordinal();

        for(int i = startSlot; i < endSlot; i++) {
            matrix[day][i] = CourseSession.BLOCKED;
        }
    }
}


