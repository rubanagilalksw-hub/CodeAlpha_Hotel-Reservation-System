# 🏨 Hotel Reservation & Management System

A desktop **Hotel Management System** built with **Java Swing**, featuring room management, reservations, customer records, payments, and reports — all through a modern, custom-styled GUI with file-based persistent storage (no database setup required).

> 🎓 This project was developed as part of the **CodeAlpha** Java Development Internship Program.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue)
![Storage](https://img.shields.io/badge/Storage-File%20I%2FO-green)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## ✨ Features

- 🔐 **Secure Login** — configurable admin username & password
- 📊 **Dashboard** — live stats: total rooms, available rooms, today's bookings, revenue
- 🛏️ **Room Management** — add, edit, delete rooms with type, pricing, capacity & features
- 📅 **Reservations** — search available rooms by type/date/guests and book instantly
- 👤 **Customer Management** — track customer details and booking history
- 💳 **Payments** — record payments via Cash, Card, Easypaisa, JazzCash, or Bank Transfer
- 📈 **Reports** — revenue and booking analytics
- ⚙️ **Settings** — update hotel info, currency, preferences, login credentials, and backup/restore data

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java (JDK 11+) |
| GUI | Java Swing (custom-painted components, no external UI libraries) |
| Storage | Plain text files (`hotel_data/`) — no external database needed |
| Architecture | MVC-style — `model`, `dao`, `ui`, `util` packages |

---

## 📁 Project Structure

```
HotelManagementSystem/
├── src/hotel/
│   ├── ui/       → All screens (Login, Dashboard, Rooms, Reservations, Payments, Reports, Settings...)
│   ├── model/    → Data classes (Room, Customer, Booking, Payment)
│   ├── dao/      → Data persistence logic (DataManager, AppConfig)
│   └── util/     → Theme.java (shared colors, fonts, styling helpers)
├── hotel_data/   → Text-file based data storage (auto-created on first run)
├── run.sh        → Launch script for Linux/macOS
├── run.bat       → Launch script for Windows
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- **JDK 11 or higher** installed (`java -version` and `javac -version` to check)

### Clone the repository
```bash
git clone https://github.com/<your-username>/HotelManagementSystem.git
cd HotelManagementSystem
```

### Run the application

**Windows:**
```bash
run.bat
```

**Linux / macOS:**
```bash
chmod +x run.sh
./run.sh
```

Both scripts automatically compile all `.java` source files into an `out/` folder and launch the application.

### Default Login
```
Username: admin
Password: admin
```
> You can change both the username and password anytime from the **Settings → Change Login Credentials** panel inside the app.

---

## 💾 Data Storage

All data is stored as plain text files inside `hotel_data/`, which is created automatically on first launch:

| File | Purpose |
|---|---|
| `rooms.txt` | Room details & availability status |
| `customers.txt` | Customer records |
| `bookings.txt` | Reservation records |
| `payments.txt` | Payment history |
| `config.properties` | Hotel info, preferences, and login credentials |

No database installation is required — the app works out of the box.

---

## 📸 Screenshots

## Login page

<img width="721" height="458" alt="image" src="https://github.com/user-attachments/assets/56a16035-2bc6-43ae-8dfd-953f806add1e" />

## Dashboard

<img width="960" height="509" alt="image" src="https://github.com/user-attachments/assets/36cd7bb0-5828-47cc-8484-27ca958db948" />

---

## 🎓 Acknowledgement

This project was built as part of the **CodeAlpha Internship Program** (Java Development track), as a practical demonstration of:
- Object-Oriented Programming (OOP) in Java
- Java Swing GUI development
- File-based data persistence (no DB dependency)
- Real-world application architecture (model–dao–ui separation)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

## 🙌 Author

Developed with ❤️ as part of the **CodeAlpha** internship.
Feel free to ⭐ star this repo if you found it helpful!
