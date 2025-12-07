-- Inventory Service Schema

DROP TABLE IF EXISTS inventory;

CREATE TABLE inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    quantity INTEGER NOT NULL,
    reserved_quantity INTEGER NOT NULL DEFAULT 0,
    warehouse_location VARCHAR(200),
    reorder_level INTEGER DEFAULT 10,
    last_restocked TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_quantity_nonnegative CHECK (quantity >= 0),
    CONSTRAINT chk_reserved_nonnegative CHECK (reserved_quantity >= 0),
    CONSTRAINT chk_reorder_level_positive CHECK (reorder_level > 0)
);

CREATE INDEX idx_inventory_product_id ON inventory(product_id);
CREATE INDEX idx_inventory_quantity ON inventory(quantity);

