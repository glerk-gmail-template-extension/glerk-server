package com.glerk.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GlerkApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlerkApplication.class, args);
    }

}
