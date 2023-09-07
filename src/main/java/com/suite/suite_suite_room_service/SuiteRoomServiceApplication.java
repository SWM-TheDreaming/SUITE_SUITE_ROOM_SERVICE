package com.suite.suite_suite_room_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SuiteRoomServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuiteRoomServiceApplication.class, args);
    }

}
