package com.example.demo.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class RoomTable implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String roomId;
    private int capacity;
    private boolean computersAvailable;
    @OneToMany(mappedBy="roomTable", fetch = FetchType.LAZY)
    private List<CourseSession> assignedCourseSessions;
    @OneToMany(mappedBy="roomTable", fetch = FetchType.LAZY)
    private List<Timing> timingConstraints;
    @ManyToOne
    private TimeTable timeTable;

    public RoomTable() {
        timingConstraints = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomTable roomTable = (RoomTable) o;
        return id.equals(roomTable.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
