CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    active BOOLEAN NOT NULL,
    moodle_sync_enabled BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_products_name ON products (name);

INSERT INTO products (id, name, description, price, active, moodle_sync_enabled, created_at, updated_at) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Angular Foundations', 'Introductory Angular course product', 199.00, true, false, NOW(), NOW()),
    ('22222222-2222-2222-2222-222222222222', 'Spring Boot Hexagonal POC', 'Backend architecture workshop product', 249.00, false, true, NOW(), NOW());