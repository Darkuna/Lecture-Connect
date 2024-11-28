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

    public void removeRoomTable(RoomTable roomTable){
        if(roomTable != null){
            roomTables.remove(roomTable);
        }
    }

    public void addCourseSessions(List<CourseSession> courseSessions){
        if(courseSessions != null){
            this.courseSessions.addAll(courseSessions);
        }
    }

    public void removeCourseSession(CourseSession courseSession){
        if(courseSessions != null){
            this.courseSessions.remove(courseSession);
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

    public List<CourseSession> getUnassignedCourseSessionsWithComputersNeeded(){
        List<CourseSession> unassignedCourseSessions = new ArrayList<>();
        for(CourseSession courseSession : courseSessions){
            if(!courseSession.isAssigned() && courseSession.isComputersNecessary()){
                unassignedCourseSessions.add(courseSession);
            }
        }
        return unassignedCourseSessions;
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

    public List<CourseSession> getUnassignedCourseSessionsWithoutComputersNeeded(){
        List<CourseSession> unassignedCourseSessions = new ArrayList<>();
        for(CourseSession courseSession : courseSessions){
            if(!courseSession.isAssigned()  && !courseSession.isComputersNecessary()){
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

    public List<RoomTable> getRoomTablesWithComputersAvailable(){
        List<RoomTable> roomTablesWithComputersAvailable = new ArrayList<>();
        for(RoomTable roomTable : roomTables){
            if(roomTable.isComputersAvailable()){
                roomTablesWithComputersAvailable.add(roomTable);
            }
        }
        return roomTablesWithComputersAvailable;
    }

    public List<RoomTable> getRoomTablesWithoutComputersAvailable(){
        List<RoomTable> roomTablesWithoutComputersAvailable = new ArrayList<>();
        for(RoomTable roomTable : roomTables){
            if(!roomTable.isComputersAvailable()){
                roomTablesWithoutComputersAvailable.add(roomTable);
            }
        }
        return roomTablesWithoutComputersAvailable;
    }

    public String toString(){
        return id.toString();
    }
}
