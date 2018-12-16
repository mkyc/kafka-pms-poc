package it.mltk.kes.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mltk.kes.domain.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;

@EnableBinding(DomainEventConsumerImpl.DomainEventConsumerBinding.class)
@Slf4j
public class DomainEventConsumerImpl implements DomainEventConsumer {

    @Autowired
    ObjectMapper objectMapper;

    @StreamListener(DomainEventConsumerBinding.INPUT)
    public void process(KStream<Object, byte[]> input) {
        input.map((k, v) -> {
            try {
                DomainEvent domainEvent = objectMapper.readValue(v, DomainEvent.class);
                return new KeyValue<>(domainEvent.getProjectUuid().toString(), domainEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).peek((k, v) -> log.debug("key: " + k + " domainEvent: " + v));
    }

    interface DomainEventConsumerBinding {
        String INPUT = "domain-event-consumer";

        @Input("domain-event-consumer")
        KStream<?, ?> input();
    }
}
