package com.example.demo.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
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

    public static final CourseSession BLOCKED = new CourseSession();

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public boolean isNew() {
        return id == null;
    }
    public boolean isBlocked() {
        return this == BLOCKED;
    }


    /*
    This toString() method was used for test data creation
    public String toString(){
        String string = String.format("INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (%d, %d, FALSE, FALSE, '%s', NULL, -2, NULL)",counter, duration, course.getId());
        counter--;
        return string;
    }

    Also add the following attribute to the class to make it work:

    private static int counter = -11;
     */
}
