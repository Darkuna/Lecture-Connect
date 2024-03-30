package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class CourseSession implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Transient
    private List<Timing> timingConstraints;
    private boolean isAssigned;
    private boolean isFixed;
    private int duration;
    @OneToOne(fetch = FetchType.EAGER)
    private Timing timing;
    @ManyToOne
    private Course course;
    @ManyToOne
    private TimeTable timeTable;
    @ManyToOne
    private RoomTable roomTable;

    //Static object BLOCKED for availability matrix
    public static final CourseSession BLOCKED = new CourseSession();

    public Timing getTiming() {
        return timing;
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }

    public List<Timing> getTimingConstraints() {
        return timingConstraints;
    }

    public void setTimingConstraints(List<Timing> timingConstraints) {
        this.timingConstraints = timingConstraints;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public RoomTable getRoomTable() {
        return roomTable;
    }

    public void setRoomTable(RoomTable roomTable) {
        this.roomTable = roomTable;
    }

    public boolean isBlocked() {
        return this == BLOCKED;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseSession courseSession = (CourseSession) o;
        return id != null && id.equals(courseSession.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
