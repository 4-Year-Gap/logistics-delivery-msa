CREATE TABLE IF NOT EXISTS p_delivery_assignment (
    delivery_assignment_id TINYINT AUTO_INCREMENT PRIMARY KEY,
    current_driver_index INT NOT NULL,
    driver_type VARCHAR(20) NOT NULL
);

INSERT INTO p_delivery_assignment (current_driver_index, driver_type)
SELECT 0, 'HUB'
WHERE NOT EXISTS (SELECT * FROM p_delivery_assignment WHERE driver_type = 'HUB');

INSERT INTO p_delivery_assignment (current_driver_index, driver_type)
SELECT 0, 'COMPANY'
WHERE NOT EXISTS (SELECT * FROM p_delivery_assignment WHERE driver_type = 'COMPANY');
