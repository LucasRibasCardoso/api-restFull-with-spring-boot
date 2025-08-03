CREATE TABLE tb_books
(
    id          SERIAL PRIMARY KEY,
    author      TEXT           NOT NULL,
    launch_date DATE           NOT NULL,
    price       NUMERIC(65, 2) NOT NULL,
    title       TEXT           NOT NULL
);

