package com.example.demo.models;

import com.example.demo.models.enums.StudyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class CourseSession implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String lecturer;
    private StudyType studyType;
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

    public boolean isFromSameCourse(CourseSession courseSession) {
        return courseSession.courseId.equals(this.courseId);
    }

    public boolean isGroupCourse(){
        return name.contains("Group");
    }

    public boolean isSplitCourse(){
        return name.contains("Split");
    }

    public boolean isAllowedToIntersectWith(CourseSession courseSession) {
        return this.semester != courseSession.semester || this.studyType != courseSession.studyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseSession that = (CourseSession) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isAssignedToSameRoomAndTime(CourseSession courseSession){
        return courseSession.getRoomTable().equals(this.roomTable) &&
                courseSession.getTiming().hasSameDayAndTime(this.timing);
    }

    @Override
    public String toString(){
        return String.format("%s: %s %s %s %s", name, studyType, roomTable.getRoomId(), timing, courseId);
    }
}
