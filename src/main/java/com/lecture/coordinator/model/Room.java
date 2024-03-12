package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Room implements Persistable<String>, Serializable {
    @Id
    private String id;
    private int capacity;
    private boolean computersAvailable;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private List<Timing> timingConstraints;

    // CONSTRUCTOR
    public Room(){}

    // GETTERS AND SETTERS
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public boolean isComputersAvailable() {
        return computersAvailable;
    }
    public void setComputersAvailable(boolean computersAvailable) {
        this.computersAvailable = computersAvailable;
    }

    @Override
    public String getId(){
        return id;
    }

    //OTHER METHODS
    public boolean isNew(){
        return true;
    }
}
