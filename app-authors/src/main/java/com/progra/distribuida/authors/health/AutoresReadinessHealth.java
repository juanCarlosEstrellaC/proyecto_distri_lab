package com.progra.distribuida.authors.health;

import com.progra.distribuida.authors.repo.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;


@Readiness
@ApplicationScoped
public class AutoresReadinessHealth implements HealthCheck {

    @Inject
    AuthorRepository autorRepository;

    @Override
    public HealthCheckResponse call() {
        try {
            autorRepository.count(); // Verifica si responde
            return HealthCheckResponse.up("Autores listos para consultas");
        } catch (Exception e) {
            return HealthCheckResponse.down("Autores NO listos");
        }
    }
}
