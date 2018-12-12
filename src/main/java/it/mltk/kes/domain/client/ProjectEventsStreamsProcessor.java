package it.mltk.kes.domain.client;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface ProjectEventsStreamsProcessor {
    @Input("input")
    KStream<?, ?> input();
}