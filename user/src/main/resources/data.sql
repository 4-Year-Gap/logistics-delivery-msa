-- ALTER TABLE p_delivery_assignment MODIFY delivery_assignment_id VARCHAR(36);
-- INSERT INTO p_delivery_assignment (delivery_assignment_id, current_driver_index)
-- SELECT UUID(), 0
-- WHERE NOT EXISTS (SELECT * FROM p_delivery_assignment);

INSERT INTO p_delivery_assignment (current_driver_index)
SELECT 0
WHERE NOT EXISTS (SELECT * FROM p_delivery_assignment);