package it.mltk.kes.domain.client;

import it.mltk.kes.domain.model.Project;

import java.util.UUID;

public interface ProjectClient {

    void save(final Project project);

    Project find(final UUID projectUuid);
}
