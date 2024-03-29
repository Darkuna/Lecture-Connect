package com.lecture.coordinator.model;

import com.lecture.coordinator.model.enums.CourseType;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class Course implements Persistable<String>, Serializable {
    @Id
    private String id;
    private CourseType courseType;
    private String name;
    private String lecturer;
    private int semester;
    private int duration;
    private int numberOfParticipants;
    private boolean computersNecessary;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseSession> courseSessions;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<Timing> timingConstraints;
    @Transient
    private int numberOfGroups;
    @Transient
    private boolean isSplit;
    @Transient
    private List<Integer> splitTimes;

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

    public boolean isComputersNecessary() {
        return computersNecessary;
    }

    public void setComputersNecessary(boolean computersNecessary) {
        this.computersNecessary = computersNecessary;
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

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType type) {
        this.courseType = type;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public void setNumberOfGroups(int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public void setSplit(boolean split) {
        isSplit = split;
    }

    public List<Integer> getSplitTimes() {
        return splitTimes;
    }

    public void setSplitTimes(List<Integer> splitTimes) {
        this.splitTimes = splitTimes;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id != null && id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
