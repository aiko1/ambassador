package com.pet.ambassador.service;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Getter
public class KafkaConsumer {
    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    @KafkaListener(topics = "${spring.kafka.topic-name}")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        payload = consumerRecord.toString();
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
