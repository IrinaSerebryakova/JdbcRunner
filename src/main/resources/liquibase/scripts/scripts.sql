-- liquibase formatted sql

-- changeset IrinaSerebryakova:1
CREATE TABLE IF NOT EXISTS passenger
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name  VARCHAR NOT NULL

);

-- changeset IrinaSerebryakova:2
CREATE TABLE IF NOT EXISTS train
(
    id         BIGSERIAL PRIMARY KEY,
    number     VARCHAR,
    train_name VARCHAR,
    town_from  VARCHAR,
    time_out   TIMESTAMP,
    town_to    VARCHAR,
    time_in    TIMESTAMP
);

-- changeset IrinaSerebryakova:3
CREATE TABLE IF NOT EXISTS town
(
    id        BIGSERIAL,
    town_name VARCHAR UNIQUE
);

-- changeset IrinaSerebryakova:4
CREATE TABLE IF NOT EXISTS pass_in_trip
(
    id           BIGSERIAL PRIMARY KEY,
    pass_name    VARCHAR,
    train_number VARCHAR,
    seat_number  INTEGER
);

-- changeset IrinaSerebryakova:5
INSERT INTO train
(number, train_name, town_from, time_out, town_to, time_in)
VALUES ('751A', 'Сапсан', 'Санкт-Петербург', '2025-05-09 05:30:00', 'Москва', '2025-05-09 09:20:00'),
       ('723Р', 'Ласточка', 'Санкт-Петербург', '2025-05-09 05:35:00', 'Москва', '2025-05-09 09:20:00'),
       ('741У', 'Аврора', 'Санкт-Петербург', '2025-05-09 06:00:00', 'Москва', '2025-05-09 11:30:00'),
       ('747А', 'Экспресс', 'Санкт-Петербург', '2025-05-09 06:50:00', 'Москва', '2025-05-09 11:00:00');

-- changeset IrinaSerebryakova:6
INSERT INTO passenger (first_name, last_name)
VALUES ('Дмитрий', 'Петров'),
       ('Светлана', 'Коняева'),
       ('Борис', 'Светлаков'),
       ('Елена', 'Чеснокова');

-- changeset IrinaSerebryakova:7
INSERT INTO town(town_name)
VALUES ('Москва'),
       ('Санкт-Петербург'),
       ('Архангельск'),
       ('Калининград');

-- changeset IrinaSerebryakova:8
INSERT INTO pass_in_trip (pass_name, train_number, seat_number)
VALUES ('Дмитрий Петров', '741У', 42),
       ('Светлана Коняева', '723Р', 53),
       ('Борис Светлаков', '751A', 96),
       ('Елена Чеснокова', '747А', 21);

