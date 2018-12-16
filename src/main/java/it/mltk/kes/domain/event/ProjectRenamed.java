package it.mltk.kes.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectRenamed extends DomainEvent {
    private final String name;

    public ProjectRenamed(UUID projectUuid, Instant when, String name) {
        super(projectUuid, when);
        this.name = name;
    }
}
