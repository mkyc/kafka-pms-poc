package it.mltk.kes.infra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mltk.kes.domain.client.DomainEventSink;
import it.mltk.kes.domain.client.ProjectEventsStreamsProcessor;
import it.mltk.kes.domain.event.DomainEvent;
import it.mltk.kes.domain.model.Project;
import it.mltk.kes.infra.jpa.model.ListableProject;
import it.mltk.kes.infra.jpa.service.ListableProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.io.IOException;

import static it.mltk.kes.infra.config.KafkaClientConfig.PROJECT_EVENTS_SNAPSHOTS;

@EnableBinding(ProjectEventsStreamsProcessor.class)
@Slf4j
public class DomainEventSinkImpl implements DomainEventSink {

    private final ObjectMapper mapper;
    private final Serde<DomainEvent> domainEventSerde;
    private final Serde<Project> projectSerde;
    private final ListableProjectService listableProjectService;

    public DomainEventSinkImpl(final ObjectMapper mapper, final ListableProjectService listableProjectService) {
        this.mapper = mapper;
        this.listableProjectService = listableProjectService;
        this.domainEventSerde = new JsonSerde<>(DomainEvent.class, mapper);
        this.projectSerde = new JsonSerde<>(Project.class, mapper);
    }

    @StreamListener("input")
    public void process(KStream<Object, byte[]> input) {
        log.debug("process : enter");
        input
                .map((key, value) -> {
                    try {
                        DomainEvent domainEvent = mapper.readValue(value, DomainEvent.class);
                        log.debug("process : domainEvent=" + domainEvent);
                        return new KeyValue<>(domainEvent.getProjectUuid().toString(), domainEvent);
                    } catch (IOException e) {
                        log.error("process : error converting json to DomainEvent", e);
                    }
                    return null;
                })
                .groupBy((s, domainEvent) -> s, Serialized.with(Serdes.String(), domainEventSerde))
                .aggregate(
                        Project::new,
                        (key, domainEvent, project) -> project.handleEvent(domainEvent),
                        Materialized.<String, Project, KeyValueStore<Bytes, byte[]>>as(PROJECT_EVENTS_SNAPSHOTS)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(projectSerde)
                )
                .toStream()
                .peek((k, v) -> {
                    log.debug("peek: " + v);
                    listableProjectService.store(ListableProject.fromProject(v));
                    log.debug("stored: " + v);
                });

        log.debug("process : exit");
    }

}
