package it.mltk.kes.infrastructure;

import it.mltk.kes.domain.model.Project;

public interface ProjectProducer {
    Project publish(final Project project);
}
