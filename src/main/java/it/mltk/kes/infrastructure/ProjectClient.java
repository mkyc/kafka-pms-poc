package it.mltk.kes.infrastructure;

import it.mltk.kes.domain.event.DomainEvent;
import it.mltk.kes.domain.model.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ProjectClient {

    static String PROJECTS_STORE = "projects-store";

    @Autowired
    DomainEventProducer domainEventProducer;

    @Autowired
    QueryableStoreRegistry queryableStoreRegistry;

    public void save(Project project) {
        List<DomainEvent> newChanges = project.changes();

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
