package it.mltk.kes.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mltk.kes.domain.model.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;

@EnableBinding(ProjectConsumerImpl.ProjectConsumerBinding.class)
@Slf4j
public class ProjectConsumerImpl implements ProjectConsumer {

    @Autowired
    ObjectMapper objectMapper;

    @StreamListener(ProjectConsumerBinding.INPUT)
    public void process(KStream<Object, byte[]> input) {
        objectMapper.enableDefaultTyping();
        input.map((k, v) -> {
            try {
                Project project = objectMapper.readValue(v, Project.class);
                return new KeyValue<>(project.getId().toString(), project);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).peek((k, v) -> log.debug("key: " + k + " project: " + v));
    }

    interface ProjectConsumerBinding {
        String INPUT = "project-consumer";

        @Input("project-consumer")
        KStream<?, ?> input();
    }
}
