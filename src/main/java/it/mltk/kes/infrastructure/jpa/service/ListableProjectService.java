package it.mltk.kes.infrastructure.jpa.service;

import it.mltk.kes.infrastructure.jpa.model.ListableProject;
import it.mltk.kes.infrastructure.jpa.repository.ListableProjectRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListableProjectService {

    private final ListableProjectRepository listableProjectRepository;

    public ListableProjectService(final ListableProjectRepository listableProjectRepository) {
        this.listableProjectRepository = listableProjectRepository;
    }

    public void store(ListableProject project) {
        listableProjectRepository.save(project);
    }
}
