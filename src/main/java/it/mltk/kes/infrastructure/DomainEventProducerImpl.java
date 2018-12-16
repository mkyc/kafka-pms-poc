package it.mltk.kes.infrastructure;

import it.mltk.kes.domain.event.DomainEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.MessageChannel;

@EnableBinding(DomainEventProducerImpl.DomainEventProducerBinding.class)
public class DomainEventProducerImpl implements DomainEventProducer {

    @Publisher(channel = DomainEventProducerBinding.OUTPUT)
    public DomainEvent publish(final DomainEvent domainEvent) {
        return domainEvent;
    }

    interface DomainEventProducerBinding {
        String OUTPUT = "domain-event-producer";

        @Output("domain-event-producer")
        MessageChannel output();
    }
}
