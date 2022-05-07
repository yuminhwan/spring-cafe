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
    updated_at   datetime(6)  DEFAULT NULL,
    CONSTRAINT unq_product_name UNIQUE (product_name)
);

CREATE TABLE `order`
(
    order_id     bigint PRIMARY KEY AUTO_INCREMENT,
    email        VARCHAR(50)  NOT NULL,
    address      VARCHAR(100) NOT NULL,
    postcode     VARCHAR(10)  NOT NULL,
    order_status VARCHAR(50)  NOT NULL,
    created_at   datetime(6)  NOT NULL,
    updated_at   datetime(6)  NOT NULL
);

CREATE TABLE order_item
(
    id         bigint PRIMARY KEY AUTO_INCREMENT,
    order_id   bigint NOT NULL,
    product_id bigint NOT NULL,
    price      bigint NOT NULL,
    quantity   int    NOT NULL,
    created_at datetime(6) default CURRENT_TIMESTAMP(6),
    updated_at datetime(6) default CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES `order` (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_to_product Foreign Key (product_id) REFERENCES product (product_id)
);

CREATE INDEX order_id ON order_item (order_id);

INSERT INTO product(product_name, category, price, stock, description, created_at, updated_at)
VALUES ('americano', 'COFFEE', '2500', '100', 'americano', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6));
INSERT INTO product(product_name, category, price, stock, description, created_at, updated_at)
VALUES ('Latte', 'COFFEE', '2000', '100', 'Latte', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6));
