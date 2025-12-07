-- Order Service Seed Data

-- Sample Orders
INSERT INTO orders (user_id, total_amount, status, shipping_address, payment_method, created_at, updated_at) VALUES
(1, 1479.97, 'DELIVERED', '123 Main St, New York, NY 10001', 'Credit Card', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 79.98, 'SHIPPED', '456 Oak Ave, Los Angeles, CA 90001', 'PayPal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 449.98, 'PROCESSING', '123 Main St, New York, NY 10001', 'Credit Card', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 249.99, 'CONFIRMED', '789 Pine Rd, Chicago, IL 60601', 'Debit Card', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1299.99, 'PENDING', '456 Oak Ave, Los Angeles, CA 90001', 'PayPal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Order Items for Order 1
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, subtotal) VALUES
(1, 1, 'Laptop Pro 15', 1, 1299.99, 1299.99),
(1, 3, 'Mechanical Keyboard', 1, 149.99, 149.99),
(1, 2, 'Wireless Mouse', 1, 29.99, 29.99);

-- Order Items for Order 2
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, subtotal) VALUES
(2, 2, 'Wireless Mouse', 2, 29.99, 59.98),
(2, 9, 'Phone Case', 1, 19.99, 19.99);

-- Order Items for Order 3
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, subtotal) VALUES
(3, 4, '4K Monitor 27"', 1, 399.99, 399.99),
(3, 5, 'USB-C Hub', 1, 49.99, 49.99);

-- Order Items for Order 4
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, subtotal) VALUES
(4, 8, 'Headphones Pro', 1, 249.99, 249.99);

-- Order Items for Order 5
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, subtotal) VALUES
(5, 1, 'Laptop Pro 15', 1, 1299.99, 1299.99);

