package it.mltk.kes.domain.model;

import it.mltk.kes.domain.event.DomainEvent;
import it.mltk.kes.domain.event.ProjectInitialized;
import it.mltk.kes.domain.event.ProjectRenamed;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.NONE;

@Data
@Slf4j
@NoArgsConstructor
public class Project {
    @Setter(NONE)
    private UUID id;
    @Setter(NONE)
    private String name = "New Project";
    @Setter(NONE)
    private List<DomainEvent> changes = new ArrayList<>();

    public Project(UUID uuid) {
        projectInitialized(new ProjectInitialized(uuid, Instant.now()));
    }

    private Project projectInitialized(final ProjectInitialized event) {
        log.debug("projectInitialized : begin");
        flushChanges();
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

    public List<DomainEvent> changes() {
        return Collections.unmodifiableList(changes);
    }

    public void flushChanges() {
        this.changes.clear();
    }

    public Project handleEvent(final DomainEvent domainEvent) {
        log.debug("handleEvent : event=" + domainEvent.toString());
        if (domainEvent instanceof ProjectInitialized) {
            this.projectInitialized((ProjectInitialized) domainEvent);
        } else if (domainEvent instanceof ProjectRenamed) {
            this.projectRenamed((ProjectRenamed) domainEvent);
        }
        return this;
    }
}
