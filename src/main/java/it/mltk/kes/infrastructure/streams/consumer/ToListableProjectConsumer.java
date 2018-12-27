package it.mltk.kes.infrastructure.streams.consumer;

import org.apache.kafka.streams.kstream.KStream;

public interface ToListableProjectConsumer {
    void process(KStream<Object, byte[]> input);
}
