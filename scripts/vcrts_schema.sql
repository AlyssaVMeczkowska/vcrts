-- VCRTS Database Schema
-- Milestone 6: MySQL Database Implementation

-- REMOVED: DROP DATABASE IF EXISTS vcrts_db; -- This line was deleting your data!
CREATE DATABASE IF NOT EXISTS vcrts_db;
USE vcrts_db;

-- Users table (unified for all user types)
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    account_type ENUM('Owner', 'Client', 'Controller') NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    password_hash VARCHAR(64) NOT NULL,
    has_agreed_to_terms BOOLEAN DEFAULT FALSE,
    creation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username),
    INDEX idx_account_type (account_type)
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    owner_id INT NOT NULL,
    vin VARCHAR(17) NOT NULL UNIQUE,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    vehicle_make VARCHAR(50) NOT NULL,
    vehicle_model VARCHAR(50) NOT NULL,
    vehicle_year INT NOT NULL,
    computing_power ENUM('Low', 'Medium', 'High') NOT NULL,
    arrival_date DATE NOT NULL,
    departure_date DATE NOT NULL,
    status ENUM('AVAILABLE', 'IN_USE', 'MAINTENANCE', 'DEPARTED') DEFAULT 'AVAILABLE',
    submission_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_owner (owner_id),
    INDEX idx_status (status),
    INDEX idx_dates (arrival_date, departure_date)
);

-- Jobs table
CREATE TABLE IF NOT EXISTS jobs (
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    job_type VARCHAR(100) NOT NULL,
    duration_hours INT NOT NULL,
    deadline DATE NOT NULL,
    description TEXT,
    submission_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_time INT DEFAULT 0,
    progress DOUBLE DEFAULT 0.0,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    FOREIGN KEY (client_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_client (client_id),
    INDEX idx_status (status),
    INDEX idx_deadline (deadline)
);

-- Requests table (for pending approvals)
CREATE TABLE IF NOT EXISTS requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    request_type ENUM('JOB_SUBMISSION', 'VEHICLE_SUBMISSION') NOT NULL,
    user_id INT NOT NULL,
    user_name VARCHAR(200) NOT NULL,
    submission_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING',
    notification_viewed BOOLEAN DEFAULT FALSE,
    request_data TEXT NOT NULL,
    rejection_reason TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_type (request_type)
);

-- Job-Vehicle assignments
CREATE TABLE IF NOT EXISTS job_vehicle_assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    assignment_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_primary BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE CASCADE,
    INDEX idx_job (job_id),
    INDEX idx_vehicle (vehicle_id)
);

-- Checkpoints table
CREATE TABLE IF NOT EXISTS checkpoints (
    checkpoint_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT NOT NULL,
    vehicle_id INT,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    checkpoint_data TEXT,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL,
    INDEX idx_job (job_id)
);

-- ============================================================================
-- DATA POPULATION
-- Using INSERT IGNORE so we don't get duplicate errors if data already exists
-- ============================================================================

-- Insert users
INSERT IGNORE INTO users (user_id, account_type, first_name, last_name, email, username, phone_number, password_hash, has_agreed_to_terms, creation_timestamp) VALUES
(1, 'Owner', 'Owner', 'One', 'owner1@email.com', 'owner1', '', 'fd83787115f5ba5f7301cedc4d9a13811c5425ada361d0a0d3e300dbca2b70f2', TRUE, '2025-11-03 02:29:46'),
(2, 'Owner', 'Owner', 'Two', 'owner2@email.com', 'owner2', '', 'fd83787115f5ba5f7301cedc4d9a13811c5425ada361d0a0d3e300dbca2b70f2', TRUE, '2025-11-03 02:30:38'),
(3, 'Client', 'Client', 'One', 'client1@email.com', 'client1', '', 'fd83787115f5ba5f7301cedc4d9a13811c5425ada361d0a0d3e300dbca2b70f2', FALSE, '2025-11-03 02:31:03'),
(4, 'Client', 'Client', 'Two', 'client2@email.com', 'client2', '', 'fd83787115f5ba5f7301cedc4d9a13811c5425ada361d0a0d3e300dbca2b70f2', FALSE, '2025-11-03 02:31:21'),
(5, 'Controller', 'Controller', 'Account', 'controller@email.com', 'controller', '', 'fd83787115f5ba5f7301cedc4d9a13811c5425ada361d0a0d3e300dbca2b70f2', FALSE, '2025-11-03 02:31:40'),
(6, 'Controller', 'Alyssa', 'Meczkowska', 'controller2@email.com', 'controller2', '', 'fd83787115f5ba5f7301cedc4d9a13811c5425ada361d0a0d3e300dbca2b70f2', FALSE, '2025-11-03 02:31:40');

