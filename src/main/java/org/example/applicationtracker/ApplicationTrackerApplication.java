package org.example.applicationtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableDiscoveryClient
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode =
        EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ApplicationTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTrackerApplication.class, args);
    }

}
