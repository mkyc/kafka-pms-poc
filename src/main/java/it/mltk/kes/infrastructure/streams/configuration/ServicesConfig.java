package it.mltk.kes.infrastructure.streams.configuration;

import it.mltk.kes.domain.service.ProjectService;
import it.mltk.kes.infrastructure.streams.client.ProjectClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {
    @Bean
    public ProjectService projectService(final ProjectClient projectClient) {
        return new ProjectService(projectClient);
    }
}
