package com.programacion.distribuida.web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Collections;

@Service
public class AuthorService {
    private WebClient webClient;

    @Value("${authors.service.url:http://localhost:8080/authors}")
    private String authorsServiceUrl;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl(authorsServiceUrl).build();
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

    public List<?> findByBook(String isbn) {
        try {
            return webClient.get()
                    .uri("/find/{isbn}", isbn)
                    .retrieve()
                    .bodyToFlux(Object.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
