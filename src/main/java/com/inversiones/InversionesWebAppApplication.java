package com.inversiones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal de la aplicación Spring Boot
 */
@SpringBootApplication
@EnableScheduling
public class InversionesWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(InversionesWebAppApplication.class, args);
    }
}
