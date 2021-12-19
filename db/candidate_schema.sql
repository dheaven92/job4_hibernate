CREATE TABLE vacancy_bank (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

INSERT INTO vacancy_bank (name) values ('Java Developer Vacancies');

CREATE TABLE vacancy (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    salary DOUBLE PRECISION,
    vacancy_bank_id INT NOT NULL REFERENCES vacancy_bank (id)
);

INSERT INTO vacancy (title, salary, vacancy_bank_id) VALUES ('Middle Developer', 220000, 1);
INSERT INTO vacancy (title, salary, vacancy_bank_id) VALUES ('Senior Developer', 350000, 1);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    experience INT,
    salary DOUBLE PRECISION,
    vacancy_bank_id INT NOT NULL REFERENCES vacancy_bank (id)
);

INSERT INTO candidate (name, experience, salary, vacancy_bank_id) VALUES ('Bob', 1, 100000, 1);
INSERT INTO candidate (name, experience, salary, vacancy_bank_id) VALUES ('Jim', 2, 150000, 1);
INSERT INTO candidate (name, experience, salary, vacancy_bank_id) VALUES ('Simon', 5, 300000, 1);