package com.programacion.distribuida.authors.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class BookAuthorId {

    @Column(name = "books_isbn")
    private String bookIsbn;

    @Column(name = "authors_id")
    private Integer authorId;
}
