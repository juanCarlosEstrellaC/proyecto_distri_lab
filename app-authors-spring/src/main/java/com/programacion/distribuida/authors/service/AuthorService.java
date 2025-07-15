package com.programacion.distribuida.authors.service;

import com.programacion.distribuida.authors.entity.Author;
import com.programacion.distribuida.authors.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Optional<Author> findById(Integer id) {
        return authorRepository.findById(id);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public List<Author> findByBookIsbn(String isbn) {
        return authorRepository.findByBookIsbn(isbn);
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Optional<Author> update(Integer id, Author authorUpdate) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorUpdate.getName());
                    author.setVersion(authorUpdate.getVersion());
                    return authorRepository.save(author);
                });
    }

    public boolean deleteById(Integer id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
