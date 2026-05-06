
ALTER TABLE products
ADD COLUMN category_id BIGINT;

ALTER TABLE products
ADD CONSTRAINT fk_products_category
FOREIGN KEY (category_id)
REFERENCES categories(id);

UPDATE products
SET category_id = 1
WHERE name = 'Milk';