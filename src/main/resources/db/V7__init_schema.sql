CREATE INDEX idx_room_owner ON rooms(owner_id);
CREATE INDEX idx_room_branch ON rooms(branch_id);

CREATE INDEX idx_bed_room ON beds(room_id);
CREATE INDEX idx_bed_owner ON beds(owner_id);

CREATE INDEX idx_pricing_room ON room_pricing(room_id);
CREATE INDEX idx_pricing_owner ON room_pricing(owner_id);