CREATE TABLE admin
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255)
);




CREATE TABLE IF NOT EXISTS customer
(
    pid           SERIAL PRIMARY KEY,
    name          VARCHAR(255),
    surname       VARCHAR(255),
    birth_date     DATE,
    email         VARCHAR(255) UNIQUE,
    phone         VARCHAR(50),
    address       VARCHAR(255),
    credit_rating INT
);

CREATE TYPE marital_status AS ENUM ('SINGLE', 'MARRIED', 'DIVORCED', 'WIDOWED');

CREATE TABLE financial_info
(
    id                  BIGSERIAL PRIMARY KEY,
    monthly_income      DECIMAL(15, 2),
    monthly_obligations DECIMAL(15, 2),
    marital_status      VARCHAR(255),
    employment_status   VARCHAR(255),
    employment_term     INT,
    dependants          INT
);




CREATE TABLE IF NOT EXISTS application
(
    id                BIGSERIAL PRIMARY KEY,
    financial_info_id BIGINT,
    customer_id       BIGINT,
    manager_id        BIGINT,
    price             NUMERIC(15, 2),
    down_payment      NUMERIC(15, 2),
    residual_value    INT,
    year_period       INT,
    interest_rate     DOUBLE PRECISION,
    status            VARCHAR(50),
    created_at        DATE,
    updated_at        DATE,
    monthly_payment   NUMERIC(15, 2),
    FOREIGN KEY (financial_info_id) REFERENCES financial_info(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customer(pid) ON DELETE CASCADE,
    FOREIGN KEY (manager_id) REFERENCES admin(id) ON DELETE SET NULL
);

CREATE TABLE vehicle_details
(
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT,
    brand VARCHAR(255),
    model VARCHAR(255),
    year INT,
    fuel VARCHAR(255),
    emission_start INT,
    emission_end INT,
    FOREIGN KEY (application_id) REFERENCES application (id)
);