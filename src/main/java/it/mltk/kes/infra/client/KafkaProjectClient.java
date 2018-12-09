package it.mltk.kes.infra.client;

import it.mltk.kes.domain.client.DomainEventSource;
import it.mltk.kes.domain.client.ProjectClient;
import it.mltk.kes.domain.event.DomainEvent;
import it.mltk.kes.domain.model.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;

import java.util.List;
import java.util.UUID;

import static it.mltk.kes.infra.config.KafkaClientConfig.PROJECT_EVENTS_SNAPSHOTS;

@Slf4j
public class KafkaProjectClient implements ProjectClient {

    private final DomainEventSource domainEventSource;
    private final QueryableStoreRegistry queryableStoreRegistry;

    public KafkaProjectClient(
            final DomainEventSource domainEventSource,
            final QueryableStoreRegistry queryableStoreRegistry
    ) {

        this.domainEventSource = domainEventSource;
        this.queryableStoreRegistry = queryableStoreRegistry;

    }

    @Override
    public void save(final Project project) {
        log.debug( "save : enter" );

        log.debug( "project = " + project.toString());

        List<DomainEvent> newChanges = project.changes();

        newChanges.forEach( domainEvent -> {
            log.debug( "save : domainEvent=" + domainEvent );

            this.domainEventSource.publish( domainEvent );

        });
        project.flushChanges();

        log.debug( "save : exit" );
    }



    @Override
    public Project find( final UUID projectUuid ) {
        log.debug( "find : enter" );

        try {

            ReadOnlyKeyValueStore<String, Project> store = queryableStoreRegistry.getQueryableStoreType(PROJECT_EVENTS_SNAPSHOTS, QueryableStoreTypes.<String, Project>keyValueStore() );

            log.debug( "find : search=" + projectUuid.toString() );
            Project project = store.get( projectUuid.toString() );
            if( null != project ) {

                log.debug( "find : before flush project=" + project.toString() );
                project.flushChanges();
                log.debug( "find : project=" + project.toString() );

                log.debug( "find : exit" );
                return project;

            } else {

                throw new IllegalArgumentException( "project[" + projectUuid.toString() + "] not found!" );
            }

        } catch( InvalidStateStoreException e ) {
            log.error( "find : error", e );
        }
        throw new IllegalArgumentException( "project[" + projectUuid.toString() + "] not found!" );
    }

}

