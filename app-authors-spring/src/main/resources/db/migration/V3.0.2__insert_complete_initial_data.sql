-- Tabla authors
INSERT INTO public.authors (id, name, version) VALUES
                                                   (1, 'Gabriel García Márquez', 1),
                                                   (2, 'Isabel Allende', 1),
                                                   (3, 'J.K. Rowling', 1);

-- Tabla books
INSERT INTO public.books (isbn, title, price, version) VALUES
                                                           ('100', 'Cien años de soledad', 199.99, 1),
                                                           ('101', 'La casa de los espíritus', 149.50, 1),
                                                           ('102', 'Harry Potter y la piedra filosofal', 120.00, 1);

-- Tabla books_authors (el libro 100 tiene dos autores)
INSERT INTO public.books_authors (books_isbn, authors_id) VALUES
                                                              ('100', 1),
                                                              ('100', 2),
                                                              ('101', 2),
                                                              ('102', 3);

-- Tabla inventory
INSERT INTO public.inventory (isbn, sold, supplied) VALUES
                                                        ('100', 10, 50),
                                                        ('101', 5, 30),
                                                        ('102', 20, 100);

-- Tabla customers
INSERT INTO public.customers (id, name, email, version) VALUES
                                                            (1, 'Juan Pérez', 'juan.perez@email.com', 1),
                                                            (2, 'Ana Gómez', 'ana.gomez@email.com', 1);

-- Tabla purchase_orders
INSERT INTO public.purchase_orders (id, customer_id, total, status, placed_on, delivered_on) VALUES
                                                                                                 (1, 1, 349, 1, '2024-06-01 10:00:00', '2024-06-03 15:00:00'),
                                                                                                 (2, 2, 120, 0, '2024-06-02 12:30:00', NULL);

-- Tabla line_items
INSERT INTO public.line_items (id, order_id, quantity, isbn) VALUES
                                                                 (1, 1, 1, '100'),
                                                                 (2, 1, 1, '101'),
                                                                 (3, 2, 1, '102');
