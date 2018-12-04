package it.mltk.kes.infra.config;

import it.mltk.kes.domain.client.DomainEventSource;
import it.mltk.kes.domain.client.ProjectClient;
import it.mltk.kes.infra.client.KafkaProjectClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableAutoConfiguration
public class KafkaClientConfig {

    public static final String PROJECT_EVENTS = "project-events";
    public static final String PROJECT_EVENTS_SNAPSHOTS = "project-events-snapshots";

    @Bean
    @Primary
    public ProjectClient projectClient(
            final DomainEventSource domainEventSource,
            final QueryableStoreRegistry queryableStoreRegistry
    ) {

        return new KafkaProjectClient( domainEventSource, queryableStoreRegistry );
    }

}
