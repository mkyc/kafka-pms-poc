package it.mltk.kes.infrastructure.streams.configuration;

import it.mltk.kes.infrastructure.streams.producer.DomainEventProducer;
import it.mltk.kes.infrastructure.streams.client.ProjectClientImpl;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ClientsConfig {

    public static String PROJECTS_STORE = "projects-store";

    @Bean
    @Primary
    public ProjectClientImpl projectClient(final DomainEventProducer domainEventProducer, final QueryableStoreRegistry queryableStoreRegistry) {
        return new ProjectClientImpl(domainEventProducer, queryableStoreRegistry);
    }
}
