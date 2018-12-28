package it.mltk.kes.infrastructure.jpa.model;

import it.mltk.kes.domain.event.*;
import it.mltk.kes.domain.model.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ListableProject {
    @Id
    private UUID projectUuid;
    private String name;
    private Instant when;

    public static ListableProject fromEvent(ProjectDomainEvent event) {
        if (event instanceof ProjectDomainEventIgnored) {
            return null;
        }

        ListableProject project = new ListableProject();

        project.setProjectUuid(event.getProjectUuid());
        project.setName(Project.NEW_PROJECT_NAME);
        project.setWhen(event.occurredOn());

        if (event instanceof ProjectRenamed) {
            project.setName(((ProjectRenamed) event).getName());
        } else if (event instanceof TaskAdded) {
            project.setName(null);
        } else if (event instanceof TaskDeleted) {
            project.setName(null);
        }

        return project;
    }
}
