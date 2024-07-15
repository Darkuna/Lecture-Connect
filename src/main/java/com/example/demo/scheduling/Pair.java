package com.example.demo.scheduling;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair {
    private int day;
    private int slot;

    public Pair(int day, int slot) {
        this.day = day;
        this.slot = slot;
    }
    public Pair(){};
}
