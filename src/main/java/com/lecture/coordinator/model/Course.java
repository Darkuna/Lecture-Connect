package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Course implements Persistable<String>, Serializable{
    @Id
    private String id;
    private String name;
    private String lecturer;
    private int numberOfParticipants;
    private int numberOfGroups;
    private boolean computersNecessary;
    private boolean hasFixedTiming;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<CourseSession> courseSessions;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public void setNumberOfGroups(int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    public boolean isComputersNecessary() {
        return computersNecessary;
    }

    public void setComputersNecessary(boolean computersNecessary) {
        this.computersNecessary = computersNecessary;
    }

    public boolean isHasFixedTiming() {
        return hasFixedTiming;
    }

    public void setHasFixedTiming(boolean hasFixedTiming) {
        this.hasFixedTiming = hasFixedTiming;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
