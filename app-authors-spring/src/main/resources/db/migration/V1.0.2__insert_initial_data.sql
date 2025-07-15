-- Insertar datos iniciales en la tabla authors
INSERT INTO public.authors (id, name, version) VALUES
    (1, 'Gabriel García Márquez', 1),
    (2, 'Isabel Allende', 1),
    (3, 'J.K. Rowling', 1)
ON CONFLICT (id) DO NOTHING;

-- Insertar datos iniciales en la tabla books_authors
INSERT INTO public.books_authors (books_isbn, authors_id) VALUES
    ('100', 1),
    ('100', 2),
    ('101', 2),
    ('102', 3)
ON CONFLICT (books_isbn, authors_id) DO NOTHING;
