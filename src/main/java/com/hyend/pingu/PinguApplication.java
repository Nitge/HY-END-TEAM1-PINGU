package com.hyend.pingu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PinguApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinguApplication.class, args);
    }

}
