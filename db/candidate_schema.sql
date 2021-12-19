CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    experience INT,
    salary DOUBLE PRECISION
);

INSERT INTO candidate (name, experience, salary) VALUES ('Bob', 1, 100000);
INSERT INTO candidate (name, experience, salary) VALUES ('Jim', 2, 150000);
INSERT INTO candidate (name, experience, salary) VALUES ('Simon', 5, 300000);