-- User Service Seed Data

INSERT INTO users (username, email, first_name, last_name, phone_number, address, city, state, postal_code, country, role, active, created_at, updated_at, last_login) VALUES
('john.doe', 'john.doe@example.com', 'John', 'Doe', '5551234567', '123 Main St', 'New York', 'NY', '10001', 'USA', 'CUSTOMER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jane.smith', 'jane.smith@example.com', 'Jane', 'Smith', '5552345678', '456 Oak Ave', 'Los Angeles', 'CA', '90001', 'USA', 'CUSTOMER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('bob.johnson', 'bob.johnson@example.com', 'Bob', 'Johnson', '5553456789', '789 Pine Rd', 'Chicago', 'IL', '60601', 'USA', 'CUSTOMER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('admin', 'admin@example.com', 'Admin', 'User', '5554567890', '321 Admin Blvd', 'Seattle', 'WA', '98101', 'USA', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('seller1', 'seller1@example.com', 'Sarah', 'Williams', '5555678901', '654 Seller St', 'Austin', 'TX', '78701', 'USA', 'SELLER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('alice.brown', 'alice.brown@example.com', 'Alice', 'Brown', '5556789012', '987 Elm Dr', 'Boston', 'MA', '02101', 'USA', 'CUSTOMER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('charlie.davis', 'charlie.davis@example.com', 'Charlie', 'Davis', '5557890123', '147 Maple Ln', 'Denver', 'CO', '80201', 'USA', 'CUSTOMER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('seller2', 'seller2@example.com', 'Mike', 'Wilson', '5558901234', '258 Commerce Rd', 'Miami', 'FL', '33101', 'USA', 'SELLER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

