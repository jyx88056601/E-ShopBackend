START TRANSACTION;

INSERT INTO users (email, is_active, last_updated_date, password, phone_number, registration_time, role, username)
VALUES
    ('admin@example.com', 1, NOW(), '$2a$10$WQL3pUIAGL79FptDbvP.OOZE9R3m.8eNtd0ZrtCKGVYYcB7cWPrhq', '1234567890', NOW(), 'ROLE_ADMIN', 'admin'),
    ('buyer@example.com', 1, NOW(), '$2a$10$KrGTFDteT9WQsDo48DN/Re.BD8o0U9aMD/j10xEbyhJAk7pLcToyu', '0987654321', NOW(), 'ROLE_BUYER', 'buyer'),
    ('seller@example.com', 1, NOW(), '$2a$10$VeluB.kqjpilWbXovIm1D.sACwSf5QgVxHvQGUYvMZe4bXONnidYW', '1122334455', NOW(), 'ROLE_SELLER', 'seller');
 
COMMIT;