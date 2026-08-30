<div align="center">

# 🚗 CarServ — Car Service Tracker

**A full-stack vehicle service management web application built with Spring Boot & Thymeleaf**

[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf)](https://www.thymeleaf.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-purple?style=for-the-badge)](LICENSE)

> Track vehicles, schedule services, manage reminders, handle bookings, and review spare parts — all in one elegant dashboard.

</div>

---

## ✨ Features

### 👤 Customer Portal
- 🔐 **Secure Registration & Login** — cookie-based session management
- 🚗 **Vehicle Management** — add, update, and track multiple cars
- 🔧 **Service History** — view full maintenance records per vehicle
- ⏰ **Smart Reminders** — get alerts for upcoming and overdue services
- 📅 **Book a Service** — schedule appointments with real-time status
- ⭐ **Reviews & Feedback** — rate and review service quality
- 📦 **Spare Parts Requests** — browse inventory and request parts
- ✏️ **Edit Profile & Delete Account**

### 🛡️ Admin Panel
- 📊 **Dashboard Overview** — live stats: cars, records, reminders, ratings, low stock
- 👥 **Customer Management** — view, promote, demote, or delete users
- 🛡️ **Team / Admins Management** — manage admin roles (SUPER_ADMIN support)
- 🔧 **Service Records** — full CRUD with invoice download support
- 📦 **Inventory Management** — spare parts with low-stock alerts
- 📩 **Part Request Fulfillment** — approve or reject customer part requests
- 📅 **Bookings & Payments** — approve/reject/complete bookings + payment tracking (COD, Paid, Pending)
- 📆 **Calendar View** — FullCalendar integration for booking schedule
- ⭐ **Review Moderation** — read and manage all customer reviews

---

## 🖼️ UI Design

The entire application uses a **luxury dark theme** with blended pink & blue glassmorphism:

- 🌈 **Pink & Blue gradient mesh** background with animated floating orbs
- 🪟 **Glassmorphism** panels with backdrop blur
- ✨ **Gradient shimmer** borders and hover effects
- 📱 Responsive layout with **Outfit** Google Font
- 🎨 Consistent design language across all pages

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 17, Spring Boot 3.2.3 |
| **ORM** | Spring Data JPA (Hibernate) |
| **Database** | MySQL 8.0 |
| **Templating** | Thymeleaf 3.x |
| **Security** | Spring Security (role-based: USER / ADMIN / SUPER_ADMIN) |
| **Frontend** | Vanilla HTML5 + CSS3 + JavaScript |
| **Calendar** | FullCalendar 6.1.8 (CDN) |
| **Fonts** | Google Fonts — Outfit |
| **Config** | dotenv-java (`.env` file support) |
| **Build** | Apache Maven |
| **Dev Tools** | Spring Boot DevTools, Actuator |

---

## 📁 Project Structure

```
carservice-main/
├── src/
│   └── main/
│       ├── java/com/example/usermanagement/
│       │   ├── controller/
│       │   │   ├── DashboardController.java       # Stats API + dashboard routing
│       │   │   ├── UserController.java            # Auth (login/register/logout)
│       │   │   ├── CarController.java             # Vehicle CRUD
│       │   │   ├── ServiceRecordController.java   # Service records + bookings
│       │   │   ├── ServiceReminderController.java # Reminders + alert API
│       │   │   ├── ReviewController.java          # Reviews
│       │   │   ├── SparePartController.java       # Parts + requests
│       │   │   ├── PageController.java            # Static page routing
│       │   │   └── GlobalControllerAdvice.java    # Global model attributes
│       │   ├── model/
│       │   │   ├── User.java / Person.java / Admin.java
│       │   │   ├── Car.java
│       │   │   ├── ServiceRecord.java / ServiceStatus.java
│       │   │   ├── ServiceReminder.java / ReminderStatus.java
│       │   │   ├── Review.java
│       │   │   ├── SparePart.java / PartRequest.java
│       │   │   ├── PaymentStatus.java
│       │   │   └── Role.java
│       │   ├── repository/                        # JPA Repositories
│       │   ├── service/                           # Business Logic Services
│       │   ├── DataInitializer.java               # Seeds default admin on startup
│       │   ├── SecurityConfig.java                # Spring Security config
│       │   └── UsermanagementApplication.java     # Main entry point
│       └── resources/
│           ├── templates/
│           │   ├── index.html                     # Login page
│           │   ├── register.html                  # Registration page
│           │   ├── user-dashboard.html            # Customer dashboard
│           │   ├── admin-dashboard.html           # Admin panel (SPA)
│           │   ├── edit-profile.html              # Profile editor
│           │   ├── cars/                          # Vehicle pages
│           │   ├── service-records/               # Service record pages
│           │   ├── reminders/                     # Reminder pages
│           │   ├── reviews/                       # Review pages
│           │   ├── spare-parts/                   # Parts pages
│           │   └── fragments/                     # Shared header fragment
│           ├── static/
│           │   ├── images/                        # Background & assets
│           │   └── theme.css                      # Shared design system
│           └── application.properties
├── .env.example                                   # Environment variable template
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- ☕ **Java 17+** — [Download JDK](https://adoptium.net/)
- 🗄️ **MySQL 8.0+** — [Download MySQL](https://www.mysql.com/downloads/)
- 📦 **Maven 3.8+** — [Download Maven](https://maven.apache.org/download.cgi) *(or use the included `mvnw` wrapper)*

---

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/carservice.git
cd carservice
```

---

### 2️⃣ Create the Database

Open MySQL and run:

```sql
CREATE DATABASE car_service_db;
```

---

### 3️⃣ Configure Environment Variables

Copy the example file and fill in your credentials:

```bash
# Windows
copy .env.example .env

# macOS / Linux
cp .env.example .env
```

Edit `.env`:

```env
DB_URL=jdbc:mysql://localhost:3306/car_service_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
SERVER_PORT=8080
```

---

### 4️⃣ Run the Application

**Using Maven Wrapper (recommended):**

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

**Or with Maven directly:**

```bash
mvn spring-boot:run
```

---

### 5️⃣ Open in Browser

```
http://localhost:8080
```

> 🚀 The app auto-creates all database tables on first run via Hibernate DDL auto.

---

## 🔑 Default Credentials

The app automatically seeds a default admin account on every startup:

| Role | Username / Email | Password |
|---|---|---|
| 👑 Super Admin | `admin` | `admin` |

> ℹ️ You can change these values in [`DataInitializer.java`](src/main/java/com/example/usermanagement/DataInitializer.java).

---

## 🌐 Key API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/` | Login page |
| `POST` | `/login` | Authenticate user |
| `GET` | `/register` | Registration page |
| `POST` | `/register` | Create new account |
| `GET` | `/dashboard` | Customer dashboard |
| `GET` | `/admin-dashboard` | Admin panel |
| `GET` | `/api/admin/stats` | Admin statistics JSON |
| `GET` | `/api/user/stats` | User statistics JSON |
| `GET` | `/api/users` | All users (Admin only) |
| `PUT` | `/api/users/{id}/role` | Change user role |
| `DELETE` | `/api/users/{id}` | Delete user |
| `GET` | `/cars/api` | All cars JSON |
| `GET` | `/service-records/api` | All service records JSON |
| `GET` | `/reminders/api` | All reminders JSON |
| `GET` | `/reminders/alerts` | Active alert messages JSON |
| `GET` | `/spare-parts/api` | All spare parts JSON |
| `GET` | `/spare-parts/requests/api` | Part requests JSON |
| `POST` | `/spare-parts/requests/{id}/fulfill` | Fulfill a part request |
| `GET` | `/reviews/api` | All reviews JSON |
| `GET` | `/edit-profile` | Edit profile page |

---

## 👥 User Roles

| Role | Access Level |
|---|---|
| `USER` | Customer portal — own cars, services, reminders, reviews |
| `ADMIN` | Full admin panel — view/manage all data (cannot promote/demote others) |
| `SUPER_ADMIN` | Unrestricted — promote/demote admins, delete any user |

---

## 🔒 Security

- Passwords are **BCrypt hashed** via Spring Security
- Sessions tracked via **HTTP Cookies** (`userId`, `userRole`)
- UI state hydrated from **localStorage**
- Role-based guards on both **frontend** (redirect) and **backend** (cookie validation)

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch:
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add amazing feature"
   ```
4. Push to the branch:
   ```bash
   git push origin feature/amazing-feature
   ```
5. Open a **Pull Request**

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Authors

> Built as a **SLIIT Second Semester** group project.

See [WORK_ALLOCATION.md](WORK_ALLOCATION.md) for team member contributions.

---

<div align="center">

Made with ❤️ and ☕ &nbsp;|&nbsp; **CarServ** — Keep your vehicles running perfectly.

</div>
