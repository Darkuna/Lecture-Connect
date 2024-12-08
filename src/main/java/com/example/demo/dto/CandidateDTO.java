package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO {
    int day;
    int slot;
    int duration;
    float preferredRatio;
    TimingDTO timing;
    String roomTable;
}
