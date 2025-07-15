package com.programacion.distribuida.web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Collections;

@Service
public class CustomerService {
    private WebClient webClient;

    @Value("${customers.service.url:http://localhost:7070/customers}")
    private String customersServiceUrl;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl(customersServiceUrl).build();
    }

    public List<?> findAll() {
        return webClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .block();
    }

    public Object findById(Integer id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}

