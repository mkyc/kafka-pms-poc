package it.mltk.kes.infrastructure.streams.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mltk.kes.domain.event.ProjectDomainEvent;
import it.mltk.kes.infrastructure.jpa.model.ListableProject;
import it.mltk.kes.infrastructure.jpa.service.ListableProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;

@EnableBinding(ToListableProjectConsumerImpl.DomainEventToListableConsumerBinding.class)
@Slf4j
public class ToListableProjectConsumerImpl implements ToListableProjectConsumer {

    private final ObjectMapper objectMapper;
    private final ListableProjectService listableProjectService;

    ToListableProjectConsumerImpl(final ObjectMapper objectMapper, final ListableProjectService listableProjectService) {
        this.objectMapper = objectMapper;
        this.listableProjectService = listableProjectService;
    }

    @SuppressWarnings("Duplicates")
    @StreamListener(DomainEventToListableConsumerBinding.INPUT)
    public void process(KStream<Object, byte[]> input) {

        input
                .map((key, value) -> {
                    try {
                        ProjectDomainEvent projectDomainEvent = objectMapper.readValue(value, ProjectDomainEvent.class);
                        return new KeyValue<>(projectDomainEvent.getProjectUuid().toString(), ListableProject.fromEvent(projectDomainEvent));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .peek((key, value) -> log.debug("key: " + key + " listableProject: " + value))
                .peek(((key, value) -> listableProjectService.store(value)));
    }

    interface DomainEventToListableConsumerBinding {
        String INPUT = "domain-event-to-listable-consumer";

        @Input("domain-event-to-listable-consumer")
        KStream<?, ?> input();
    }
}
