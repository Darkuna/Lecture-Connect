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
    private String name;
    private String lecturer;
    private int semester;
    private int numberOfParticipants;
    private boolean computersNecessary;
    @Transient
    private List<Timing> timingConstraints;
    private boolean isAssigned;
    private boolean isFixed;
    private int duration;
    @OneToOne(fetch = FetchType.EAGER)
    private Timing timing;
    private String courseId;
    @ManyToOne
    private TimeTable timeTable;
    @ManyToOne
    private RoomTable roomTable;

    public static final CourseSession BLOCKED = new CourseSession();
    public static final CourseSession PREFERRED = new CourseSession();

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

    public boolean isSamePS(CourseSession courseSession) {
        return courseSession.courseId.equals(this.courseId);
    }

    public boolean isGroupCourse(){
        return name.contains("Group");
    }

    public boolean isSplitCourse(){
        return name.contains("Split");
    }


    /*
    // This toString Method was used for test data creation.
    // To use it again, uncomment the method and add the following variable to the class attributes:

    //    private static int id_counter = ID_OF_THE_NEXT_COURSE_SESSION;

    // Use the createTestData() Method in TimeTableServiceTest.java to create the data

    public String toString(){
        String string = String.format("INSERT INTO COURSE_SESSION(ID, NAME, LECTURER, SEMESTER, NUMBER_OF_PARTICIPANTS, DURATION, COMPUTERS_NECESSARY, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (%d, '%s', '%s', %d, %d, %d, %b, %b, %b, '%s', NULL, -2, NULL)",
                id_counter, name, lecturer, semester, numberOfParticipants, duration, computersNecessary, isAssigned, isFixed, courseId);
        id_counter--;
        return string;
    }
    */
}
