package com.lecture.coordinator.model;

import com.lecture.coordinator.model.enums.Day;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
public class Timing implements Persistable<Long>, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    @Column(name = "day_of_the_week")
    private Day day;
    @ManyToOne
    private Course course;
    @ManyToOne
    private RoomTable roomTable;

    //CONSTRUCTOR
    public Timing(){}

    //GETTERS AND SETTERS
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public Day getDay() {
        return day;
    }
    public void setDay(Day day) {
        this.day = day;
    }
    @Override
    public Long getId() {
        return id;
    }

    //OTHER METHODS
    @Override
    public boolean isNew(){
        return id == null;
    }

    public String toString(){
        return String.format("%s, %s - %s Uhr", day, startTime, endTime);
    }
}
