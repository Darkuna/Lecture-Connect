package at.uibk.leco.models;

import at.uibk.leco.models.enums.Day;
import at.uibk.leco.models.enums.TimingType;
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
        if (!this.day.equals(timing.day)) {
            return false;
        }

        return this.startTime.isBefore(timing.endTime) && timing.startTime.isBefore(this.endTime);
    }

    public String toString(){
        return String.format("%s, %s - %s Uhr", day.toString(), startTime, endTime);
    }

    public boolean hasSameDayAndTime(Timing timing){
        return this.day.equals(timing.day) &&
                this.endTime.equals(timing.endTime) &&
                this.startTime.equals(timing.startTime);
    }
}
