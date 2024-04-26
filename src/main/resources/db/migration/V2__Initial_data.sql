INSERT INTO admin (id, name, surname)
VALUES
    (1, 'John', 'Doe'),
    (2, 'Jane', 'Roe'),
    (3, 'Alice', 'Smith'),
    (4, 'Bob', 'Brown'),
    (5, 'Carol', 'Jones'),
    (6, 'David', 'White'),
    (7, 'Eve', 'Black'),
    (8, 'Frank', 'Green'),
    (9, 'Grace', 'Taylor');


INSERT INTO customer (personal_id, name, surname, birth_date, email, phone, address, credit_rating)
VALUES
    (1, 'John', 'Doe', '1980-01-15', 'john.doe@example.com', '555-0101', '123 Elm St, Springfield', 750),
    (2, 'Jane', 'Smith', '1992-04-23', 'jane.smith@example.com', '555-0202', '456 Oak St, Anytown', 800),
    (3, 'Alice', 'Johnson', '1985-07-19', 'alice.johnson@example.com', '555-0303', '789 Pine St, Metropolis', 680),
    (4, 'Bob', 'Brown', '1990-12-11', 'bob.brown@example.com', '555-0404', '101 Maple St, Smallville', 720),
    (5, 'Charlie', 'Davis', '1975-05-30', 'charlie.davis@example.com', '555-0505', '102 Birch St, Rivertown', 695),
    (6, 'Ella', 'Fitzgerald', '1999-03-28', 'ella.fitzgerald@example.com', '555-0606', '103 Cedar St, Lakeview', 815),
    (7, 'Miles', 'Davis', '1988-09-16', 'miles.davis@example.com', '555-0707', '201 Spruce St, Hilltown', 640),
    (8, 'Louis', 'Armstrong', '1982-02-01', 'louis.armstrong@example.com', '555-0808', '202 Redwood St, Coastside', 710),
    (9, 'Duke', 'Ellington', '1993-11-21', 'duke.ellington@example.com', '555-0909', '301 Willow St, Seaside', 730),
    (10, 'Charlie', 'Parker', '1987-08-29', 'charlie.parker@example.com', '555-1010', '401 Palm St, Sunnyside', 780),
    (11, 'Billie', 'Holiday', '1994-06-17', 'billie.holiday@example.com', '555-1111', '501 Cherry St, Eastside', 690),
    (12, 'Diana', 'Krall', '1981-03-15', 'diana.krall@example.com', '555-1212', '601 Hickory St, Westside', 820),
    (13, 'Nina', 'Simone', '1990-02-21', 'nina.simone@example.com', '555-1313', '701 Pine St, Northtown', 760),
    (14, 'Norah', 'Jones', '1986-05-30', 'norah.jones@example.com', '555-1414', '801 Fir St, Southtown', 740),
    (15, 'Oscar', 'Peterson', '1991-07-15', 'oscar.peterson@example.com', '555-1515', '901 Spruce St, Centerville', 770);



INSERT INTO financial_info (
    id,
    monthly_income,
    monthly_obligations,
    marital_status,
    employment_status,
    employment_term,
    dependants
) VALUES
      (1, 5000.00, 200.00, 'SINGLE', 'FULL_TIME', 5, 0),
      (2, 4500.00, 150.00, 'MARRIED', 'FULL_TIME', 10, 2),
      (3, 6200.00, 400.00, 'SINGLE', 'FULL_TIME', 2, 1),
      (4, 3300.00, 600.00, 'DIVORCED', 'UNEMPLOYED', 0, 1),
      (5, 4700.00, 350.00, 'MARRIED', 'FULL_TIME', 8, 3),
      (6, 3900.00, 500.00, 'SINGLE', 'SELF_EMPLOYED', 4, 0),
      (7, 5800.00, 300.00, 'MARRIED', 'FULL_TIME', 6, 1),
      (8, 4700.00, 200.00, 'DIVORCED', 'FULL_TIME', 3, 2),
      (9, 5600.00, 450.00, 'MARRIED', 'FULL_TIME', 7, 2),
      (10, 4200.00, 250.00, 'SINGLE', 'UNEMPLOYED', 0, 1),
      (11, 4500.00, 450.00, 'DIVORCED', 'FULL_TIME', 12, 0),
      (12, 3200.00, 800.00, 'MARRIED', 'SELF_EMPLOYED', 15, 1),
      (13, 5400.00, 250.00, 'SINGLE', 'FULL_TIME', 2, 0),
      (14, 6100.00, 400.00, 'MARRIED', 'FULL_TIME', 9, 2),
      (15, 4900.00, 300.00, 'COHABITEE', 'UNEMPLOYED', 0, 1);


