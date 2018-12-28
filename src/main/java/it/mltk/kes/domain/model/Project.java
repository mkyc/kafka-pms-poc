package it.mltk.kes.domain.model;

import it.mltk.kes.domain.event.*;
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
        this.tasks.put(event.getTaskUuid(), new Task(event.getName()));
        this.changes.add(event);
        log.debug("taskAdded : end");
        return this;
    }

    public void deleteTask(final UUID taskUuid) {
        taskDeleted(new TaskDeleted(taskUuid, this.id, Instant.now()));
    }

    private Project taskDeleted(TaskDeleted event) {
        log.debug("taskDeleted : begin");
        this.tasks.remove(event.getTaskUuid());
        this.changes.add(event);
        log.debug("taskDeleted : end");
        return this;
    }

    public void renameTask(final UUID taskUuid, final String name) {
        taskRenamed(new TaskRenamed(taskUuid, name, this.id, Instant.now()));
    }

    private Project taskRenamed(TaskRenamed event) {
        log.debug("taskRenamed : begin");
        this.tasks.replace(event.getTaskUuid(), new Task(event.getName()));
        this.changes.add(event);
        log.debug("taskRenamed : end");
        return this;
    }

    public List<ProjectDomainEvent> changes() {
        return Collections.unmodifiableList(changes);
    }

    public Map<UUID, Task> getTasks() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(this.tasks));
    }

    public void flushChanges() {
        this.changes.clear();
    }

    public Project handleEvent(final ProjectDomainEvent event) {
        log.debug("handleEvent : event=" + event.toString());
        if (event instanceof ProjectInitialized) {
            this.projectInitialized((ProjectInitialized) event);
        } else if (event instanceof ProjectRenamed) {
            this.projectRenamed((ProjectRenamed) event);
        } else if (event instanceof TaskAdded) {
            this.taskAdded((TaskAdded) event);
        } else if (event instanceof TaskDeleted) {
            this.taskDeleted((TaskDeleted) event);
        } else if (event instanceof TaskRenamed) {
            this.taskRenamed((TaskRenamed) event);
        }
        return this;
    }
}
