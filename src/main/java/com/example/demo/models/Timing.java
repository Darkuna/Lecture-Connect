package com.example.demo.models;

import com.example.demo.models.enums.Day;
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
    @Column(name = "day_of_the_week")
    private Day day;
    @ManyToOne
    private Course course;
    @ManyToOne
    private RoomTable roomTable;

    //CONSTRUCTOR
    public Timing(){}

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
}
