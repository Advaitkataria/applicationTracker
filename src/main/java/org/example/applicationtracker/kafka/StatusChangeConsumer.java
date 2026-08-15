package org.example.applicationtracker.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StatusChangeConsumer {

    @KafkaListener(topics = "application-status-changes",groupId = "notification-group")
    public void listen(String message){
        System.out.println("Received event: "+ message);
    }
}