-- Insert jobs
INSERT IGNORE INTO jobs (job_id, client_id, job_type, duration_hours, deadline, description, submission_timestamp, status) VALUES
(1, 3, 'Simulation', 53, '2025-12-09', '', '2025-11-19 03:18:42', 'PENDING'),
(2, 3, 'Computational Task', 6, '2025-11-28', '', '2025-11-19 03:18:54', 'PENDING'),
(3, 3, 'Networking & Communication', 61, '2025-11-30', '', '2025-11-19 03:19:18', 'PENDING'),
(4, 4, 'Simulation', 23, '2025-12-02', '', '2025-11-19 03:19:25', 'PENDING'),
(5, 3, 'Simulation', 6, '2025-11-24', '', '2025-11-19 03:19:48', 'PENDING'),
(6, 3, 'Simulation', 24, '2025-11-24', '', '2025-11-19 03:20:08', 'PENDING'),
(7, 4, 'Networking & Communication', 14, '2025-12-11', '', '2025-11-19 03:20:17', 'PENDING'),
(8, 3, 'Networking & Communication', 40, '2025-11-24', '', '2025-11-19 03:20:38', 'PENDING'),
(9, 3, 'Data Storage & Transfer', 2, '2025-11-24', '', '2025-11-19 03:20:51', 'PENDING'),
(10, 3, 'Data Storage & Transfer', 12, '2025-12-10', 'qadadsda', '2025-12-01 22:38:57', 'PENDING');

-- Insert vehicles
INSERT IGNORE INTO vehicles (vehicle_id, owner_id, vin, license_plate, vehicle_make, vehicle_model, vehicle_year, computing_power, arrival_date, departure_date, status, submission_timestamp) VALUES
(1, 1, '1G1PC5SB0E7180475', 'HUY-5810', 'Tesla', 'Y', 2025, 'Medium', '2025-12-01', '2025-12-18', 'AVAILABLE', '2025-11-19 03:19:42'),
(2, 2, '3N1AB6AP6BL602066', 'JLP-2810', 'Tesla', 'X', 2024, 'High', '2025-12-16', '2025-12-18', 'AVAILABLE', '2025-11-19 03:20:27'),
(3, 1, '1FMJK2A51DEF50669', 'XHY-5481', 'Tesla', 'X', 2024, 'Medium', '2025-11-28', '2025-12-04', 'AVAILABLE', '2025-11-19 03:21:08'),
(4, 1, 'asdasddas', 'asadasd', 'sfdsfdfs', 'sfdssf', 2009, 'Low', '2025-12-09', '2025-12-11', 'AVAILABLE', '2025-12-01 22:38:53');

