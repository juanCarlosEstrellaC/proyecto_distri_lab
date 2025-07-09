package com.programacion.distribuida.customers.health;

import com.programacion.distribuida.customers.repo.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;


@Readiness
@ApplicationScoped
public class ClientesReadinessHealth implements HealthCheck {

    @Inject
    CustomerRepository customerRepository;

    @Override
    public HealthCheckResponse call() {
        try {
            customerRepository.count(); // Verifica si responde
            return HealthCheckResponse.up("Customer listos para consultas");
        } catch (Exception e) {
            return HealthCheckResponse.down("Customer NO listos");
        }
    }
}
