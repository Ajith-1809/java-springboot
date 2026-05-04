# 🌟 HR Management Portal (Java Spring Boot Edition)

A high-performance, enterprise-grade Human Resources Management System built with **Java Spring Boot**, **React 18**, and **PostgreSQL (Supabase)**. This professional application features a sophisticated glassmorphism design and a robust multi-cloud architecture.

---

## 💎 Project Essence
This portal provides a unified interface for employee lifecycle management, featuring secure authentication, real-time auditing, and a global distribution model.

### 🚀 Live Infrastructure
*   **Frontend**: [Firebase Hosting](https://firebase.google.com/) (Global CDN)
*   **Backend**: [Render](https://render.com/) (Java/Maven Web Service)
*   **Database**: [Supabase](https://supabase.com/) (PostgreSQL Instance)

---

## 🛠️ Technical Sophistication

### Backend (Java Spring Boot)
*   **Language**: Java 17+
*   **Framework**: Spring Boot 3.x
*   **Security**: Spring Security with JWT (JSON Web Tokens)
*   **Data Access**: Spring Data JPA / Hibernate
*   **Database**: PostgreSQL (Supabase)
*   **Build Tool**: Maven

### Frontend (React)
*   **Framework**: React 18 (Vite)
*   **Design System**: Modern Vanilla CSS with Glassmorphism
*   **State Management**: React Hooks & Context API
*   **Networking**: Fetch API with interceptor patterns

---

## ✨ Key Features
*   **🔐 Secure Authentication**: JWT-based secure login with role-based access control (Admin/User).
*   **👥 Employee Management**: Full CRUD operations for employee records with a sleek, interactive dashboard.
*   **📊 Real-time Auditing**: Automatic logging of all administrative actions for transparency and security.
*   **🖼️ Profile Management**: Support for profile picture uploads and detailed employee dossiers.
*   **📱 Responsive Architecture**: Optimized for desktop, tablet, and mobile workflows.

---

## 🏗️ Multi-Cloud Deployment Strategy

### 1. Backend on Render
The Spring Boot application is containerized and deployed as a high-performance Web Service.
*   **Build Command**: `./mvnw clean install`
*   **Start Command**: `java -jar target/*.jar`
*   **Required Environment Variables**:
    *   `DB_URL`: JDBC connection string for Supabase.
    *   `DB_USER`: Database username.
    *   `DB_PASS`: Database password.
    *   `JWT_SECRET`: Security signing key.

### 2. Frontend on Firebase
The React application is compiled and served through Firebase's global edge network.
*   **Build Command**: `npm run build`
*   **Deployment**: `firebase deploy --only hosting`

---

## 📋 Professional Installation (Local)

### Prerequisites
*   JDK 17 or higher
*   Maven 3.8+
*   Node.js 18+

### Steps
1.  **Clone & Enter**:
    ```bash
    git clone https://github.com/Ajith-1809/java-springboot.git
    cd java-springboot
    ```
2.  **Launch Backend**:
    ```bash
    cd backend
    ./mvnw spring-boot:run
    ```
3.  **Launch Frontend**:
    ```bash
    cd ../frontend
    npm install
    npm run dev
    ```

---

## 🛡️ License
Distributed under the MIT License. See `LICENSE` for more information.

---
**Developed with ❤️ for Modern HR Operations.**
