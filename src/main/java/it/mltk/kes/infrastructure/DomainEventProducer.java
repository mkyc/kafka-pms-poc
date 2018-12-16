package it.mltk.kes.infrastructure;

import it.mltk.kes.domain.event.DomainEvent;

public interface DomainEventProducer {
    DomainEvent publish(final DomainEvent project);
}
