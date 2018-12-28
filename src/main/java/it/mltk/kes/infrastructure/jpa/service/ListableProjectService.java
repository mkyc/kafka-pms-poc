package it.mltk.kes.infrastructure.jpa.service;

import it.mltk.kes.infrastructure.jpa.model.ListableProject;
import it.mltk.kes.infrastructure.jpa.repository.ListableProjectRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ListableProjectService {

    private final ListableProjectRepository listableProjectRepository;

    public ListableProjectService(final ListableProjectRepository listableProjectRepository) {
        this.listableProjectRepository = listableProjectRepository;
    }

    public void store(ListableProject project) {
        if (project.getName() == null) {
            Optional<ListableProject> existingProject = listableProjectRepository.findById(project.getProjectUuid());
            existingProject.ifPresent(p -> {
                project.setName(p.getName());
                listableProjectRepository.save(project);
            });
        } else {
            listableProjectRepository.save(project);
        }
    }

    public List<ListableProject> getAll() {
        return listableProjectRepository.findAll();
    }
}
