-- Script para insertar customers de ejemplo si no existen
INSERT INTO customers (name, email, version) 
SELECT 'Juan Pérez', 'juan@example.com', 1
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'juan@example.com');

INSERT INTO customers (name, email, version) 
SELECT 'María García', 'maria@example.com', 1
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'maria@example.com');

INSERT INTO customers (name, email, version) 
SELECT 'Carlos López', 'carlos@example.com', 1
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'carlos@example.com');

INSERT INTO customers (name, email, version) 
SELECT 'Ana Martínez', 'ana@example.com', 1
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'ana@example.com');

INSERT INTO customers (name, email, version) 
SELECT 'Pedro Rodríguez', 'pedro@example.com', 1
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'pedro@example.com');
