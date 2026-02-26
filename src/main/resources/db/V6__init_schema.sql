ALTER TABLE rooms
    ADD CONSTRAINT chk_room_capacity_positive CHECK (capacity > 0);

ALTER TABLE room_pricing
    ADD COLUMN occupancy_count INTEGER NOT NULL;

ALTER TABLE room_pricing
    ADD CONSTRAINT chk_occupancy_positive CHECK (occupancy_count > 0);

ALTER TABLE room_pricing
    ADD CONSTRAINT chk_price_positive CHECK (price_per_bed > 0);