# Learning Management System (LMS)

## 📚 Project Description
The **Learning Management System (LMS)** is a web-based platform that allows instructors to create and manage courses, lessons, quizzes, and assignments. Students can enroll in courses, access learning materials, submit work, and receive feedback. Administrators oversee system configurations, manage users, and access platform-wide reports.

---

## 🎯 System Purpose
To provide a centralized, secure environment for delivering educational content, automating enrollment and grading workflows, and sending timely notifications via in-app messages and email.

---

## 👥 User Roles

- **ADMIN**: Full control over users, roles, courses, and system configuration  
- **INSTRUCTOR**: Create and manage own courses, lessons, assessments, and view student performance  
- **STUDENT**: Enroll in courses, access lessons, submit assignments, take quizzes, and view grades  

---

## 🧱 Backend Architecture

- Built with **Java Spring Boot**, exposing RESTful APIs  
- Data persisted in **MySQL** via Spring Data JPA  
- **JWT-based authentication** and **role-based authorization**  
- Event-driven notification services  
- Environment-specific configuration (DB credentials, JWT secrets)  
- **Maven** used for build and dependency management  

---

## 🚀 Key Functional Features

- **User Management**: Sign up, login, profile updates, password reset  
- **Course & Lesson Management**: CRUD operations, multimedia uploads  
- **Enrollment**: Students can enroll, instructors view rosters  
- **Attendance Tracking**: OTP-based session validation  
- **Assessments**:
  - Quizzes: Question banks, timed attempts, auto-scoring  
  - Assignments: File uploads, submission tracking, instructor feedback  
- **Notifications**: Real-time in-app alerts and email updates for deadlines, content, and grades  

---

## 🔐 Security and Validation

- **JWT Authentication** with 1-day expiry  
- Role-based access control for endpoints  
- Model validation via **Jakarta Validation** annotations  
- Passwords securely **hashed** before storage  
- Sensitive configs externalized from source code  

---

## 🧩 Entities Overview

- **User**: Username, email, hashed password, role  
- **Course**: Title, description, associated instructor  
- **Lesson**: Content linked to a course  
- **Enrollment**: Relationship between student and course  
- **Attendance**: OTP-based check-ins  
- **Quiz & Question**: Quiz definitions and question banks  
- **QuizAttempt**: Student quiz submissions and scores  
- **Assignment & Submission**: Instructor-defined assignments and student uploads  
- **Notification**: In-app/email messages triggered by events  

---

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **MySQL 8**
- **JWT Security**
- **Maven**
- **Gmail SMTP** (for email notifications)
