package com.progra.distribuida.authors.rest;

import com.progra.distribuida.authors.db.Author;
import com.progra.distribuida.authors.repo.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class AuthorRest {

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    @Inject
    AuthorRepository authorRepository;

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {
        var obj = authorRepository.findByIdOptional(id);
        if (obj.isEmpty())  {
            // Si no se encuentra el autor, se devuelve un error 404
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(obj.get()).build();
    }

    @GET
    public List<Author> findAll() {
        return authorRepository.findAll().list();
    }

    // Simulación de errores
    AtomicInteger index = new AtomicInteger();

    @GET
    @Path("/find/{isbn}")
    public List<Author> findByBook(@PathParam("isbn") String isbn) {
        /*  Simulación de errores para pruebas. De 5 intentos, 4 son falla y 1 éxito.
        int valor = index.getAndIncrement();
        if (valor % 5 != 0) {
            String msg = String.format("Intento %d, generando error", valor);
            System.out.println("Author ************************ " + msg);
            throw new RuntimeException(msg);
        } */
        var ret = authorRepository.findByBook(isbn);

        // Para devolver el nombre del autor junto al puerto, sin modificar el objeto original y copiando los demás campos
        Config config = ConfigProvider.getConfig();
        var puerto = config.getValue("quarkus.http.port", Integer.class);

        return ret.stream().map(obj -> {
            var copia = new Author();
            copia.setId(obj.getId());
            copia.setName(String.format("%s (%d)", obj.getName(), puerto));
            copia.setVersion(obj.getVersion());
            return copia;
        }).toList();

        // para no agregar el puerto al nombre del autor, devolver la lista de autores sin modificarla
        //return ret;
    }

    @POST
    public Response create(Author author) {
        try {
            // Validar que el autor tenga un nombre
            if (author.getName() == null || author.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El nombre del autor es requerido y no puede estar vacío")
                    .build();
            }
            
            // Asegurar que el ID sea null para que se genere automáticamente
            author.setId(null);
            
            // Si version es null, establecerla en 1
            if (author.getVersion() == null) {
                author.setVersion(1);
            }
            
            authorRepository.persist(author);
            return Response.status(Response.Status.CREATED).entity(author).build();
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Author author) {
        var obj = authorRepository.findByIdOptional(id);
        if (obj.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        authorRepository.update(id, author);
        return Response.ok(obj.get()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        try {
            var obj = authorRepository.findByIdOptional(id);
            if (obj.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            // Primero eliminar las relaciones en books_authors si existen
            authorRepository.delete("DELETE FROM BookAuthor ba WHERE ba.author.id = ?1", id);
            
            // Luego eliminar el autor
            authorRepository.deleteById(id);
            
            return Response.ok(obj.get()).build();
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}