INSERT INTO vehicle_details (application_id, brand, model, year, fuel, emission_start, emission_end)
VALUES (1, 'Toyota', 'Corolla', 2019, 'PETROL', 120, 120),
       (1, 'Honda', 'Civic', 2018, 'HYBRID', 110, 110),
       (2, 'Ford', 'Fusion', 2020, 'PETROL', 150, 150),
       (2, 'Chevrolet', 'Impala', 2021, 'DIESEL', 130, 130),
       (3, 'Nissan', 'Altima', 2022, 'PETROL', 125, 125),
       (3, 'Hyundai', 'Sonata', 2017, 'PETROL', 135, 135),
       (4, 'Kia', 'Optima', 2021, 'ELECTRIC', 100, 100),
       (4, 'Tesla', 'Model 3', 2022, 'ELECTRIC', 90, 90),
       (5, 'BMW', '5 Series', 2023, 'PETROL', 140, 140),
       (5, 'Audi', 'A4', 2023, 'DIESEL', 115, 115),
       (6, 'Mercedes', 'C-Class', 2020, 'DIESEL', 120, 120),
       (6, 'Volvo', 'XC60', 2021, 'HYBRID', 100, 100),
       (7, 'Ford', 'Escape', 2022, 'PETROL', 140, 140),
       (7, 'Toyota', 'RAV4', 2020, 'ELECTRIC', 90, 90),
       (8, 'Honda', 'Accord', 2021, 'HYBRID', 110, 110),
       (8, 'Subaru', 'Forester', 2019, 'PETROL', 130, 130),
       (9, 'Jeep', 'Wrangler', 2022, 'DIESEL', 145, 145),
       (9, 'Tesla', 'Model S', 2023, 'ELECTRIC', 75, 75),
       (10, 'Audi', 'Q5', 2023, 'PETROL', 135, 135),
       (10, 'BMW', '3 Series', 2022, 'DIESEL', 120, 120),
       (11, 'Lexus', 'ES', 2022, 'HYBRID', 110, 110),
       (11, 'Cadillac', 'XT5', 2020, 'PETROL', 145, 145),
       (12, 'Porsche', 'Macan', 2021, 'DIESEL', 135, 135),
       (12, 'Jaguar', 'F-Pace', 2023, 'ELECTRIC', 80, 80),
       (13, 'Mazda', 'CX-5', 2021, 'PETROL', 125, 125),
       (13, 'Lincoln', 'Corsair', 2022, 'HYBRID', 95, 95),
       (14, 'Volvo', 'S60', 2020, 'PETROL', 130, 130),
       (14, 'Mercedes', 'GLC', 2021, 'DIESEL', 120, 120),
       (15, 'Infiniti', 'Q50', 2022, 'PETROL', 110, 110),
       (15, 'Acura', 'TLX', 2023, 'HYBRID', 100, 100);




INSERT INTO application (
    financial_info_id,
    customer_id,
    vehicle_details_id,  -- Include this if the column is used
    manager_id,
    price,
    down_payment,
    residual_value,
    year_period,
    interest_rate,
    status,
    created_at,
    updated_at,
    monthly_payment,
    agreement_fee
) VALUES
      (1, 1, 1, null, 25000.00, 5000.00, 10000.00, 5, 4.5, 'SIGNED', '2023-01-01', '2023-04-01', 350.00, null),
      (2, 2, 2, 1, 18000.00, 3600.00, 7200.00, 3, 3.9, 'PENDING_CHANGES', '2023-01-15', '2023-04-01', 450.00, null),
      (3, 3, 3, 2, 22000.00, 4400.00, 8800.00, 4, 4.0, 'SIGNED', '2023-02-01', '2023-04-01', 500.00, null),
      (4, 4, 4, 2, 15000.00, 3000.00, 6000.00, 3, 5.0, 'SIGNED', '2023-02-20', '2023-04-01', 380.00, null),
      (5, 5, 5, 3, 32000.00, 6400.00, 12800.00, 6, 3.5, 'PENDING_CHANGES', '2023-03-05', '2023-04-01', 460.00, null),
      (6, 6, 6, 3, 36000.00, 7200.00, 14400.00, 7, 4.2, 'REJECTED', '2023-03-15', '2023-04-01', 470.00, null),
      (7, 7, 7, 4, 20000.00, 4000.00, 8000.00, 5, 4.8, 'SIGNED', '2023-03-25', '2023-04-01', 340.00, null),
      (8, 8, 8, 4, 24000.00, 4800.00, 9600.00, 6, 3.6, 'REJECTED', '2023-04-01', '2023-04-01', 390.00, null),
      (9, 9, 9, 5, 27000.00, 5400.00, 10800.00, 5, 4.4, 'PENDING_CHANGES', '2023-04-10', '2023-04-10', 510.00, null),
      (10, 10, 10, 5, 30000.00, 6000.00, 12000.00, 5, 3.7, 'SIGNED', '2023-04-15', '2023-04-15', 540.00, null),
      (11, 11, 11, 6, 40000.00, 8000.00, 16000.00, 7, 4.0, 'SIGNED', '2023-04-01', '2023-04-01', 550.00, null),
      (12, 12, 12, 6, 35000.00, 7000.00, 14000.00, 6, 3.5, 'REJECTED', '2023-04-02', '2023-04-02', 480.00, null),
      (13, 13, 13, 7, 25000.00, 5000.00, 10000.00, 4, 4.5, 'PENDING_CHANGES', '2023-04-03', '2023-04-03', 430.00, null),
      (14, 14, 14, 7, 45000.00, 9000.00, 18000.00, 5, 3.9, 'SIGNED', '2023-04-04', '2023-04-04', 590.00, null),
      (15, 15, 15, 8, 37000.00, 7400.00, 14800.00, 6, 4.3, 'PENDING_CHANGES', '2023-04-05', '2023-04-05', 560.00, null);








