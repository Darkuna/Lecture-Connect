package com.example.demo.models;

import com.example.demo.models.base.TimestampedEntity;
import com.example.demo.models.enums.CourseType;
import com.example.demo.models.enums.StudyType;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Course extends TimestampedEntity implements Persistable<String>, Serializable {
    @Id
    private String id;
    private CourseType courseType;
    private StudyType studyType;
    private String name;
    private String lecturer;
    private int semester;
    private int duration;
    private int numberOfParticipants;
    private boolean computersNecessary;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<Timing> timingConstraints = new ArrayList<>();
    @Transient
    private int numberOfGroups;
    @Transient
    private boolean split;
    @Transient
    private List<Integer> splitTimes;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id != null && id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", courseType=" + courseType +
                ", studyType=" + studyType +
                ", name='" + name + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", semester=" + semester +
                ", duration=" + duration +
                ", numberOfParticipants=" + numberOfParticipants +
                ", computersNecessary=" + computersNecessary +
                ", timingConstraints=" + timingConstraints +
                ", numberOfGroups=" + numberOfGroups +
                ", isSplit=" + split +
                ", splitTimes=" + splitTimes +
                '}';
    }
}
