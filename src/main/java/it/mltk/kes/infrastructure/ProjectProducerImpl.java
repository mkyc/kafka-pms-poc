package it.mltk.kes.infrastructure;

import it.mltk.kes.domain.model.Project;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.MessageChannel;

@EnableBinding(ProjectProducerImpl.ProjectProducerBinding.class)
public class ProjectProducerImpl implements ProjectProducer {

    @Publisher(channel = ProjectProducerBinding.OUTPUT)
    public Project publish(final Project project) {
        return project;
    }

    interface ProjectProducerBinding {
        String OUTPUT = "project-producer";

        @Output("project-producer")
        MessageChannel output();
    }
}
