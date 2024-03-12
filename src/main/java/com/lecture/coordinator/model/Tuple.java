package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class Tuple{

}

/*

@Entity
public class Tuple<T> implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private T l;
    private T r;

    public T getL() {
        return l;
    }

    public void setL(T l) {
        this.l = l;
    }

    public T getR() {
        return r;
    }

    public void setR(T r) {
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
*/