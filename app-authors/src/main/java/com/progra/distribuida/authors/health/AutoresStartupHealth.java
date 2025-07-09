package com.progra.distribuida.authors.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Startup;


@Startup
@ApplicationScoped
public class AutoresStartupHealth implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("Autores arrancaron correctamente");
    }
}
