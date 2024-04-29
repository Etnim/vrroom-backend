CREATE TABLE admin
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255)
);




CREATE TABLE IF NOT EXISTS customer
(
    personal_id     BIGINT PRIMARY KEY,
    name           VARCHAR(255),
    surname        VARCHAR(255),
    birth_date      DATE,
    email          VARCHAR(255) UNIQUE,
    phone          VARCHAR(50) UNIQUE,
    address        VARCHAR(255),
    credit_rating  INT
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
    id BIGSERIAL PRIMARY KEY,
    financial_info_id BIGINT,
    customer_id       BIGINT,
    manager_id        BIGINT,
    price             NUMERIC(15, 2),
    down_payment      NUMERIC(15, 2),
    residual_value    NUMERIC(15, 2),  -- Changed from INT to NUMERIC to match BigDecimal
    year_period       INT,
    interest_rate     DOUBLE PRECISION,
    status            VARCHAR(50),
    created_at        TIMESTAMP,       -- Changed from DATE to TIMESTAMP
    updated_at        TIMESTAMP,       -- Changed from DATE to TIMESTAMP
    monthly_payment   NUMERIC(15, 2),
    agreement_fee     NUMERIC(15, 2),  -- Added field to match Java class
    FOREIGN KEY (financial_info_id) REFERENCES financial_info(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customer(personal_id) ON DELETE CASCADE, -- Adjusted to match standard naming
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
CREATE TABLE application_status_history (
                                            id BIGSERIAL PRIMARY KEY,
                                            application_id BIGINT NOT NULL,
                                            status VARCHAR(255) NOT NULL,
                                            changed_by_manager_id BIGINT,
                                            changed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

                                            CONSTRAINT fk_application
                                                FOREIGN KEY (application_id)
                                                    REFERENCES application (id)
                                                    ON DELETE CASCADE,

                                            CONSTRAINT fk_manager
                                                FOREIGN KEY (changed_by_manager_id)
                                                    REFERENCES admin (id)
                                                    ON DELETE SET NULL
);

CREATE INDEX idx_application_status_history_application_id ON application_status_history (application_id);
CREATE INDEX idx_application_status_history_manager_id ON application_status_history (changed_by_manager_id);