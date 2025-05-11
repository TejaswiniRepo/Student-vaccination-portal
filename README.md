# 🏥 School Vaccination Portal – Backend (Spring Boot)

This is the backend service for the **School Vaccination Portal** full-stack web application. It handles all core business logic, database interactions, and REST APIs for managing students, vaccination drives, dashboards, and reports.

---

## 📦 Tech Stack

- **Java 17**
- **Spring Boot**
- **MongoDB** (local or Atlas)
- **Spring Data MongoDB**
- **Spring Web**
- **Lombok**
- **Apache Commons CSV** – for CSV export
- **OpenPDF** – for generating PDF reports

---

## 🔗 API Base URL

When deployed:
https://onlinedomain.com/api

When running locally:
http://localhost:8080/api


---

## 🧰 Features

### ✅ Authentication
- Simulated login for school coordinator role

### 🎓 Student Management
- Add/edit/view students
- Search/filter by name, class, ID, or vaccination status
- Bulk import students via CSV upload
- Mark student vaccinated in a specific drive

### 💉 Vaccination Drive Management
- Create/edit drives
- Prevent date overlaps
- Validate future scheduling (≥15 days ahead)

### 📊 Dashboard APIs
- Total students
- % vaccinated
- Upcoming drives (next 30 days)

### 📁 Reports
- Export student vaccination data as:
    - CSV
    - PDF

---

## 🚀 Getting Started

### 1️⃣ Clone the repository

```bash
git clone https://github.com/TejaswiniRepo/Student-vaccination-portal.git
cd school-vaccine-backend

2️⃣ Set up MongoDB
Use MongoDB Atlas or run locally

Update your Mongo URI in src/main/resources/application.properties:

spring.data.mongodb.uri=mongodb://localhost:27017/school_vaccination_db

Run the application
Using Maven wrapper:
./mvnw spring-boot:run

Or build the JAR:
./mvnw clean install
java -jar target/school-vaccine-portal-0.0.1-SNAPSHOT.jar

