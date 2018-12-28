package it.mltk.kes.domain.model;

import it.mltk.kes.domain.event.ProjectDomainEvent;
import it.mltk.kes.domain.event.ProjectInitialized;
import it.mltk.kes.domain.event.ProjectRenamed;
import it.mltk.kes.domain.event.TaskAdded;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;

import static lombok.AccessLevel.NONE;

@Data
@Slf4j
@NoArgsConstructor
public class Project {

    public static final String NEW_PROJECT_NAME = "New Project";

    @Setter(NONE)
    private UUID id;
    @Setter(NONE)
    private String name = NEW_PROJECT_NAME;
    @Getter(NONE)
    @Setter(NONE)
    private Map<UUID, Task> tasks = new HashMap<>();
    @Setter(NONE)
    private List<ProjectDomainEvent> changes = new ArrayList<>();

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

    public void addTask(final UUID taskUuid, final String name) {
        taskAdded(new TaskAdded(taskUuid, name, this.id, Instant.now()));
    }

    private Project taskAdded(final TaskAdded event) {
        log.debug("taskAdded : begin");
        this.tasks.put(event.getTaskUuid(), event.getTask());
        this.changes.add(event);
        log.debug("taskAdded : end");
        return this;
    }

    public List<ProjectDomainEvent> changes() {
        return Collections.unmodifiableList(changes);
    }

    public void flushChanges() {
        this.changes.clear();
    }

    public Project handleEvent(final ProjectDomainEvent projectDomainEvent) {
        log.debug("handleEvent : event=" + projectDomainEvent.toString());
        if (projectDomainEvent instanceof ProjectInitialized) {
            this.projectInitialized((ProjectInitialized) projectDomainEvent);
        } else if (projectDomainEvent instanceof ProjectRenamed) {
            this.projectRenamed((ProjectRenamed) projectDomainEvent);
        } else if (projectDomainEvent instanceof TaskAdded) {
            this.taskAdded((TaskAdded) projectDomainEvent);
        }
        return this;
    }
}
