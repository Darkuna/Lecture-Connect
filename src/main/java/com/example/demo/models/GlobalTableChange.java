package com.example.demo.models;

import com.example.demo.models.enums.ChangeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class GlobalTableChange implements Persistable<Long>, Serializable {
    @Id
    private Long id;
    private String description;
    private ChangeType changeType;
    @ManyToOne
    private TimeTable timeTable;
    private LocalDateTime date;
    @ManyToOne
    private Userx user;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
