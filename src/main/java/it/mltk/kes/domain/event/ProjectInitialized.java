package it.mltk.kes.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectInitialized extends DomainEvent {

    public ProjectInitialized(UUID projectUuid, Instant when) {
        super(projectUuid, when);
    }
}
