-- V1__Initialize_migration.sql
-- 创建Flyway初始化迁移脚本

-- 开始事务
START TRANSACTION;

-- 创建地址表
CREATE TABLE addresses (
                           address_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
                           house_number    VARCHAR(255) NULL,
                           building_number VARCHAR(255) NULL,
                           city            VARCHAR(255) NOT NULL,
                           community       VARCHAR(255) NULL,
                           country         VARCHAR(255) NULL,
                           district        VARCHAR(255) NULL,
                           phone_number    VARCHAR(255) NOT NULL,
                           postal_code     VARCHAR(255) NOT NULL,
                           province        VARCHAR(255) NOT NULL,
                           recipient_name  VARCHAR(255) NOT NULL,
                           street_name     VARCHAR(255) NOT NULL,
                           unit_number     VARCHAR(255) NULL,
                           user_id         BIGINT       NOT NULL
);

-- 创建用户表
CREATE TABLE users (
                       user_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email             VARCHAR(255) NOT NULL,
                       is_active         BIT NOT NULL,
                       last_updated_date DATETIME(6) NOT NULL,
                       password          VARCHAR(255) NOT NULL,
                       phone_number      VARCHAR(255) NOT NULL,
                       registration_time DATETIME(6) NOT NULL,
                       role              ENUM('ROLE_ADMIN', 'ROLE_BUYER', 'ROLE_SELLER', 'ROLE_USER') NOT NULL,
                       username          VARCHAR(255) NOT NULL,
                       CONSTRAINT unique_user_email UNIQUE (email),
                       CONSTRAINT unique_user_phone UNIQUE (phone_number),
                       CONSTRAINT unique_username UNIQUE (username)
);

-- 创建购物车表
CREATE TABLE carts (
                       user_id BIGINT NOT NULL PRIMARY KEY,
                       CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- 创建产品表
CREATE TABLE products (
                          product_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                          category         VARCHAR(255) NOT NULL,
                          created_time     DATETIME(6) NOT NULL,
                          description      TEXT NULL,
                          main_picture_url VARCHAR(255) NULL,
                          name             VARCHAR(255) NOT NULL,
                          owner_id         BIGINT NOT NULL,
                          price            DECIMAL(38, 2) NOT NULL,
                          stock            INT NOT NULL,
                          updated_time     DATETIME(6) NOT NULL
);

-- 创建产品图片表
CREATE TABLE product_images (
                                product_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                is_main          BIT NULL,
                                url              VARCHAR(255) NOT NULL,
                                product_id       BIGINT NULL,
                                CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- 创建订单表 (先不添加外键约束)
CREATE TABLE orders (
                        order_id           BINARY(16) NOT NULL PRIMARY KEY,
                        customer_id        BIGINT NOT NULL,
                        merchant_id        BIGINT NOT NULL,
                        order_created_time DATETIME(6) NOT NULL,
                        order_number       VARCHAR(255) NOT NULL,
                        order_status       ENUM('CANCELED', 'COMPLETED', 'PAID', 'SHIPPED', 'SHIPPING', 'UNPAID', 'PROCESSING') NOT NULL,
                        total_amount       DECIMAL(10, 2) NOT NULL,
                        payment_id         BIGINT NULL,
                        shipment_id        BIGINT NULL
);

-- 创建订单项目表
CREATE TABLE order_items (
                             order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             price         DECIMAL(38, 2) NOT NULL,
                             product_id    BIGINT NOT NULL,
                             product_name  VARCHAR(255) NOT NULL,
                             quantity      INT NOT NULL,
                             total_price   DECIMAL(38, 2) NOT NULL,
                             order_id      BINARY(16) NULL,
                             CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
                                 ON UPDATE SET NULL ON DELETE SET NULL
);

-- 创建索引
CREATE INDEX idx_order_id ON order_items (order_id);
CREATE INDEX idx_customer_id ON orders (customer_id);
CREATE INDEX idx_merchant_id ON orders (merchant_id);

-- 创建支付表
CREATE TABLE payments (
                          payment_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
                          transaction_id VARCHAR(255) NULL,
                          amount         DECIMAL(38, 2) NULL,
                          payment_date   DATETIME(6) NULL,
                          payment_method VARCHAR(255) NULL,
                          status         ENUM('CANCELLED', 'COMPLETED', 'DECLINED', 'EXPIRED', 'FAILED', 'PENDING', 'PROCESSING', 'REFUNDED', 'UNPAID') NOT NULL,
                          order_id       BINARY(16) NOT NULL,
                          CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
                              ON UPDATE CASCADE ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX idx_order_id ON payments (order_id);

-- 创建发货表
CREATE TABLE shipments (
                           shipment_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
                           shipped_date    DATETIME(6) NULL,
                           status          TINYINT NULL CHECK (status BETWEEN 0 AND 4),
                           tracking_number VARCHAR(255) NULL,
                           address_id      BIGINT NOT NULL,
                           order_id        BINARY(16) NOT NULL,
                           CONSTRAINT fk_shipments_address FOREIGN KEY (address_id) REFERENCES addresses (address_id)
                               ON UPDATE CASCADE ON DELETE CASCADE,
                           CONSTRAINT fk_shipments_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
                               ON UPDATE CASCADE ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX idx_order_id ON shipments (order_id);

-- 创建购物车项目表
CREATE TABLE cart_items (
                            cart_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            quantity     INT NOT NULL,
                            cart_id      BIGINT NOT NULL,
                            product_id   BIGINT NOT NULL,
                            CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products (product_id)
                                ON UPDATE CASCADE ON DELETE CASCADE,
                            CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts (user_id)
);

-- 创建索引
CREATE INDEX id_product ON cart_items (product_id);
CREATE INDEX idx_cart_id ON cart_items (cart_id);

-- 创建用户地址关联表
CREATE TABLE user_address_ids (
                                  user_id    BIGINT NOT NULL,
                                  address_id BIGINT NULL,
                                  CONSTRAINT fk_user_address_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- 创建用户产品关联表
CREATE TABLE user_product_ids (
                                  user_user_id BIGINT NOT NULL,
                                  product_ids  BIGINT NULL,
                                  CONSTRAINT fk_user_product_user FOREIGN KEY (user_user_id) REFERENCES users (user_id)
);

-- 添加剩余的外键约束（解决循环依赖问题）
ALTER TABLE orders
    ADD CONSTRAINT unique_order_shipment UNIQUE (shipment_id),
    ADD CONSTRAINT unique_order_payment UNIQUE (payment_id),
    ADD CONSTRAINT fk_orders_payment FOREIGN KEY (payment_id) REFERENCES payments (payment_id)
        ON DELETE CASCADE,
    ADD CONSTRAINT fk_orders_shipment FOREIGN KEY (shipment_id) REFERENCES shipments (shipment_id);

-- 提交事务
COMMIT;