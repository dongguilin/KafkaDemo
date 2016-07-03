package com.guilin.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 * Created by T57 on 2016/7/3 0003.
 */
public class ConsumerDemo {

    private static Properties props = new Properties();

    @BeforeClass
    public static void beforeClass() {
        props.put("bootstrap.servers", "aleiyeb:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }

    //Automatic Offset Committing
    @Test
    public void test1() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("guilin-topic", "my-topic"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            }
        }
    }

    //Manual Offset Control
    @Test
    public void test2() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("guilin-topic", "my-topic"));

        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
//                insertIntoDb(buffer);
                consumer.commitSync();// to mark all received messages as committed
                buffer.clear();
            }
        }
    }

    //Manual Offset Control
    @Test
    public void test3() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("guilin-topic", "my-topic"));

        try {
            boolean running = true;
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println(record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }

    //Manual Partition Assignment
    @Test
    public void test4() {
        String topic = "guilin-topic";
        TopicPartition partition0 = new TopicPartition(topic, 0);
        TopicPartition partition1 = new TopicPartition(topic, 1);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.assign(Arrays.asList(partition0, partition1));
    }

}
