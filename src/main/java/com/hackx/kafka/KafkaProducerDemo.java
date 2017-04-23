package com.hackx.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 128);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 1024);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 200; i++) {
            System.out.println("" + i);
            producer.send(new ProducerRecord<String, String>("first_topic", Integer.toString(i), Integer.toString(i)));
        }
        producer.close();
    }

}
