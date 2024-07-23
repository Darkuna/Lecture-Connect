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
@EqualsAndHashCode
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

    @Transient
    private AvailabilityMatrix availabilityMatrix;

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

    public void addTimingConstraint(Timing timing) {
        availabilityMatrix.addTimingConstraint(timing);
        timingConstraints.add(timing);
    }
}
