CREATE TABLE engine (
    id SERIAL PRIMARY KEY
);

CREATE TABLE car (
    id        SERIAL PRIMARY KEY,
    engine_id INT NOT NULL REFERENCES engine (id)
);

CREATE TABLE driver (
    id SERIAL PRIMARY KEY
);

CREATE TABLE history_owner (
    driver_id INT NOT NULL REFERENCES driver (id),
    car_id    INT NOT NULL REFERENCES car (id),
    PRIMARY KEY (driver_id, car_id)
);