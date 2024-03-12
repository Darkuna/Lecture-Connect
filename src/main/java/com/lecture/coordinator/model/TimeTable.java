package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class TimeTable implements Persistable<Long>, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "academic_year")
    private int year;
    private Semester semester;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Room> rooms;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Room> courses;

    @OneToMany(mappedBy = "timeTable", fetch= FetchType.LAZY)
    private List<RoomTable> roomTables;

    //CONSTRUCTOR
    public TimeTable(){}

    //GETTERS AND SETTERS
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    //OTHER METHODS
    @Override
    public boolean isNew() {
        return id == null;
    }
}
