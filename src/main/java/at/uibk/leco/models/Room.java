package at.uibk.leco.models;

import at.uibk.leco.models.base.TimestampedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Room extends TimestampedEntity implements Persistable<String>, Serializable {
    @Id
    private String id;
    private int capacity;
    private boolean computersAvailable;
    @Transient
    private List<Timing> timingConstraints;

    public Room() {
    }

    public Room(String id, int capacity, boolean computersAvailable) {
        this.id = id;
        this.capacity = capacity;
        this.computersAvailable = computersAvailable;
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id != null && id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
