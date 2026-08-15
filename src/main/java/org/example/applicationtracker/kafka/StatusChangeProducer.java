package org.example.applicationtracker.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatusChangeProducer {
    private final KafkaTemplate<String,String> kafkaTemplate;

    public StatusChangeProducer(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void sendStatusChange(String message){
        kafkaTemplate.send("application-status-changes",message);
    }
}
