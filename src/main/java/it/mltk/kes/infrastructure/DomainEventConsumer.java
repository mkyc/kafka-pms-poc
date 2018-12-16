package it.mltk.kes.infrastructure;

import org.apache.kafka.streams.kstream.KStream;

public interface DomainEventConsumer {
    void process(KStream<Object, byte[]> input);
}
