CREATE TABLE `t_users`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255),
    `first_name` VARCHAR(255),
    `last_name` VARCHAR(255),
    PRIMARY KEY (`id`)
);

CREATE TABLE `t_orders`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_number` VARCHAR(255),
    `sku_code` VARCHAR(255),
    `price` DECIMAL(19, 2),
    `quantity` INT,
    `user_id` BIGINT,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_user_order`
        FOREIGN KEY (`user_id`)
        REFERENCES `t_users` (`id`)
);