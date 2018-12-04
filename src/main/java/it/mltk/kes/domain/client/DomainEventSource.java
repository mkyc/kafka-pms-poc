package it.mltk.kes.domain.client;

import it.mltk.kes.domain.event.DomainEvent;

public interface DomainEventSource {

    DomainEvent publish( final DomainEvent event );
}
