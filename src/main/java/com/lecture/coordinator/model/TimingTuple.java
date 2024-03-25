package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TimingTuple implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Timing l;
    @OneToOne
    private Timing r;

    public Timing getL() {
        return l;
    }

    public void setL(Timing l) {
        this.l = l;
    }

    public Timing getR() {
        return r;
    }

    public void setR(Timing r) {
        this.r = r;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
