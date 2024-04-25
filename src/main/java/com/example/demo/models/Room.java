package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Setter
@Entity
public class Room implements Persistable<String>, Serializable {
    @Id
    private String id;
    private int capacity;
    private boolean computersAvailable;
    @Transient
    private List<Timing> timingConstraints;
    @OneToMany(mappedBy="room", fetch = FetchType.LAZY)
    private List<RoomTable> roomTables;

    // CONSTRUCTOR
    public Room() {
    }

    public Room(String id, int capacity, boolean computersAvailable) {
        this.id = id;
        this.capacity = capacity;
        this.computersAvailable = computersAvailable;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isComputersAvailable() {
        return computersAvailable;
    }

    @Override
    public String getId() {
        return id;
    }

    public List<Timing> getTimingConstraints() {
        return timingConstraints;
    }

    public List<RoomTable> getRoomTables() {
        return roomTables;
    }

    //OTHER METHODS
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id != null && id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
