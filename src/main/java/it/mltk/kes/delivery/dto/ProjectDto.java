package it.mltk.kes.delivery.dto;

import it.mltk.kes.domain.model.Project;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ProjectDto {

    private UUID projectUuid;
    private String name;
    private Collection<TaskDto> tasks;

    public static ProjectDto fromProject(final Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectUuid(project.getId());
        projectDto.setName(project.getName());

        if (null != project.getTasks() && !project.getTasks().isEmpty()) {
            projectDto.setTasks(
                    project.getTasks().entrySet()
                            .stream()
                            .map(t -> new TaskDto(t.getKey(), t.getValue().getName()))
                            .collect(Collectors.toCollection(ArrayList::new)));
        }
        return projectDto;
    }
}
