package it.mltk.kes.domain.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.vavr.API;
import it.mltk.kes.domain.event.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.Stream.ofAll;
import static lombok.AccessLevel.NONE;

@Data
@Slf4j
public class Project {

    @Setter(NONE)
    private UUID projectUuid;

    @Setter(NONE)
    private String name = "New Project";

    @Getter(NONE)
    @Setter(NONE)
    private Map<UUID, Task> tasks = new HashMap<>();

    @Getter(NONE)
    @Setter(NONE)
    private List<DomainEvent> changes = new ArrayList<>();

    public Project() {
    }

    public Project(final UUID projectUuid) {

        projectInitialized(new ProjectInitialized(projectUuid, Instant.now()));

    }

    // Builder Methods
    public static Project createFrom(final UUID projectUuid, final Collection<DomainEvent> domainEvents) {

        return ofAll(domainEvents).foldLeft(new Project(projectUuid), Project::handleEvent);
    }

    private Project projectInitialized(final ProjectInitialized event) {
        log.debug("projectInitialized : event=" + event);

        flushChanges();
        this.projectUuid = event.getProjectUuid();
        this.changes.add(event);

        return this;
    }

    public void renameProject(final String name) {

        projectRenamed(new ProjectRenamed(name, this.projectUuid, Instant.now()));

    }

    private Project projectRenamed(final ProjectRenamed event) {
        log.debug("projectRenamed : event=" + event);

        this.name = event.getName();
        this.changes.add(event);

        return this;
    }

    public String getName() {

        return this.name;
    }

    public void addTask(final UUID taskUuid, final String name) {

        taskAdded(new TaskAdded(taskUuid, name, this.projectUuid, Instant.now()));

    }

    private Project taskAdded(final TaskAdded event) {
        log.debug("taksAdded : event=" + event);

        this.tasks.put(event.getTaskUuid(), event.getTask());
        this.changes.add(event);

        return this;
    }

    public void renameTask(final UUID taskUuid, final String name) {
        taskRenamed(new TaskNameUpdated(taskUuid, name, this.projectUuid, Instant.now()));
    }

    private Project taskRenamed(TaskNameUpdated event) {
        log.debug("taskRenamed : event=" + event);

        this.tasks.replace(event.getTaskUuid(), event.getTask());
        this.changes.add(event);

        return this;
    }

    public void deleteTask(final UUID taskUuid) {
        taskDeleted(new TaskDeleted(taskUuid, this.projectUuid, Instant.now()));
    }

    private Project taskDeleted(TaskDeleted event) {
        log.debug("taskDeleted : event=" + event);

        this.tasks.remove(event.getTaskUuid());
        this.changes.add(event);

        return this;
    }

    public Map<UUID, Task> getTasks() {

        return ImmutableMap.copyOf(this.tasks);
    }

    public List<DomainEvent> changes() {

        return ImmutableList.copyOf(this.changes);
    }

    public void flushChanges() {

        this.changes.clear();

    }

    public Project handleEvent(final DomainEvent domainEvent) {

        return API.Match(domainEvent).of(
                Case($(instanceOf(ProjectInitialized.class)), this::projectInitialized),
                Case($(instanceOf(ProjectRenamed.class)), this::projectRenamed),
                Case($(instanceOf(TaskAdded.class)), this::taskAdded),
                Case($(instanceOf(TaskNameUpdated.class)), this::taskRenamed),
                Case($(instanceOf(TaskDeleted.class)), this::taskDeleted),
                Case($(), this)
        );
    }

}

