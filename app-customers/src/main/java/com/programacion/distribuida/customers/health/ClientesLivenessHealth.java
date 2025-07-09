package com.programacion.distribuida.customers.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;


@Liveness
@ApplicationScoped
public class ClientesLivenessHealth implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("Modulo Clientes está vivo.");
    }
}
