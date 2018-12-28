package it.mltk.kes.infrastructure.streams.client;

import it.mltk.kes.domain.event.ProjectDomainEvent;
import it.mltk.kes.domain.model.Project;
import it.mltk.kes.infrastructure.streams.producer.DomainEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static it.mltk.kes.infrastructure.configuration.StreamsClientsConfig.PROJECTS_STORE;

@Component
@Slf4j
public class ProjectClientImpl implements ProjectClient {

    private final DomainEventProducer domainEventProducer;
    private final QueryableStoreRegistry queryableStoreRegistry;

    public ProjectClientImpl(final DomainEventProducer domainEventProducer, final QueryableStoreRegistry queryableStoreRegistry) {
        this.domainEventProducer = domainEventProducer;
        this.queryableStoreRegistry = queryableStoreRegistry;
    }

    public void save(Project project) {
        List<ProjectDomainEvent> newChanges = project.changes();

        newChanges.forEach(domainEvent -> domainEventProducer.publish(domainEvent));
        project.flushChanges();
    }

    public Project find(UUID projectUuid) {
        log.debug("find : enter");
        try {

            ReadOnlyKeyValueStore<String, Project> store = queryableStoreRegistry.getQueryableStoreType(PROJECTS_STORE, QueryableStoreTypes.keyValueStore());

            log.debug("find : search=" + projectUuid.toString());
            Project project = store.get(projectUuid.toString());
            if (null != project) {

                log.debug("find : before flush project=" + project.toString());
                project.flushChanges();
                log.debug("find : project=" + project.toString());

                log.debug("find : exit");
                return project;

            } else {
                throw new IllegalArgumentException("project[" + projectUuid.toString() + "] not found!");
            }

        } catch (InvalidStateStoreException e) {
            log.error("find : error", e);
        }
        throw new IllegalArgumentException("project[" + projectUuid.toString() + "] not found!");
    }
}
