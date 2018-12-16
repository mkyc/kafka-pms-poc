package it.mltk.kes.domain.event;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public abstract class DomainEvent {
    private final UUID projectUuid;
    private final Instant when;

    public DomainEvent(UUID projectUuid, Instant when) {
        this.projectUuid = projectUuid;
        this.when = when;
    }
}
