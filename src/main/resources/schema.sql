-- create database if not exists, special syntax for postgresql
-- since postgresql does not support IF NOT EXISTS for CREATE DATABASE
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'bookstore') THEN
            CREATE DATABASE bookstore;
        END IF;
    END
$$;

-- create app tables

CREATE TABLE IF NOT EXISTS app_user
(
    id         bigserial    NOT NULL, -- bigserial is auto incrementing bigint
    username   varchar(255) NOT NULL,
    email      varchar(255) NOT NULL,
    password   varchar(255) NOT NULL,
    role       varchar(255) NOT NULL,
    created_at timestamp    NOT NULL,
    updated_at timestamp    NOT NULL,
    deleted_at timestamp DEFAULT NULL,

    PRIMARY KEY (id)
);

-- make email and username unique for app_user table
ALTER TABLE app_user
    ADD CONSTRAINT unique_email UNIQUE (email),
    ADD CONSTRAINT unique_username UNIQUE (username);

CREATE TABLE IF NOT EXISTS book_category
(
    id          bigserial    NOT NULL,
    name        varchar(255) NOT NULL,
    description text         NOT NULL,
    created_at  timestamp    NOT NULL,
    updated_at  timestamp    NOT NULL,
    deleted_at  timestamp DEFAULT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS book
(
    id               bigserial    NOT NULL,
    title            varchar(255) NOT NULL,
    author           varchar(255) NOT NULL,
    description      text         NOT NULL,
    price            numeric      NOT NULL,
    image_url        varchar(255) NOT NULL,
    quantity         int          NOT NULL,
    language         varchar(255) NOT NULL,
    publisher        varchar(255) NOT NULL,
    publication_date date         NOT NULL,
    isbn10           varchar(255) NOT NULL,
    isbn13           varchar(255) NOT NULL,
    dimensions       varchar(255) NOT NULL,
    average_rating   numeric      NOT NULL DEFAULT 0,
    category_id      bigint       NOT NULL,
    created_at       timestamp    NOT NULL,
    updated_at       timestamp    NOT NULL,
    deleted_at       timestamp             DEFAULT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES book_category (id)
);

CREATE TABLE IF NOT EXISTS book_review
(
    id         bigserial NOT NULL,
    book_id    bigint    NOT NULL,
    user_id    bigint    NOT NULL,
    rating     numeric   NOT NULL,
    comment    text      NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    deleted_at timestamp DEFAULT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_book_id FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE IF NOT EXISTS shopping_cart
(
    id         bigserial NOT NULL,
    user_id    bigint    NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    deleted_at timestamp DEFAULT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE IF NOT EXISTS shopping_cart_item
(
    id               bigserial NOT NULL,
    shopping_cart_id bigint    NOT NULL,
    book_id          bigint    NOT NULL,
    quantity         int       NOT NULL,
    created_at       timestamp NOT NULL,
    updated_at       timestamp NOT NULL,
    deleted_at       timestamp DEFAULT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_shopping_cart_id FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart (id),
    CONSTRAINT fk_book_id FOREIGN KEY (book_id) REFERENCES book (id)
);

CREATE TABLE IF NOT EXISTS order_info
(
    id               bigserial    NOT NULL,
    user_id          bigint       NOT NULL,
    total_price      numeric      NOT NULL,
    status           varchar(255) NOT NULL,
    shipping_address text         NOT NULL,
    delivered_at     timestamp    NOT NULL,
    created_at       timestamp    NOT NULL,
    updated_at       timestamp    NOT NULL,
    deleted_at       timestamp DEFAULT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE IF NOT EXISTS order_item
(
    id         bigserial NOT NULL,
    order_id   bigint    NOT NULL,
    book_id    bigint    NOT NULL,
    quantity   int       NOT NULL,
    price      numeric   NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    deleted_at timestamp DEFAULT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES order_info (id),
    CONSTRAINT fk_book_id FOREIGN KEY (book_id) REFERENCES book (id)
);