package it.mltk.kes.infrastructure;

import it.mltk.kes.domain.event.DomainEvent;
import it.mltk.kes.domain.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProjectClient {

    @Autowired
    DomainEventProducer domainEventProducer;

    public void save(Project project) {
        List<DomainEvent> newChanges = project.changes();

        newChanges.forEach(domainEvent -> domainEventProducer.publish(domainEvent));
        project.flushChanges();
    }

    public Project find(UUID projectUuid) {
        //TODO get data from store
        return new Project(projectUuid);
    }
}
