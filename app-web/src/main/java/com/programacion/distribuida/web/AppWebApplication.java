package com.programacion.distribuida.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppWebApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AppWebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("ok");
    }
}

