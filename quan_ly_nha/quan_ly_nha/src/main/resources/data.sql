-- Tạo database (nếu chưa được tạo tự động bởi Spring Boot)
CREATE DATABASE IF NOT EXISTS restaurant_management_dev;
USE restaurant_management_dev;

-- Khởi tạo bảng roles và dữ liệu mặc định
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_MANAGER'),
('ROLE_STAFF'),
('ROLE_CUSTOMER');

-- Tạo tài khoản admin mặc định
-- Mật khẩu được mã hóa bằng BCrypt: "admin123"
INSERT INTO users (username, email, password, full_name, phone) VALUES
('admin', 'admin@restaurant.com', '$2a$10$yfB.Q8JEuB9ZGW2fGhHKy.HzJAS5MNQqHE3zdtKH9nwKbqk/Eyh9K', 'System Admin', '0123456789');

-- Gán quyền admin cho tài khoản admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- Tạo dữ liệu mẫu cho danh mục món ăn
INSERT INTO categories (name, description) VALUES
('Món khai vị', 'Các món ăn nhẹ để khai vị'),
('Món chính', 'Các món ăn chính'),
('Món tráng miệng', 'Các món ngọt sau bữa ăn'),
('Đồ uống', 'Các loại thức uống');

-- Tạo dữ liệu mẫu cho menu
INSERT INTO menu_items (name, description, price, available, category_id) VALUES
('Salad Caesar', 'Salad rau xanh với sốt Caesar, bánh mì nướng và phô mai Parmesan', 85000, true, (SELECT id FROM categories WHERE name = 'Món khai vị')),
('Súp hải sản', 'Súp hải sản với tôm, mực và các loại hải sản khác', 95000, true, (SELECT id FROM categories WHERE name = 'Món khai vị')),
('Bò bít tết', 'Thịt bò Úc thượng hạng nướng với sốt tiêu đen và khoai tây chiên', 250000, true, (SELECT id FROM categories WHERE name = 'Món chính')),
('Mì Ý hải sản', 'Mì Ý với sốt cà chua và hải sản tươi', 180000, true, (SELECT id FROM categories WHERE name = 'Món chính')),
('Cá hồi áp chảo', 'Cá hồi Na Uy áp chảo với sốt chanh bơ và rau củ', 220000, true, (SELECT id FROM categories WHERE name = 'Món chính')),
('Bánh Tiramisu', 'Bánh Tiramisu truyền thống kiểu Ý', 65000, true, (SELECT id FROM categories WHERE name = 'Món tráng miệng')),
('Cheesecake', 'Bánh phô mai New York với mứt dâu', 70000, true, (SELECT id FROM categories WHERE name = 'Món tráng miệng')),
('Espresso', 'Cà phê Espresso đậm đà', 45000, true, (SELECT id FROM categories WHERE name = 'Đồ uống')),
('Nước ép cam', 'Nước ép cam tươi', 50000, true, (SELECT id FROM categories WHERE name = 'Đồ uống')),
('Cocktail Mojito', 'Cocktail Mojito truyền thống', 85000, true, (SELECT id FROM categories WHERE name = 'Đồ uống'));

-- Tạo dữ liệu mẫu cho bàn ăn
INSERT INTO restaurant_tables (table_number, capacity, occupied, status) VALUES
('A1', 2, false, 'AVAILABLE'),
('A2', 2, false, 'AVAILABLE'),
('A3', 2, false, 'AVAILABLE'),
('B1', 4, false, 'AVAILABLE'),
('B2', 4, false, 'AVAILABLE'),
('B3', 4, false, 'AVAILABLE'),
('C1', 6, false, 'AVAILABLE'),
('C2', 6, false, 'AVAILABLE'),
('VIP1', 8, false, 'AVAILABLE'),
('VIP2', 10, false, 'AVAILABLE');
-- File: src/main/resources/data.sql

-- Script này sẽ chạy tự động khi Spring Boot khởi động
-- Chèn các vai trò (roles) mặc định
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_MANAGER'),
('ROLE_STAFF'),
('ROLE_CUSTOMER')
    ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Chèn tài khoản admin mặc định nếu chưa tồn tại
-- Mật khẩu "admin123" được mã hóa bằng BCrypt
INSERT INTO users (username, email, password, full_name, phone)
SELECT 'admin', 'admin@restaurant.com', '$2a$10$yfB.Q8JEuB9ZGW2fGhHKy.HzJAS5MNQqHE3zdtKH9nwKbqk/Eyh9K', 'System Admin', '0123456789'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Gán quyền admin cho tài khoản admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);