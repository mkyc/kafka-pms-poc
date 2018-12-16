package it.mltk.kes.infra.jpa.service;

import it.mltk.kes.infra.jpa.model.ListableProject;
import it.mltk.kes.infra.jpa.repository.ListableProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
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
