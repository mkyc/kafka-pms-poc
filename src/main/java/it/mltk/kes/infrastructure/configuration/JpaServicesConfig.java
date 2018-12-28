package it.mltk.kes.infrastructure.configuration;

import it.mltk.kes.infrastructure.jpa.repository.ListableProjectRepository;
import it.mltk.kes.infrastructure.jpa.service.ListableProjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaServicesConfig {

    @Bean
    public ListableProjectService listableProjectService(final ListableProjectRepository listableProjectRepository) {
        return new ListableProjectService(listableProjectRepository);
    }
}
