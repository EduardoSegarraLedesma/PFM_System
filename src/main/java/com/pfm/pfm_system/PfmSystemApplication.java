package com.pfm.pfm_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PfmSystemApplication {
    public static void main(String[] args) {
        // http://localhost:8080/index.html
        ServerController.getInstance();
        SpringApplication.run(PfmSystemApplication.class, args);
    }
}