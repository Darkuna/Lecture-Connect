package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Room implements Persistable<String>, Serializable, Comparable<Room> {
    @Id
    private String id;
    private int capacity;
    private boolean computersAvailable;


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

    public int compareTo(Room room){
        assert room.getId() != null;
        return this.id.compareTo(room.getId());
    }
}
