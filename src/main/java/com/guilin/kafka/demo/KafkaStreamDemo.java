package com.guilin.kafka.demo;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by T57 on 2016/7/3 0003.
 */
public class KafkaStreamDemo {

    public static void main(String[] args) {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(StreamsConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(StreamsConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(StreamsConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(StreamsConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        StreamsConfig config = new StreamsConfig(props);

        KStreamBuilder builder = new KStreamBuilder();
//        builder.table("my-input-topic").mapValue(value -> value.length().toString()).to("my-output-topic");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
    }

}
