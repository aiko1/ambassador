package com.pet.ambassador.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest()
@EmbeddedKafka(partitions = 1, topics = {"demo"})
public class KafkaTest {
    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Value("${spring.kafka.topic-name}")
    private String topic;

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.consumer.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
    }

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
            throws Exception {
        String data = "Sending with our own simple KafkaProducer";

        producer.sendMessage(data);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertTrue(consumer.getPayload().contains(data));
    }
}
