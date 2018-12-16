package it.mltk.kes.infrastructure;

import org.apache.kafka.streams.kstream.KStream;

public interface ProjectConsumer {
    void process(KStream<Object, byte[]> input);
}
