DROP TABLE if exists order_item;
DROP TABLE if exists product;
DROP TABLE if exists `order`;

CREATE TABLE product
(
    product_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(20) NOT NULL,
    category     VARCHAR(50) NOT NULL,
    price        BIGINT      NOT NULL,
    stock        BIGINT      NOT NULL,
    description  VARCHAR(100) DEFAULT NULL,
    created_at   datetime(6) NOT NULL,
    updated_at   datetime(6) NOT NULL,
    CONSTRAINT unq_product_name UNIQUE (product_name)
);

