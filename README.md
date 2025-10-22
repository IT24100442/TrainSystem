# 🚆 TrainLink – Train Reservation and Management System

TrainLink is a **Spring Boot web application** designed to simplify train ticket booking, management, and operations. It provides distinct functionalities for **administrators** and **passengers**, ensuring a smooth and reliable experience for both roles.

---

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Tech Stack](#tech-stack)
- [Design Patterns Used](#design-patterns-used)
- [Installation Guide](#installation-guide)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Sample Credentials](#sample-credentials)
- [Future Improvements](#future-improvements)
- [Contributors](#contributors)
- [License](#license)

---

## 🧭 Overview
The TrainLink system enables users to:
- View train schedules and seat availability  
- Book and manage train tickets  
- Make payments via multiple methods  
- Allow administrators to manage trains, routes, and bookings efficiently  

The system integrates **Spring Boot, Thymeleaf, and MySQL/File Handling** (depending on version) to provide a responsive and maintainable web solution.

---

## ✨ Features

### 👤 Passenger
- Register and log in securely  
- Search trains by route, date, and class  
- Book, view, and cancel tickets  
- Pay via credit/debit card or mobile payment  

### 🛠️ Admin
- Add, update, and delete train details  
- Manage routes and schedules  
- View bookings and generate reports  

### 💳 Payment Module
Implements **Strategy Design Pattern** to allow multiple payment methods:
- Credit Card  
- Debit Card  
- Mobile Payment (e.g., eZ Cash, Frimi)  

---

## 🧱 System Architecture
The system follows the **MVC (Model–View–Controller)** pattern and **layered architecture**:
- **Controller Layer:** Handles HTTP requests and responses  
- **Service Layer:** Contains business logic  
- **Repository Layer:** Manages data storage and retrieval  
- **View Layer:** Built with Thymeleaf for dynamic HTML rendering  

---

## 💻 Tech Stack

| Category | Technologies |
|-----------|--------------|
| Backend | Java 17+, Spring Boot |
| Frontend | Thymeleaf, HTML5, CSS3, JavaScript |
| Database | MySQL or File-based Storage |
| Build Tool | Maven |
| IDE | IntelliJ IDEA / Eclipse |
| Version Control | Git & GitHub |

---

## ⚙️ Installation Guide

### Prerequisites
- Java 17 or higher  
- Maven 3.9+  
- MySQL Server (if using database version)  
- IDE (IntelliJ IDEA / Eclipse)  

### Steps
1. **Clone the Repository**
   ```bash
        git clone https://github.com/yourusername/trainlink.git
        cd trainlink
2. Configure Database (if applicable)

Open application.properties
Update your MySQL credentials:
     
       spring.datasource.url=jdbc:mysql://localhost:3306/trainlink
       spring.datasource.username=root
       spring.datasource.password=yourpassword

3. Build the Project

       mvn clean install


4. Run the Application

       mvn spring-boot:run


5. Access the App

       http://localhost:8080

## 🧩 Project Structure

<img width="661" height="531" alt="image" src="https://github.com/user-attachments/assets/f9c485ad-06f1-4e9f-b058-74004a022699" />





## 👥 Contributors
| Name                           | Role          | Responsibility                     |
|--------------------------------|---------------|------------------------------------|
| **Wickremasinghe N. N. D. R.** | Leader        | Train Route and Updates Management |
| **Shazna M. M.**               | Scrum Master  | Digital Ticket Verification System |
| **Herath M. I. P. H.**         | Developer     | Booking Management System          |
| **Rizquan A. W. M.**           | Developer     | Customer Account Management        |
| **Geethapriya W. M. D. N.**    | Developer     | Employee Account Management        |
| **Weerasingha B. A. S.**       | Developer     | Customer Service Management        |


## 📄 License

This project is for educational purposes only and distributed under the MIT License.
You are free to use and modify it with proper attribution.
