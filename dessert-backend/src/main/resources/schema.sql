CREATE DATABASE IF NOT EXISTS dessert_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dessert_db;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色：admin/user',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

DROP TABLE IF EXISTS category;
CREATE TABLE category (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    description VARCHAR(255) COMMENT '分类描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

DROP TABLE IF EXISTS dessert;
CREATE TABLE dessert (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '甜点ID',
    image VARCHAR(255) COMMENT '图片路径',
    name VARCHAR(100) NOT NULL COMMENT '甜点名称',
    category_id INT NULL COMMENT '所属分类ID',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    description TEXT COMMENT '甜点描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布日期',
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='甜点表';

DROP TABLE IF EXISTS login_record;
CREATE TABLE login_record (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    ip_address VARCHAR(50) COMMENT '登录IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录记录表';

INSERT INTO category (name, description) VALUES 
('蛋糕', '各种美味蛋糕'),
('饼干', '香脆可口的饼干'),
('冰淇淋', '清凉解暑的冰淇淋'),
('巧克力', '香浓丝滑的巧克力'),
('布丁', '嫩滑香甜的布丁');

INSERT INTO user (username, password, email, phone, role) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'admin@example.com', '13800138000', 'admin'),
('user', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'user@example.com', '13900139000', 'user');
