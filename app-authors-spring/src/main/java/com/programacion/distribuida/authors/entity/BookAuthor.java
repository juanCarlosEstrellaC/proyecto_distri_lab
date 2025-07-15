package com.programacion.distribuida.authors.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "books_authors")
public class BookAuthor {

    @EmbeddedId
    private BookAuthorId id;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "authors_id", nullable = false)
    private Author author;
}
