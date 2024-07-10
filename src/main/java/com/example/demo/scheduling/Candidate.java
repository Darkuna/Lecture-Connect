package com.example.demo.scheduling;

import com.example.demo.models.AvailabilityMatrix;
import com.example.demo.models.Pair;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Candidate {
    private AvailabilityMatrix availabilityMatrix;
    private Pair position;

    public Candidate(AvailabilityMatrix availabilityMatrix, Pair position) {
        this.availabilityMatrix = availabilityMatrix;
        this.position = position;
    }

    public String toString(){
        return String.format("Candidate %s, Day: %d, Slot: %d", availabilityMatrix.getRoomTable().getRoom().getId(), position.getDay(), position.getSlot());
    }
}
