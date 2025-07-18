package com.programacio.distribuida.books.rest;

import com.programacio.distribuida.books.clients.AuthorRestClient;
import com.programacio.distribuida.books.db.Book;
import com.programacio.distribuida.books.dtos.AuthorDto;
import com.programacio.distribuida.books.dtos.BookDto;
import com.programacio.distribuida.books.repo.BooksRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.stork.Stork;
import io.smallrye.stork.api.Service;
import io.smallrye.stork.api.ServiceInstance;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class BookRest {

    @Inject
    BooksRepository booksRepository;

    @Inject
    ModelMapper mapper;

    @Inject
    @RestClient
    AuthorRestClient client;


    @GET
    @Path("/{isbn}")
    public Response findByIsbn(@PathParam("isbn") String isbn) {
        var stork = Stork.getInstance();

        //--listar servicios
        Map<String, Service> services = stork.getServices();

        services.entrySet()
                .stream()
                .forEach(it -> {
                    System.out.println(it.getKey());

                    Multi<ServiceInstance> instances = it.getValue()
                            .getInstances()
                            .onItem()
                            .transformToMulti(items -> Multi.createFrom().iterable(items));

                    instances.subscribe()
                            .with(item->{
                                System.out.println("  " + item.getHost() + ":" + item.getPort());
                            });
                });

        //--seleccionar una instancia
        Service service = stork.getService("authors-api");
        Uni<ServiceInstance> instance = service.selectInstance();
        instance
                .subscribe()
                .with(inst -> {
                    System.out.println("**Instancia seleccionada: " + inst.getHost() + ":" + inst.getPort());
                });


        BookDto ret = new BookDto();

        //1. buscar el libro
        var obj = booksRepository.findByIdOptional(isbn);
        if (obj.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        }
        mapper.map(obj.get(), ret);

        var authors = client.findByBook(isbn)
                .stream()
                .map(AuthorDto::getName)
                .toList();

        ret.setAuthors(authors);

        return Response.ok(ret)
                .build();
    }



    @GET
    public List<BookDto> findAll() {
        return booksRepository.streamAll().map(book -> {
            BookDto bookDto = new BookDto();
            mapper.map(book, bookDto);
            var authors = client.findByBook(book.getIsbn())
                            .stream()
                            .map(AuthorDto::getName)
                            .toList();

            bookDto.setAuthors(authors.stream().toList());
            return bookDto;
        }).toList();
    }


    private BookDto generarBookDto(Book libro) {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn(libro.getIsbn());
        bookDto.setTitle(libro.getTitle());
        bookDto.setPrice(libro.getPrice());

        // 2. Buscar el inventario y agregarlo al DTO
        var inventary = libro.getInventory();
        if (inventary != null) {
            bookDto.setInventaySold(inventary.getSold());
            bookDto.setInventaySupplied(inventary.getSupplied());
        }

        var miListadeAutores = client.findByBook(libro.getIsbn()).stream().map(AuthorDto::getName).toList();
        bookDto.setAuthors(miListadeAutores);

        return bookDto;
    }

    @POST
    public Response create(Book book) {
        try {
            // Validar que el libro tenga ISBN y título
            if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El ISBN del libro es requerido y no puede estar vacío")
                    .build();
            }
            
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El título del libro es requerido y no puede estar vacío")
                    .build();
            }
            
            // Verificar que el ISBN no exista ya
            var existing = booksRepository.findByIdOptional(book.getIsbn());
            if (existing.isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                    .entity("Ya existe un libro con este ISBN")
                    .build();
            }
            
            // Si version es null, establecerla en 1
            if (book.getVersion() == null) {
                book.setVersion(1);
            }
            
            booksRepository.persist(book);
            return Response.status(Response.Status.CREATED).entity(book).build();
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{isbn}")
    public Response update(@PathParam("isbn") String isbn, Book book) {
        try {
            var obj = booksRepository.findByIdOptional(isbn);
            if (obj.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            // Validar que el libro tenga título
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El título del libro es requerido y no puede estar vacío")
                    .build();
            }
            
            booksRepository.update(isbn, book);
            return Response.ok(obj.get()).build();
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{isbn}")
    public Response delete(@PathParam("isbn") String isbn) {
        try {
            var obj = booksRepository.findByIdOptional(isbn);
            if (obj.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            booksRepository.deleteById(isbn);
            return Response.ok(obj.get()).build();
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}