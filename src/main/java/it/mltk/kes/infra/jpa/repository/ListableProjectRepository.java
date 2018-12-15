package it.mltk.kes.infra.jpa.repository;

import it.mltk.kes.infra.jpa.model.ListableProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListableProjectRepository extends JpaRepository<ListableProject, UUID> {
}
