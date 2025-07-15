package com.programacion.distribuida.web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Collections;

@Service
public class BookService {
    private WebClient webClient;

    @Value("${books.service.url:http://localhost:9090/books}")
    private String booksServiceUrl;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl(booksServiceUrl).build();
    }

    public List<?> findAll() {
        return webClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .block();
    }

    public Object findByIsbn(String isbn) {
        return webClient.get()
                .uri("/{isbn}", isbn)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
