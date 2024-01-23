CREATE TABLE inventory_availability (
  id STRING(50) NOT NULL,
  sku_id STRING(100) NOT NULL,
  node_id STRING(100),
  product_key STRING(200),
  quantity_on_stock numeric,
  active BOOL,
  created_at timestamp,
  updated_at timestamp,
) PRIMARY KEY(id);

CREATE TABLE inventory_reservation (
  id STRING(50) NOT NULL,
  inventory_id STRING(50) NOT NULL,
  cart_key STRING(100),
  line_id STRING(100),
  reserved_quantity numeric,
  message STRING(200),
  created_at timestamp,
  updated_at timestamp,
) PRIMARY KEY(inventory_id, id),
  INTERLEAVE IN PARENT inventory_availability ON DELETE CASCADE;
