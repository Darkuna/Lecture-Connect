package com.example.demo.scheduling;

import com.example.demo.models.AvailabilityMatrix;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Candidate {
    private AvailabilityMatrix availabilityMatrix;
    private int day;
    private int slot;
    private int duration;
    private boolean preferredSlots;

    public Candidate(AvailabilityMatrix availabilityMatrix, int day, int slot, int duration, boolean preferredSlots) {
        this.availabilityMatrix = availabilityMatrix;
        this.day = day;
        this.slot = slot;
        this.duration = duration;
        this.preferredSlots = preferredSlots;
    }

    public boolean intersects(Candidate candidate) {
        return AvailabilityMatrix.toTiming(this).intersects(AvailabilityMatrix.toTiming(candidate));
    }

    public String toString(){
        return String.format("Candidate %s, Day: %d, Slot: %d", availabilityMatrix.getRoomTable().getRoomId(), day, slot);
    }
}
