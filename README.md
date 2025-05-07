## Project Description & Inferred Requirements

### Core Purpose  
This application is a **Learning Management System (LMS)** back‑end built with Java and Spring Boot. It provides a comprehensive set of RESTful endpoints to manage every aspect of an online learning platform—users, courses, lessons, assessments, notifications, and performance tracking—enabling seamless integration with any front‑end or mobile client.

---

### User Management & Security  
- **Authentication & Authorization**  
  - Endpoints for user registration and login (`/api/auth/signup`, `/api/auth/signin`) secured by JWT.  
  - Role‑based access control (Student, Instructor, Admin) enforced on all protected routes.  
  - Passwords hashed at rest; every API request requiring authentication validates the JWT in the `Authorization` header.  
- **Profile Management**  
  - Users can view and update profile details (name, email, role).  

---

### Course & Lesson Management  
- **Course CRUD**  
  - Instructors can create, read, update, and delete courses via `CourseController`.  
- **Lesson Handling**  
  - Within each course, instructors add lessons (title, content, attachments).  
  - On lesson creation, an `AddedLessonEvent` is published to an in‑memory event bus, triggering notifications for enrolled students.  

---

### Enrollment & Notifications  
- **Enrollment**  
  - Students enroll in courses, publishing an `EnrollmentEvent`.  
- **Event‑Driven Notifications**  
  - A lightweight notification module listens for events (lesson added, enrollment, material uploaded) and persists notification records.  
  - Notifications can be fetched through a REST endpoint so clients can display real‑time alerts (e.g. “New lesson available”).  

---

### Assessment & Performance  
- **Assignments & Quizzes**  
  - Supports long‑form assignments and automatically graded quizzes.  
  - A shared `QuestionBank` supplies quiz questions.  
- **Submission Workflow**  
  - Students submit assignments and quiz attempts (`Submission`, `QuizAttempt` entities).  
  - Instructors review submissions and provide feedback.  
- **Performance Tracking**  
  - A `PerformanceService` aggregates grades and feedback, enabling dashboards for student progress.  

---

### Data Persistence  
- **JPA Entities**  
  - All domain objects (User, Course, Lesson, Assignment, Quiz, Submission, Notification, etc.) are mapped with JPA/Hibernate and stored in a relational database.  
- **Repositories**  
  - Spring Data JPA repositories provide standard CRUD operations.  

---

### Validation & Error Handling  
- **Input Validation**  
  - DTOs (e.g. `SignupRequest`, `LessonRequest`) use Jakarta Bean Validation (`@Valid`) to enforce data integrity.  
- **Consistent Error Responses**  
  - Controllers return appropriate HTTP status codes (201 Created, 200 OK, 400 Bad Request, etc.) and error payloads.  

---

### Automated Testing  
- **Unit & Integration Tests**  
  - Tests for core controllers (`CourseControllerTest`, `UserControllerTest`) boot up the Spring context and verify endpoint behavior.  

---

## Inferred High‑Level Requirements

### User Stories  
1. **Student**: Register, log in, browse courses, enroll, view lessons, and receive notifications.  
2. **Instructor**: Create and manage courses, add lessons and materials, build assessments, and review student work.  
3. **Administrator**: Manage users, oversee performance metrics, and configure system-wide settings.  

### Functional Requirements  
- JWT‑based authentication and role‑based authorization  
- CRUD operations for users, courses, lessons, assessments, feedback, and notifications  
- Event‑driven notification pipeline for key actions  
- Submission handling and automated quiz grading  
- Instructor feedback workflow  

### Non‑Functional Requirements  
- RESTful JSON API following best practices  
- Secure credential handling (password hashing, token expiration, role checks)  
- Modular, extensible package structure (user, course, assessment, notification)  
- Comprehensive test coverage with Spring Boot’s test framework  
