package com.example.demo.models;

import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.TimingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Timing implements Persistable<Long>, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private TimingType timingType;
    @Column(name = "day_of_the_week")
    private Day day;
    @ManyToOne
    private Course course;
    @ManyToOne
    private RoomTable roomTable;

    //CONSTRUCTOR
    public Timing(){}

    public Timing(LocalTime startTime, LocalTime endTime, Day day, TimingType timingType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.timingType = timingType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timing timing = (Timing) o;
        return id != null && id.equals(timing.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public long getDuration(){
        return startTime.until(endTime, ChronoUnit.MINUTES);
    }

    public boolean intersects(Timing timing) {
        if (this.day != timing.day) {
            return false;
        }

        return this.startTime.isBefore(timing.endTime) && timing.startTime.isBefore(this.endTime);
    }

    /*
    This toString() method was used for test data creation

    public String toString(){
        return String.format("INSERT INTO TIMING VALUES (%d, %d, '%s', '%s', NULL, NULL ",-id-20, day.ordinal(),
                startTime.getHour() >= 10 ? startTime.getHour()+":"+ startTime.getMinute()+":00" : "0" + startTime.getHour()+":"+ startTime.getMinute()+":00",
                endTime.getHour() >= 10 ? endTime.getHour()+":"+ endTime.getMinute()+":00" : "0" + endTime.getHour()+":"+ endTime.getMinute()+":00");
    }
    */

    public String toString(){
        return String.format("%s, %s - %s Uhr", day.toString(), startTime, endTime);
    }
}
