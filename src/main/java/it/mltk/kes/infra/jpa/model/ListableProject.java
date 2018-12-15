package it.mltk.kes.infra.jpa.model;

import it.mltk.kes.domain.model.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ListableProject {
    @Id
    private UUID id;
    private String name;

    public static ListableProject fromProject(final Project project) {
        ListableProject listableProject = new ListableProject();
        listableProject.id = project.getProjectUuid();
        listableProject.name = project.getName();
        return listableProject;
    }
}
