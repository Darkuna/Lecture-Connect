package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {
    private String id;
    private int capacity;
    private boolean computersAvailable;
}
