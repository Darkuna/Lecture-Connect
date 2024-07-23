package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomTableDTO {
    private Long id;
    private String roomId;
    private int capacity;
    private boolean isComputersAvailable;
    private List<TimingDTO> timingConstraints;
}

