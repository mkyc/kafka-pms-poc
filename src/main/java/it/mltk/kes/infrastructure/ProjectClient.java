package it.mltk.kes.infrastructure;

import it.mltk.kes.domain.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProjectClient {

    @Autowired
    ProjectProducer projectProducer;

    public void save(Project project) {
        projectProducer.publish(project);
    }

    public Project find(UUID projectUuid) {
        //TODO get data from store
        return new Project(projectUuid);
    }
}
