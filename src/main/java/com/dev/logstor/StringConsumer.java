package com.dev.logstor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.dev.logstor.models.LogStor;
import com.google.gson.Gson;

@Service
public class StringConsumer {

    private final Gson gson = new Gson();
    
    @Autowired
    LogStorService service_obj;

    @KafkaListener(topics = "logstor", groupId="logstor-consumers")
    public void consume(String message) {
        System.out.println("Received raw message: " + message);
        
        // Convert to POJO
        LogStor log = gson.fromJson(message, LogStor.class);
        service_obj.postLogs(log);
        
    }
}
