package com.example.demo.models;

import com.example.demo.models.enums.Semester;
import com.example.demo.models.base.TimestampedEntity;
import com.example.demo.models.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class TimeTable extends TimestampedEntity implements Persistable<Long>, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Semester semester;
    @Column(name = "academic_year")
    private int year;
    private Status status;
    @OneToMany(mappedBy = "timeTable", fetch= FetchType.LAZY)
    private List<RoomTable> roomTables;
    @OneToMany(mappedBy = "timeTable", fetch = FetchType.LAZY)
    private List<CourseSession> courseSessions;

    public TimeTable(){
        this.roomTables = new ArrayList<>();
        this.courseSessions = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public void addRoomTable(RoomTable roomTable){
        if(roomTable != null){
            roomTables.add(roomTable);
        }
    }

    public void addCourseSessions(List<CourseSession> courseSessions){
        if(courseSessions != null){
            this.courseSessions.addAll(courseSessions);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTable timeTable = (TimeTable) o;
        return id != null && id.equals(timeTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public List<CourseSession> getUnassignedCourseSessions(){
        List<CourseSession> unassignedCourseSessions = new ArrayList<>();
        for(CourseSession courseSession : courseSessions){
            if(!courseSession.isAssigned()){
                unassignedCourseSessions.add(courseSession);
            }
        }
        return unassignedCourseSessions;
    }

    public List<CourseSession> getAssignedCourseSessions(){
        List<CourseSession> assignedCourseSessions = new ArrayList<>();
        for(CourseSession courseSession : courseSessions){
            if(courseSession.isAssigned()){
                assignedCourseSessions.add(courseSession);
            }
        }
        return assignedCourseSessions;
    }

    public String toString(){
        return id.toString();
    }
}
