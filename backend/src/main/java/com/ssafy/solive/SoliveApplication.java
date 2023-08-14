package com.ssafy.solive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SoliveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoliveApplication.class, args);
    }

}
