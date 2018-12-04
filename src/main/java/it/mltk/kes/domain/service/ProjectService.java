package it.mltk.kes.domain.service;

import it.mltk.kes.domain.client.ProjectClient;
import it.mltk.kes.domain.model.Project;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class ProjectService {

    private final ProjectClient client;

    public ProjectService(final ProjectClient client) {

        this.client = client;

    }

    public UUID createProject() {
        log.debug("createProject : enter");

        Project project = new Project(UUID.randomUUID());
        this.client.save(project);

        return project.getProjectUuid();
    }

    public void renameProject(final UUID projectUuid, final String name) {
        log.debug("renameProject : enter");

        Project project = this.client.find(projectUuid);
        project.renameProject(name);
        this.client.save(project);
    }

    public UUID addTask(final UUID projectUuid, final String name) {
        log.debug("addTask : enter");

        Project project = this.client.find(projectUuid);

        UUID taskUuid = UUID.randomUUID();
        project.addTask(taskUuid, name);

        this.client.save(project);

        return taskUuid;
    }

    public void renameTask(final UUID projectUuid, final UUID taskUuid, final String name) {
        log.debug("renameTask : enter");

        Project project = this.client.find(projectUuid);
        project.renameTask(taskUuid, name);

        this.client.save(project);
    }

    public void deleteTask(final UUID projectUuid, final UUID taskUuid) {
        log.debug("deleteTask : enter");

        Project project = this.client.find(projectUuid);
        project.deleteTask(taskUuid);

        this.client.save(project);
    }

    public Project find( final UUID projectUuid ) {
        log.debug( "find : enter" );

        Project project = this.client.find( projectUuid );
        log.debug( "find : project=" + project );

        log.debug( "find : exit" );
        return project;
    }

}
