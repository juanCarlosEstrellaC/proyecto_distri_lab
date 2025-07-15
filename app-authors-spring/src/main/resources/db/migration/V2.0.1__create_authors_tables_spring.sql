-- Tabla authors
CREATE TABLE IF NOT EXISTS public.authors (
    id serial PRIMARY KEY,
    name varchar(255),
    version integer
);

-- Tabla books_authors (tabla intermedia)
CREATE TABLE IF NOT EXISTS public.books_authors (
    books_isbn varchar(255) NOT NULL,
    authors_id integer NOT NULL,
    PRIMARY KEY (books_isbn, authors_id),
    FOREIGN KEY (authors_id) REFERENCES public.authors(id)
);
