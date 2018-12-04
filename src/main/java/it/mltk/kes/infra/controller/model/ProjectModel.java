package it.mltk.kes.infra.controller.model;

import it.mltk.kes.domain.model.Project;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Slf4j
public class ProjectModel {

    private UUID projectUuid;
    private String name;
    private Collection<TaskModel> tasks;

    public static ProjectModel fromProject(final Project project ) {

        ProjectModel model = new ProjectModel();
        model.setProjectUuid(project.getProjectUuid());
        model.setName( project.getName() );

        if( null != project.getTasks() && !project.getTasks().isEmpty() ) {
            model.setTasks(project.getTasks().entrySet().stream().map(t -> new TaskModel(t.getKey(), t.getValue().getName())).collect(Collectors.toCollection(ArrayList::new)));
        }

        return model;
    }

}

