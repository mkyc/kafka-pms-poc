package it.mltk.kes.infrastructure.streams.producer;

import it.mltk.kes.domain.event.ProjectDomainEvent;

public interface DomainEventProducer {
    ProjectDomainEvent publish(final ProjectDomainEvent project);
}
