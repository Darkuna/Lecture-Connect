package com.example.demo.models;

import com.example.demo.models.enums.CourseType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

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

    // This toString Method was used for test data creation.
    // To use it again, uncomment the method and add the following variables to the class attributes:

    //    private static int id_counter = ID_OF_THE_NEXT_COURSE_SESSION;
    //    private static int timing_counter = ID_OF_THE_NEXT_TIMING;

    // Use the createTestData() Method in TimeTableServiceTest.java to create the data

    public String toString(){
        Random random = new Random();
        int upper_bound;
        int lower_bound;
        if(course.getCourseType().equals(CourseType.VO)){
            lower_bound = -41;
            upper_bound = -34;
        }
        else{
            if(course.isComputersNecessary()){
                lower_bound = -50;
                upper_bound = -41;
            }
            else{
                lower_bound = -62;
                upper_bound = -50;
            }
        }
        String string = String.format("INSERT INTO COURSE_SESSION(ID, DURATION, IS_ASSIGNED, IS_FIXED, COURSE_ID, ROOM_TABLE_ID, TIME_TABLE_ID, TIMING_ID) VALUES (%d, %d, TRUE, FALSE, '%s', %d, -3, %d)",id_counter, duration, course.getId(), random.nextInt(lower_bound, upper_bound), timing_counter);
        id_counter--;
        timing_counter--;
        return string;
    }
     */
}
