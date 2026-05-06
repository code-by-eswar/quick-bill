CREATE TABLE categories (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(100) UNIQUE NOT NULL,

    description TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO categories (
    name,
    description
)
VALUES
(
    'Groceries',
    'Daily grocery products'
),
(
    'Beverages',
    'Drinks and liquids'
);
select * from categories;
select * from categories;