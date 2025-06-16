package com.programacio.distribuida.books.clients;

import com.programacio.distribuida.books.dtos.AuthorDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.ArrayList;
import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(baseUri = "stork://authors-api")
public interface AuthorRestClient {

    @GET
    @Path("/find/{isbn}")
    @Retry(maxRetries = 2, delay = 0)
    @Fallback(fallbackMethod = "findByBookFallback")
    List<AuthorDto> findByBook(@PathParam("isbn") String isbn);

    default List<AuthorDto> findByBookFallback(String isbn) {
        var aut = new AuthorDto();
        aut.setId(0);
        aut.setName("Fallback Author -- No disponible--");

        return List.of(aut);
    }

}
