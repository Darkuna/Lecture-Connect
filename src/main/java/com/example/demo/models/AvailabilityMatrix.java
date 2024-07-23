package com.example.demo.models;

import com.example.demo.constants.TimingConstants;
import com.example.demo.exceptions.roomTable.NoEnoughSpaceAvailableException;
import com.example.demo.models.enums.Day;
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

    private long total_available_time = 5 * Duration.between(START_TIME, END_TIME).toMinutes();
    private final int capacity;
    private final boolean computersAvailable;
    private final RoomTable roomTable;

    private final CourseSession[][] matrix;

    public AvailabilityMatrix(RoomTable roomTable) {
        this.matrix = new CourseSession[DAYS_IN_WEEK][SLOTS_PER_DAY];
        this.capacity = roomTable.getCapacity();
        this.computersAvailable = roomTable.isComputersAvailable();
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

    public Candidate getEarliestAvailableSlotForDuration(int minutes) {
        int numberOfSlots = minutes / DURATION_PER_SLOT;
        for (int i = 0; i < SLOTS_PER_DAY; i++) {
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                if (matrix[j][i] == null) {
                    for (int k = 0; k < numberOfSlots; k++) {
                        if (matrix[j][k] != null) {
                            break;
                        }
                    }
                    return new Candidate(this, j, i, minutes);
                }
            }
        }
        throw new NoEnoughSpaceAvailableException("Not enough space in availability matrix");
    }

    public Candidate getRandomAvailableSlot(int minutes) {
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
        return new Candidate(this, dayIndex, slotIndex, minutes);
    }

    private boolean isSlotsAvailable(int dayIndex, int slotIndex, int numberOfSlots) {
        for (int i = slotIndex; i < slotIndex + numberOfSlots; i++) {
            if (matrix[dayIndex][i] != null) {
                return false;
            }
        }
        return true;
    }

    public boolean semesterIntersects(Candidate candidate, CourseSession courseSession) {
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

    private int timeToSlotIndex(LocalTime time) {
        time = time.minusHours(START_TIME.getHour());
        time = time.minusMinutes(START_TIME.getMinute());
        return (time.getHour() * 60 + time.getMinute()) / 15;
    }

    public Timing assignCourseSession(Candidate candidate, CourseSession courseSession) {
        for (int i = candidate.getSlot(); i < candidate.getSlot() + candidate.getDuration() / DURATION_PER_SLOT; i++) {
            matrix[candidate.getDay()][i] = courseSession;
        }
        total_available_time -= candidate.getDuration();
        return toTiming(candidate);
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

    public void addTimingConstraint(Timing timing) {
        int startSlot = timeToSlotIndex(timing.getStartTime());
        int endSlot = timeToSlotIndex(timing.getEndTime());
        int day = timing.getDay().ordinal();

        for(int i = startSlot; i < endSlot; i++) {
            matrix[day][i] = CourseSession.BLOCKED;
        }
        total_available_time -= timing.getDuration();
    }

    public List<Candidate> getPossibleCandidatesOfDay(int dayOfAssignment, int duration) {
        List<Candidate> candidates = new ArrayList<>();
        boolean interrupted;
        int numberOfSlots = duration / DURATION_PER_SLOT;

        for (int i = 0; i <= SLOTS_PER_DAY - numberOfSlots; i++) {
            if (matrix[dayOfAssignment][i] == null) {
                interrupted = false;
                for (int j = i; j < i + numberOfSlots; j++) {
                    if (matrix[dayOfAssignment][j] != null) {
                        i = j;
                        interrupted = true;
                        break;
                    }
                }
                if (!interrupted) {

                    candidates.add(new Candidate(this, dayOfAssignment, i, duration));
                    i += numberOfSlots - 1;
                }
            }
        }
        return candidates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int time = START_TIME.getHour();
        String mark;
        sb.append(this.getRoomTable().getRoomId());
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


