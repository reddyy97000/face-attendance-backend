# Face Attendance – Spring Boot Backend

Backend for the **Attendance System with Face Recognition** (React + Spring Boot + SQL + OpenCV).

[![Live Backend](https://img.shields.io/badge/Render-Live_API-46E3B7?logo=render&logoColor=white)](https://face-attendance-backend-wotj.onrender.com)
[![Live Frontend](https://img.shields.io/badge/Vercel-Live_App-000000?logo=vercel&logoColor=white)](https://ss-agency-attendence.vercel.app)
[![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)

---

## 🌐 Live Deployments
* **Backend API (Render):** [https://face-attendance-backend-wotj.onrender.com](https://face-attendance-backend-wotj.onrender.com)
* **Frontend Web App (Vercel):** [https://ss-agency-attendence.vercel.app](https://ss-agency-attendence.vercel.app)
* **Health Check:** [https://face-attendance-backend-wotj.onrender.com/actuator/health](https://face-attendance-backend-wotj.onrender.com/actuator/health)

---

## 🛠️ Tech Stack
- **Java 17**, **Spring Boot 3.3.4**, **Spring Data JPA**
- **Database:** MySQL (production / Docker) or embedded H2 (standalone zero-install)
- **Computer Vision:** OpenCV 4.9 (`org.openpnp:opencv`) – Haar cascade frontal face detection + feature normalization
- **Deployment:** Docker (multi-stage build), Render, Docker Compose

---

## 🚀 Running Locally

### Option A: With Docker Compose (MySQL + Backend)
```bash
cp .env.example .env
docker compose up --build -d
```

### Option B: Maven Local Run (Standalone H2 DB)
```bash
mvn spring-boot:run
```
Server starts on `http://localhost:8081`.

---

## 🔌 API Reference

| Method | Path | Request Body / Query | Description |
|---|---|---|---|
| `GET` | `/` | – | Service health dashboard & links |
| `GET` | `/api/students` | – | List all registered students |
| `POST` | `/api/students` | `{name, rollNo, section, image}` | Register student with face photo (base64) |
| `DELETE` | `/api/students/{id}` | – | Delete student & attendance records |
| `POST` | `/api/attendance/mark` | `{image}` | Face scan verification & attendance recording |
| `GET` | `/api/attendance` | `?date=YYYY-MM-DD` (optional) | List attendance records |
| `GET` | `/api/attendance/stats` | – | Student attendance percentages |
| `GET` | `/actuator/health` | – | Container liveness & readiness probes |

---

## 🧠 How Face Recognition Works
1. Incoming base64 photo is decoded into OpenCV matrix.
2. OpenCV Haar Cascade (`haarcascade_frontalface_default.xml`) locates the largest facial region.
3. Cropped face is resized to 64×64 pixels, grayscale converted, and histogram equalized.
4. Feature vector is normalized and stored.
5. Verification compares incoming vectors using normalized cross-correlation against registered faces.

---

## ⚙️ Environment Variables

| Variable | Default | Purpose |
|---|---|---|
| `PORT` | `8081` | Server HTTP port |
| `DB_URL` | H2 embedded file | JDBC Database connection URL |
| `DB_USERNAME` | `sa` | Database username |
| `DB_PASSWORD` | empty | Database password |
| `CORS_ORIGINS` | localhost + Vercel | Permitted frontend origins |
| `API_KEY` | empty | Optional X-API-Key header secret |
| `FACE_THRESHOLD` | `0.70` | Face recognition confidence threshold |
