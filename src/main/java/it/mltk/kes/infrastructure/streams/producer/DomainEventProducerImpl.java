package it.mltk.kes.infrastructure.streams.producer;

import it.mltk.kes.domain.event.ProjectDomainEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.MessageChannel;

@EnableBinding(DomainEventProducerImpl.DomainEventProducerBinding.class)
public class DomainEventProducerImpl implements DomainEventProducer {

    @Publisher(channel = DomainEventProducerBinding.OUTPUT)
    public ProjectDomainEvent publish(final ProjectDomainEvent projectDomainEvent) {
        return projectDomainEvent;
    }

    interface DomainEventProducerBinding {
        String OUTPUT = "domain-event-producer";

        @Output("domain-event-producer")
        MessageChannel output();
    }
}