-- Insert requests
INSERT IGNORE INTO requests (request_id, request_type, user_id, user_name, submission_timestamp, status, notification_viewed, request_data, rejection_reason) VALUES
(1, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Data Storage & Transfer\nduration: 2\ndeadline: 2025-11-24\ndescription: \n---\n', ''),
(2, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Simulation\nduration: 5\ndeadline: 2025-11-24\ndescription: \n---\n', ''),
(3, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Real-Time Processing\nduration: 15\ndeadline: 2025-11-30\ndescription: \n---\n', ''),
(4, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Networking & Communication\nduration: 40\ndeadline: 2025-11-24\ndescription: \n---\n', ''),
(5, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Simulation\nduration: 53\ndeadline: 2025-12-09\ndescription: \n---\n', ''),
(6, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Simulation\nduration: 24\ndeadline: 2025-11-24\ndescription: \n---\n', ''),
(7, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Networking & Communication\nduration: 61\ndeadline: 2025-11-30\ndescription: \n---\n', ''),
(8, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Computational Task\nduration: 6\ndeadline: 2025-11-28\ndescription: \n---\n', ''),
(9, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-11-19 03:12:43', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Simulation\nduration: 6\ndeadline: 2025-11-24\ndescription: \n---\n', ''),
(10, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Simulation\nduration: 24\ndeadline: 2025-11-27\ndescription: \n---\n', ''),
(11, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Computational Task\nduration: 51\ndeadline: 2025-11-25\ndescription: \n---\n', ''),
(12, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Networking & Communication\nduration: 16\ndeadline: 2025-11-24\ndescription: \n---\n', ''),
(13, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Networking & Communication\nduration: 16\ndeadline: 2025-11-23\ndescription: \n---\n', ''),
(14, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Simulation\nduration: 23\ndeadline: 2025-12-02\ndescription: \n---\n', ''),
(15, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Batch Processing\nduration: 14\ndeadline: 2025-11-30\ndescription: \n---\n', ''),
(16, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'REJECTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Networking & Communication\nduration: 24\ndeadline: 2025-11-24\ndescription: \n---\n', ''),
(17, 'JOB_SUBMISSION', 4, 'Job Client 2', '2025-11-19 03:13:41', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 2\njob_type: Networking & Communication\nduration: 14\ndeadline: 2025-12-11\ndescription: \n---\n', ''),
(18, 'VEHICLE_SUBMISSION', 1, 'Vehicle Owner 1', '2025-11-19 03:16:26', 'ACCEPTED', TRUE, 'type: vehicle_availability\nuser_id: 1\nvin: 1FMJK2A51DEF50669\nlicense_plate: XHY-5481\nvehicle_make: Tesla\nvehicle_model: X\nvehicle_year: 2024\ncomputing_power: Medium\nstart_date: 2025-11-28\nend_date: 2025-12-04\n---\n', ''),
(19, 'VEHICLE_SUBMISSION', 1, 'Vehicle Owner 1', '2025-11-19 03:16:26', 'ACCEPTED', TRUE, 'type: vehicle_availability\nuser_id: 1\nvin: 1G1PC5SB0E7180475\nlicense_plate: HUY-5810\nvehicle_make: Tesla\nvehicle_model: Y\nvehicle_year: 2025\ncomputing_power: Medium\nstart_date: 2025-12-01\nend_date: 2025-12-18\n---\n', ''),
(20, 'VEHICLE_SUBMISSION', 1, 'Vehicle Owner 1', '2025-11-19 03:16:26', 'REJECTED', TRUE, 'type: vehicle_availability\nuser_id: 1\nvin: 1GCRKSE79CZ241798\nlicense_plate: JTR-8613\nvehicle_make: Tesla\nvehicle_model: 3\nvehicle_year: 2021\ncomputing_power: Medium\nstart_date: 2025-11-21\nend_date: 2025-11-26\n---\n', ''),
(21, 'VEHICLE_SUBMISSION', 2, 'Vehicle Owner 2', '2025-11-19 03:17:59', 'REJECTED', TRUE, 'type: vehicle_availability\nuser_id: 2\nvin: 1GNDX13E53D159157\nlicense_plate: GHY-2014\nvehicle_make: BMW\nvehicle_model: X5 M\nvehicle_year: 2026\ncomputing_power: Medium\nstart_date: 2025-12-18\nend_date: 2025-12-25\n---\n', ''),
(22, 'VEHICLE_SUBMISSION', 2, 'Vehicle Owner 2', '2025-11-19 03:17:59', 'ACCEPTED', TRUE, 'type: vehicle_availability\nuser_id: 2\nvin: 3N1AB6AP6BL602066\nlicense_plate: JLP-2810\nvehicle_make: Tesla\nvehicle_model: X\nvehicle_year: 2024\ncomputing_power: High\nstart_date: 2025-12-16\nend_date: 2025-12-18\n---\n', ''),
(23, 'VEHICLE_SUBMISSION', 1, 'Vehicle Owner 1', '2025-12-01 22:37:34', 'ACCEPTED', TRUE, 'type: vehicle_availability\nuser_id: 1\nvin: asdasddas\nlicense_plate: asadasd\nvehicle_make: sfdsfdfs\nvehicle_model: sfdssf\nvehicle_year: 2009\ncomputing_power: Low\nstart_date: 2025-12-09\nend_date: 2025-12-11\n---\n', ''),
(24, 'JOB_SUBMISSION', 3, 'Job Client 1', '2025-12-01 22:38:11', 'ACCEPTED', TRUE, 'type: job_submission\nuser_id: 1\njob_type: Data Storage & Transfer\nduration: 12\ndeadline: 2025-12-10\ndescription: qadadsda\n---\n', ''),
(25, 'VEHICLE_SUBMISSION', 1, 'Vehicle Owner 1', '2025-12-01 22:41:31', 'REJECTED', TRUE, 'type: vehicle_availability\nuser_id: 1\nvin: adasdds\nlicense_plate: adsasd\nvehicle_make: adas\nvehicle_model: adasd\nvehicle_year: 2011\ncomputing_power: Low\nstart_date: 2025-12-11\nend_date: 2025-12-12\n---\n', '');