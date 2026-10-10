CREATE DATABASE IF NOT EXISTS oibsip_reservation;
USE oibsip_reservation;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS trains (
    train_number VARCHAR(20) PRIMARY KEY,
    train_name VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pnr VARCHAR(20) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    passenger_name VARCHAR(120) NOT NULL,
    train_number VARCHAR(20) NOT NULL,
    train_name VARCHAR(120) NOT NULL,
    travel_class VARCHAR(40) NOT NULL,
    journey_date DATE NOT NULL,
    source_station VARCHAR(120) NOT NULL,
    destination_station VARCHAR(120) NOT NULL,
    status ENUM('BOOKED', 'CANCELLED') NOT NULL DEFAULT 'BOOKED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Demo account for local practice only. Change the password before any real use.
INSERT IGNORE INTO users (username, password) VALUES ('admin', 'admin123');

-- Sample train data for testing the train-name lookup.
INSERT IGNORE INTO trains (train_number, train_name) VALUES
('12001', 'Shatabdi Express'),
('12951', 'Mumbai Rajdhani Express'),
('12301', 'Howrah Rajdhani Express'),
('12627', 'Karnataka Express');
