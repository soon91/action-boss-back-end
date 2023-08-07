package com.sparta.actionboss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActionBossApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActionBossApplication.class, args);
    }

}
