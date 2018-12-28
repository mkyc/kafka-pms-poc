package it.mltk.kes.infrastructure.jpa.repository;

import it.mltk.kes.infrastructure.jpa.model.ListableProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListableProjectRepository extends JpaRepository<ListableProject, UUID> {
}
