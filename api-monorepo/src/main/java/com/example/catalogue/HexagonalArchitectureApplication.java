package com.example.catalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HexagonalArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexagonalArchitectureApplication.class, args);
    }
}