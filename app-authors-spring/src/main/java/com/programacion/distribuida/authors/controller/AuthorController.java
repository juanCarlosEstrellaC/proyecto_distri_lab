package com.programacion.distribuida.authors.controller;

import com.programacion.distribuida.authors.entity.Author;
import com.programacion.distribuida.authors.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @Value("${server.port}")
    private Integer appPort;

    // Simulación de errores
    private final AtomicInteger index = new AtomicInteger();

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable Integer id) {
        return authorService.findById(id)
                .map(author -> ResponseEntity.ok(author))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Author> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/find/{isbn}")
    public List<Author> findByBook(@PathVariable String isbn) {
        // Simulación de errores para pruebas. De 5 intentos, 4 son falla y 1 éxito.
        /*
        int valor = index.getAndIncrement();
        if (valor % 5 != 0) {
            String msg = String.format("Intento %d, generando error", valor);
            System.out.println("Author ************************ " + msg);
            throw new RuntimeException(msg);
        }
        */

        var ret = authorService.findByBookIsbn(isbn);

        // Para devolver el nombre del autor junto al puerto, sin modificar el objeto original
        return ret.stream().map(obj -> {
            var copia = new Author();
            copia.setId(obj.getId());
            copia.setName(String.format("%s (%d)", obj.getName(), appPort));
            copia.setVersion(obj.getVersion());
            return copia;
        }).toList();

        // Para no agregar el puerto al nombre del autor
        // return ret;
    }

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {
        try {
            Author savedAuthor = authorService.save(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Integer id, @RequestBody Author author) {
        return authorService.update(id, author)
                .map(updatedAuthor -> ResponseEntity.ok(updatedAuthor))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Author> delete(@PathVariable Integer id) {
        var author = authorService.findById(id);
        if (author.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        boolean deleted = authorService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok(author.get());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
