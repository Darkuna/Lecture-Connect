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

    @ManyToMany(mappedBy="rooms", fetch = FetchType.LAZY)
    private List<TimeTable> timeTables;

    // CONSTRUCTOR
    public Room() {
    }

    public Room(String id, int capacity, boolean computersAvailable) {
        this.id = id;
        this.capacity = capacity;
        this.computersAvailable = computersAvailable;
    }

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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimingConstraints(List<Timing> timingConstraints) {
        this.timingConstraints = timingConstraints;
    }

    public List<Timing> getTimingConstraints() {
        return timingConstraints;
    }

    //OTHER METHODS
    public boolean isNew() {
        return true;
    }
}
