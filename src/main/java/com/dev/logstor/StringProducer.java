package com.dev.logstor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StringProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public StringProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendJsonString(String jsonString) {
        kafkaTemplate.send("logstor", jsonString);
        System.out.println("Sent raw JSON string: " + jsonString);
        return "Sent raw JSON string: " + jsonString;
    }
}
