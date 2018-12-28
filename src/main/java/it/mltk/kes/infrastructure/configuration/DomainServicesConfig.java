package it.mltk.kes.infrastructure.configuration;

import it.mltk.kes.domain.service.ProjectService;
import it.mltk.kes.infrastructure.streams.client.ProjectClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServicesConfig {
    @Bean
    public ProjectService projectService(final ProjectClient projectClient) {
        return new ProjectService(projectClient);
    }
}
