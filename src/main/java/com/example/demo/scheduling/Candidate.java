package com.example.demo.scheduling;

import com.example.demo.models.AvailabilityMatrix;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Candidate {
    private AvailabilityMatrix availabilityMatrix;
    private Pair position;
    private int duration;

    public Candidate(AvailabilityMatrix availabilityMatrix, Pair position, int duration) {
        this.availabilityMatrix = availabilityMatrix;
        this.position = position;
        this.duration = duration;
    }

    public String toString(){
        return String.format("Candidate %s, Day: %d, Slot: %d", availabilityMatrix.getRoomTable().getRoom().getId(), position.getDay(), position.getSlot());
    }
}
