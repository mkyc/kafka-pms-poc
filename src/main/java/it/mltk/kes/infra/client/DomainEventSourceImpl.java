package it.mltk.kes.infra.client;

import it.mltk.kes.domain.client.DomainEventSource;
import it.mltk.kes.domain.event.DomainEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.MessageChannel;

@EnableBinding(DomainEventSourceImpl.DomainEventProducerBinding.class)
public class DomainEventSourceImpl implements DomainEventSource {

    @Publisher(channel = DomainEventSourceImpl.DomainEventProducerBinding.OUTPUT)
    public DomainEvent publish(final DomainEvent event) {
        return event;
    }

    interface DomainEventProducerBinding {
        String OUTPUT = "project-events-output";

        @Output("project-events-output")
        MessageChannel output();
    }
}
