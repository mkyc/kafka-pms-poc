package it.mltk.kes.infra.client;

import it.mltk.kes.domain.client.DomainEventSource;
import it.mltk.kes.domain.event.DomainEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.Publisher;

@EnableBinding(Source.class)
public class DomainEventSourceImpl implements DomainEventSource {

    @Publisher(channel = Source.OUTPUT)
    public DomainEvent publish(final DomainEvent event) {
        return event;
    }

}
