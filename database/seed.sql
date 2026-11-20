-- Chỉ định rõ Database cần thêm dữ liệu
USE cafe_management_db;

-- ==============================================================================
-- 3. THÊM DỮ LIỆU MẪU (INSERT DATA)
-- ==============================================================================

-- 3.1 Thêm dữ liệu vào bảng Users (Mật khẩu đang được mã hóa BCrypt)
INSERT INTO Users (user_id, username, password_hash, role, full_name, phone, salary, is_active) VALUES
                                                                                                    (1, 'admin', '$2a$12$Oxv1pgXcaokh5PfCgYSisu6VPTXIrOK5Tr3XD.MzKp2akWZkj5Nk.', 'ADMIN', 'Quản trị viên hệ thống', '104240750', 15000000, 1),
                                                                                                    (2, 'staff1', '$2a$12$5kRvwhTRDo.2ql8R8n383u5NxWmZwTQ3Ob2LjMK52/4VylqGzTTJe', 'STAFF', 'Nhân viên quầy số 1', '104240022', 6000000, 1);

-- 3.2 Thêm dữ liệu vào bảng Customers
INSERT INTO Customers (customer_id, phone, full_name, points, created_at) VALUES
                                                                              (1, '104240047', 'Nguyễn Phúc Vinh', 1000, NULL),
                                                                              (2, '123456789', 'Alexander đệ tam', 0, NULL);

-- 3.3 Thêm dữ liệu vào bảng Categories
INSERT INTO Categories (category_id, name) VALUES
                                               (1, 'Cà Phê Truyền Thống'),
                                               (2, 'Cà Phê Pha Máy'),
                                               (3, 'Phindi');

-- 3.4 Thêm dữ liệu vào bảng Products
INSERT INTO Products (product_id, category_id, name, base_price, is_active) VALUES
                                                                                (1, 1, 'Cà Phê Đen', 15000, 1),
                                                                                (2, 1, 'Cà Phê Sữa', 15000, 1),
                                                                                (3, 1, 'Cà Phê Muối', 20000, 1),
                                                                                (4, 1, 'Cà Phê Trứng', 20000, 1),
                                                                                (5, 1, 'Cà Phê Cốt Dừa', 20000, 1),
                                                                                (6, 1, 'Bạc Xỉu', 15000, 1),
                                                                                (7, 2, 'Espresso', 35000, 1),
                                                                                (8, 2, 'Americano', 35000, 1),
                                                                                (9, 2, 'Cappuccino', 45000, 1),
                                                                                (10, 2, 'Latte', 45000, 1),
                                                                                (11, 2, 'Caramel Macchiato', 55000, 1),
                                                                                (12, 2, 'Mocha', 55000, 1),
                                                                                (13, 3, 'Phindi Kem Sữa', 40000, 1),
                                                                                (14, 3, 'Phindi Hạnh Nhân', 40000, 1),
                                                                                (15, 3, 'Phindi Choco', 40000, 1);