package com.programacion.distribuida.customers.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Startup;


@Startup
@ApplicationScoped
public class ClientesStartupHealth implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("Customers arrancaron correctamente");
    }
}
