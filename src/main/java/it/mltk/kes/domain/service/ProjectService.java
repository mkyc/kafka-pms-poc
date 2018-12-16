package it.mltk.kes.domain.service;

import it.mltk.kes.domain.model.Project;
import it.mltk.kes.infrastructure.ProjectClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    ProjectClient projectClient;

    public UUID initProject() {

        Project project = new Project(UUID.randomUUID());

        log.debug("project : " + project);

        projectClient.save(project);

        return project.getId();
    }

    public void renameProject(UUID projectUuid, String name) {
        Project project = projectClient.find(projectUuid);
        project.renameProject(name);
        projectClient.save(project);
    }
}
