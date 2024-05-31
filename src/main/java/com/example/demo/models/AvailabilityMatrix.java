package com.example.demo.models;

import com.example.demo.constants.TimingConstants;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

@Getter
public class AvailabilityMatrix {
    private static final int DAYS_IN_WEEK = 5;
    private static final LocalTime START_TIME = TimingConstants.START_TIME;
    private static final LocalTime END_TIME = TimingConstants.END_TIME;
    private static final int SLOTS_PER_DAY = (int) Duration.between(START_TIME, END_TIME).toMinutes() / 60 * 4;

    private long total_available_time = 5 * Duration.between(START_TIME, END_TIME).toMinutes();
    private int capacity;
    private boolean computersAvailable;

    private final CourseSession[][] matrix;

    public AvailabilityMatrix(RoomTable roomTable) {
        this.matrix = new CourseSession[DAYS_IN_WEEK][SLOTS_PER_DAY];
        this.capacity = roomTable.getRoom().getCapacity();
        this.computersAvailable = roomTable.getRoom().isComputersAvailable();

        // mark all time slots with timingConstraints as BLOCKED
        if(roomTable.getTimingConstraints() != null){
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

    private int timeToSlotIndex(LocalTime time) {
        time = time.minusHours(START_TIME.getHour());
        time = time.minusMinutes(START_TIME.getMinute());
        return (time.getHour() * 60 + time.getMinute()) / 15;
    }

    public boolean isSlotAvailable(int day, int slot) {
        return matrix[day][slot] == null;
    }

    public boolean addCourseSession(int day, int startSlot, int duration, CourseSession courseSession) {
        for (int i = startSlot; i < startSlot + duration; i++) {
            if (!isSlotAvailable(day, i)) {
                return false;
            }
        }
        for (int i = startSlot; i < startSlot + duration; i++) {
            matrix[day][i] = courseSession;
        }
        return true;
    }

    public void removeCourseSession(int day, int startSlot, int duration) {
        for (int i = startSlot; i < startSlot + duration; i++) {
            matrix[day][i] = null;
        }
    }

    public Optional<CourseSession> getCourseSession(int day, int slot) {
        return Optional.ofNullable(matrix[day][slot]);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < SLOTS_PER_DAY; i++){
            for(int j = 0; j < DAYS_IN_WEEK; j++){
                char mark = (matrix[j][i] != null && matrix[j][i].equals(CourseSession.BLOCKED)) ? 'X' : ' ';
                sb.append(String.format("| %c ", mark));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}


