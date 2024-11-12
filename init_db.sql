-- Tạo database nếu chưa tồn tại
CREATE DATABASE IF NOT EXISTS assignment3_db;

-- Sử dụng database vừa tạo
USE assignment3_db;

-- Tạo bảng "users"
CREATE TABLE IF NOT EXISTS user_account (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- Khóa chính
    username VARCHAR(50),  -- Cột username, không được null và phải là duy nhất
    email VARCHAR(100) NOT NULL UNIQUE,    -- Cột email, không được null và phải là duy nhất
    password VARCHAR(255) NOT NULL,  -- Cột password, không được null
    access_token VARCHAR(255),  -- Cột access_token để lưu JWT hoặc token truy cập
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Cột thời gian tạo, mặc định là thời gian hiện tại
);