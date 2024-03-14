package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
public class Course implements Persistable<String>, Serializable{
    @Id
    private String id;
    private String name;
    private String lecturer;
    private int duration;
    private int numberOfParticipants;
    private int numberOfGroups;
    private boolean isSplit;
    @OneToOne
    private IntegerTuple splitTimes;
    private boolean computersNecessary;
    private boolean isTimingFixed;

    @OneToOne
    private TimingTuple fixedTimings;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<CourseSession> courseSessions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<Timing> timingConstraints;
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


    @Override
    public boolean isNew() {
        return true;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public void setSplit(boolean split) {
        isSplit = split;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Timing> getTimingConstraints() {
        return timingConstraints;
    }

    public void setTimingConstraints(List<Timing> timingConstraints) {
        this.timingConstraints = timingConstraints;
    }

    public List<CourseSession> getCourseSessions() {
        return courseSessions;
    }

    public void setCourseSessions(List<CourseSession> courseSessions) {
        this.courseSessions = courseSessions;
    }

    public boolean isTimingFixed() {
        return isTimingFixed;
    }

    public void setTimingFixed(boolean timingFixed) {
        isTimingFixed = timingFixed;
    }

    public TimingTuple getFixedTimings() {
        return fixedTimings;
    }
    public void setFixedTimings(TimingTuple fixedTimings) {
        this.fixedTimings = fixedTimings;
    }

    public IntegerTuple getSplitTimes() {
        return splitTimes;
    }

    public void setSplitTimes(IntegerTuple splitTimes) {
        this.splitTimes = splitTimes;
    }
}
