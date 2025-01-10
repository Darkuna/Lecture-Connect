package at.uibk.leco.models;

import at.uibk.leco.models.enums.ChangeType;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private ChangeType changeType;
    private String timeTable;
    private LocalDateTime date;
    private String changeUser;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
