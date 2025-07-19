package com.programacion.distribuida.customers.rest;

import com.programacion.distribuida.customers.db.Customer;
import com.programacion.distribuida.customers.dto.CustomerDto;
import com.programacion.distribuida.customers.repo.CustomerRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class CustomerRest {

    @Inject
    CustomerRepository repository;

    @Inject
    ModelMapper mapper;

    
    @GET
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = repository.listAll();
        return customers.stream()
                .map(customer -> mapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }


    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Integer id) {
        Customer customer = repository.findById(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer no encontrado con ID: " + id)
                    .build();
        }
        CustomerDto customerDto = mapper.map(customer, CustomerDto.class);
        return Response.ok(customerDto).build();
    }

  
    @POST
    public Response createCustomer(CustomerDto customerDto) {
        try {
            // Validar datos
            if (customerDto.getName() == null || customerDto.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El nombre es requerido")
                        .build();
            }
            
            if (customerDto.getEmail() == null || customerDto.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El email es requerido")
                        .build();
            }

            Customer customer = mapper.map(customerDto, Customer.class);
            customer.setId(null); // Asegurar que sea un nuevo registro
            customer.setVersion(1); // Versión inicial
            
            repository.persist(customer);
            
            CustomerDto createdCustomer = mapper.map(customer, CustomerDto.class);
            return Response.status(Response.Status.CREATED)
                    .entity(createdCustomer)
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear customer: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Integer id, CustomerDto customerDto) {
        try {
            Customer existingCustomer = repository.findById(id);
            if (existingCustomer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer no encontrado con ID: " + id)
                        .build();
            }

            // Validar datos
            if (customerDto.getName() == null || customerDto.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El nombre es requerido")
                        .build();
            }
            
            if (customerDto.getEmail() == null || customerDto.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El email es requerido")
                        .build();
            }

            // Actualizar campos
            existingCustomer.setName(customerDto.getName());
            existingCustomer.setEmail(customerDto.getEmail());
            
            existingCustomer.setVersion(existingCustomer.getVersion() + 1);

            repository.persist(existingCustomer);
            
            CustomerDto updatedCustomer = mapper.map(existingCustomer, CustomerDto.class);
            return Response.ok(updatedCustomer).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar customer: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Integer id) {
        try {
            Customer customer = repository.findById(id);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer no encontrado con ID: " + id)
                        .build();
            }

            repository.delete(customer);
            return Response.status(Response.Status.NO_CONTENT).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar customer: " + e.getMessage())
                    .build();
        }
    }
}
