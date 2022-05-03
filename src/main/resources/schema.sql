CREATE TABLE product
(
    product_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(20) NOT NULL,
    category     VARCHAR(50) NOT NULL,
    price        BIGINT      NOT NULL,
    stock        BIGINT      NOT NULL,
    description  VARCHAR(500) DEFAULT NULL,
    created_at   datetime(6) NOT NULL,
    updated_at   datetime(6)  DEFAULT NULL,
    CONSTRAINT unq_product_name UNIQUE (product_name)
);
