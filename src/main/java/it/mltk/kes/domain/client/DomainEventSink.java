package it.mltk.kes.domain.client;

import org.apache.kafka.streams.kstream.KStream;

public interface DomainEventSink {
    void process(KStream<Object, byte[]> input);
}
