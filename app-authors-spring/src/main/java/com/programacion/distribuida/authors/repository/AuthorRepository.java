package com.programacion.distribuida.authors.repository;

import com.programacion.distribuida.authors.entity.Author;
import com.programacion.distribuida.authors.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    
    @Query("SELECT ba.author FROM BookAuthor ba WHERE ba.id.bookIsbn = :isbn")
    List<Author> findByBookIsbn(@Param("isbn") String isbn);
}
