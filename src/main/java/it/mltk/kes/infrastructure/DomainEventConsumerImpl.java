package it.mltk.kes.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mltk.kes.domain.event.DomainEvent;
import it.mltk.kes.domain.model.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.io.IOException;

import static it.mltk.kes.infrastructure.ProjectClient.PROJECTS_STORE;

@EnableBinding(DomainEventConsumerImpl.DomainEventConsumerBinding.class)
@Slf4j
public class DomainEventConsumerImpl implements DomainEventConsumer {

    private final Serde<DomainEvent> domainEventSerde;
    private final Serde<Project> projectSerde;
    ObjectMapper objectMapper;

    DomainEventConsumerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        domainEventSerde = new JsonSerde<>(DomainEvent.class, objectMapper);
        projectSerde = new JsonSerde<>(Project.class, objectMapper);
    }

    @StreamListener(DomainEventConsumerBinding.INPUT)
    public void process(KStream<Object, byte[]> input) {
        input
                .map((k, v) -> {
                    try {
                        DomainEvent domainEvent = objectMapper.readValue(v, DomainEvent.class);
                        return new KeyValue<>(domainEvent.getProjectUuid().toString(), domainEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .peek((k, v) -> log.debug("key: " + k + " domainEvent: " + v))
                .groupBy((s, domainEvent) -> s, Serialized.with(Serdes.String(), domainEventSerde))
                .aggregate(
                        Project::new,
                        (key, domainEvent, project) -> project.handleEvent(domainEvent),
                        Materialized.<String, Project, KeyValueStore<Bytes, byte[]>>as(PROJECTS_STORE)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(projectSerde)
                )
                .toStream()
                .peek((k, v) -> log.debug("key: " + k + " project: " + v))
                .to("projects", Produced.with(Serdes.String(), projectSerde));
    }

    interface DomainEventConsumerBinding {
        String INPUT = "domain-event-consumer";

        @Input("domain-event-consumer")
        KStream<?, ?> input();
    }
}
