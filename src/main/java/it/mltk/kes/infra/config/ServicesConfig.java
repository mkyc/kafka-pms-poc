package it.mltk.kes.infra.config;

import it.mltk.kes.domain.client.ProjectClient;
import it.mltk.kes.domain.service.ProjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    public ProjectService projectService(final ProjectClient projectClient ) {

        return new ProjectService( projectClient );
    }

}