DROP DATABASE IF EXISTS companydb;
CREATE DATABASE companydb;
USE companydb;


CREATE TABLE Departments (
                             dept_id INT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             manager_id INT
);

CREATE TABLE Employees (
                           emp_id INT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           age INT CHECK (age >= 18),
                           email VARCHAR(100) NOT NULL UNIQUE,
                           dept_id INT NOT NULL,
                           FOREIGN KEY (dept_id) REFERENCES Departments(dept_id)
);

CREATE TABLE Managers (
                          emp_id INT PRIMARY KEY,
                          dept_id INT UNIQUE,
                          FOREIGN KEY (emp_id) REFERENCES Employees(emp_id),
                          FOREIGN KEY (dept_id) REFERENCES Departments(dept_id)
);

ALTER TABLE Departments
    ADD FOREIGN KEY (manager_id) REFERENCES Employees(emp_id);

CREATE TABLE Clients (
                         client_id INT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE
);

CREATE TABLE Projects (
                          pr_id INT PRIMARY KEY,
                          title VARCHAR(100) NOT NULL,
                          client_id INT NOT NULL,
                          FOREIGN KEY (client_id) REFERENCES Clients(client_id)
);

CREATE TABLE Project_Employees (
                                   pr_id INT,
                                   emp_id INT,
                                   PRIMARY KEY (pr_id, emp_id),
                                   FOREIGN KEY (pr_id) REFERENCES Projects(pr_id) ON DELETE CASCADE,
                                   FOREIGN KEY (emp_id) REFERENCES Employees(emp_id) ON DELETE CASCADE
);

CREATE TABLE Resources (
                           res_id INT PRIMARY KEY,
                           material VARCHAR(100) NOT NULL,
                           quantity INT CHECK (quantity > 0)
);

CREATE TABLE Project_Resources (
                                   pr_id INT,
                                   res_id INT,
                                   PRIMARY KEY (pr_id, res_id),
                                   FOREIGN KEY (pr_id) REFERENCES Projects(pr_id),
                                   FOREIGN KEY (res_id) REFERENCES Resources(res_id)
);

CREATE TABLE Documents (
                           doc_id INT PRIMARY KEY,
                           title VARCHAR(100) NOT NULL,
                           pr_id INT NOT NULL,
                           FOREIGN KEY (pr_id) REFERENCES Projects(pr_id)
);

CREATE TABLE Users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       date_of_birth DATE,
                       role VARCHAR(20) DEFAULT 'USER',
                       photo LONGBLOB
);

INSERT INTO Departments VALUES
                            (1, 'Design', NULL),
                            (2, 'Engineering', NULL),
                            (3, 'Financial Affairs', NULL);

INSERT INTO Employees VALUES
                          (101, 'Anna Ivanova', 30, 'a.ivanova@gmail.com', 1),
                          (102, 'Martin Markov', 28, 'm.markov@gmail.com', 2),
                          (103, 'Maria Zhekova', 35, 'm.zhekova@gmail.com', 3);

UPDATE Departments SET manager_id = 101 WHERE dept_id = 1;
UPDATE Departments SET manager_id = 102 WHERE dept_id = 2;
UPDATE Departments SET manager_id = 103 WHERE dept_id = 3;

INSERT INTO Managers VALUES
                         (101,1),
                         (102,2),
                         (103,3);

INSERT INTO Clients VALUES
                        (201, 'Georgi Stanchev', 'g.stanchev@gmail.com'),
                        (202, 'Raiffeisen Bank', 'user@raiffeisen.com'),
                        (203, 'Rumyana Ivanova', 'r.ivanova@gmail.com');

INSERT INTO Projects VALUES
                         (301, 'Dianabat Two Storey House', 201),
                         (302, 'Boulevard Hristo Botev Office Building', 202),
                         (303, 'Dragalevtsi One Storey Villa', 203);

INSERT INTO Project_Employees VALUES
                                  (301, 101),
                                  (302, 102),
                                  (303, 103);

INSERT INTO Resources VALUES
                          (401, 'Laptop', 10),
                          (402, 'Projector', 2),
                          (403, 'Whiteboard', 5);

INSERT INTO Project_Resources VALUES
                                  (301, 401),
                                  (302, 402),
                                  (303, 403);

INSERT INTO Documents VALUES
                          (501, 'Architectural Plans', 301),
                          (502, 'Electricity Network', 302),
                          (503, 'Estimated Costs', 303);

INSERT INTO Users (username, password, first_name, last_name, email, date_of_birth, role)
VALUES
    ('admin', 'admin123', 'System', 'Administrator', 'admin@company.com', '1990-01-01', 'ADMIN'),
    ('user1', 'user123', 'Ivan', 'Petrov', 'ivan@gmail.com', '1995-05-10', 'USER');
