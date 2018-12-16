package it.mltk.kes.domain.model;

import it.mltk.kes.domain.event.DomainEvent;
import it.mltk.kes.domain.event.ProjectInitialized;
import it.mltk.kes.domain.event.ProjectRenamed;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@NoArgsConstructor
public class Project {
    private UUID id;
    private String name = "New Project";

    private List<DomainEvent> changes = new ArrayList<>();

    public Project(UUID uuid) {
        projectInitialized(new ProjectInitialized(uuid, Instant.now()));
    }

    private Project projectInitialized(final ProjectInitialized event) {
        log.debug("projectInitialized : begin");
        this.id = event.getProjectUuid();
        this.changes.add(event);
        log.debug("projectInitialized : end");
        return this;
    }

    public void renameProject(String name) {
        projectRenamed(new ProjectRenamed(this.id, Instant.now(), name));
    }

    private Project projectRenamed(final ProjectRenamed event) {
        log.debug("projectRenamed : begin");
        this.name = event.getName();
        this.changes.add(event);
        log.debug("projectRenamed : end");
        return this;
    }
}
