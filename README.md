# Covoituni – Student Carpooling Desktop Application

Covoituni is a JavaFX desktop application that connects university students who drive to campus with students looking for a ride. Drivers publish trips and manage their vehicles, passengers book seats, and an administrator moderates users, events and complaints.

It was built as a team project at ESPRIT, with each feature developed on its own Git branch and integrated into `lastmain`.

## Features

**Accounts and roles**
- Registration and login with BCrypt-hashed passwords and input validation
- Three roles: **admin**, **driver** and **passenger**, each with its own dashboard
- Password reset by email and a personal profile page with a profile picture

**Carpooling**
- Trip publishing, editing and listing
- Ride announcements, including announcements linked to events
- Seat reservations, with separate views for drivers and passengers, plus a trip history
- Vehicle and vehicle category management

**Community**
- Events created and managed by the admin
- Reviews left by users
- Complaints, which users submit and the admin answers from a dedicated screen

**Integrations**
- **Google Maps**: map view inside the app
- **AI chatbot**: DeepSeek-R1 through the NVIDIA API, for user assistance
- **Google Translate** (RapidAPI): text translation
- **QR codes** (ZXing), sent by email
- **PDF export** (iText)

## Tech stack

| Layer | Technology |
| --- | --- |
| Language | Java 17 |
| User interface | JavaFX 21 (FXML + CSS), FontAwesomeFX |
| Database | MySQL accessed through JDBC (prepared statements) |
| Security | jBCrypt password hashing |
| Libraries | iText (PDF), ZXing (QR codes), JavaMail / Commons Email, OkHttp, Gson, dotenv-java |
| Build | Maven |

## Architecture

The project follows an MVC structure:

```
src/main/java
├── entities/   # Model classes: User, Role, Trajet, Reservation, Annonce, Car, Event, Avis, Reclamation...
├── Services/   # Data access (CRUD over JDBC), one service per entity
├── User/       # User and role services
├── gui/
│   ├── Admin/  # Admin controllers: users, drivers, passengers, events, complaints
│   └── Users/  # Driver and passenger controllers: trips, reservations, cars, maps, chatbot...
├── utils/      # Database connection (MyConnection) and password hashing (PasswordUtil)
└── main/       # Entry point (TestFX)

src/main/resources
├── Admin/, Users/   # FXML views
└── styles/          # CSS themes
```

## Getting started

### Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL 8 (or MariaDB)

### 1. Database
Create a MySQL database and import the schema. The default connection in `src/main/java/utils/MyConnection.java` is:

```
jdbc:mysql://localhost:3307/jdbcdemo  (user: root, empty password)
```

Change the port, database name or credentials there to match your setup.

### 2. Environment variables
Create `src/main/resources/.env` (it must **not** be committed) with your own keys:

```dotenv
GOOGLE_MAPS_API_KEY=your_google_maps_key
LICENCE_KEY=your_jxbrowser_licence_key

EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_app_password
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
```

The chatbot (NVIDIA API) and translator (RapidAPI) also need your own API keys.

### 3. Run

```bash
mvn clean javafx:run
```

The application opens on the login screen.

## Team

Developed by a team of ESPRIT students as part of an integrated Java project:
Mouhamed Mahmoud, Oussama Negzaoui, Iheb Ayed, Hassen Jridi, Ahmed Chatti and Mohamed Ben Fredj.
