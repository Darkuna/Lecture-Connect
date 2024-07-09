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
}
