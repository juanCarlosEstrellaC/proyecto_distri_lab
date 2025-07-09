package com.programacio.distribuida.books.health;

import com.programacio.distribuida.books.repo.BooksRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;


@Readiness
@ApplicationScoped
public class LibrosReadinessHealth implements HealthCheck {

    @Inject
    BooksRepository booksRepository;

    @Override
    public HealthCheckResponse call() {
        try {
            booksRepository.count(); // Verifica si responde
            return HealthCheckResponse.up("Books listos para consultas");
        } catch (Exception e) {
            return HealthCheckResponse.down("Books NO listos");
        }
    }
}
