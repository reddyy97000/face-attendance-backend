# Face Attendance – Spring Boot Backend

Backend for the **Attendance System with Face Recognition** (React + Spring Boot + SQL + OpenCV).

## Tech
- Java 17, Spring Boot 3.3, Spring Data JPA
- MySQL (default) or H2 (no-install profile)
- OpenCV 4.9 (`org.openpnp:opencv`, natives bundled) – Haar cascade face detection + feature matching

## Run
```bash
# Option A: MySQL (edit username/password in src/main/resources/application.properties)
mvn spring-boot:run

# Option B: no database install needed
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```
Server starts at `http://localhost:8081`.

## API
| Method | Path | Body / Query | Description |
|---|---|---|---|
| GET | /api/students | – | List registered students |
| POST | /api/students | `{name, rollNo, section, image}` (image = base64 data URL) | Register student + face |
| DELETE | /api/students/{id} | – | Remove student and their records |
| POST | /api/attendance/mark | `{image}` | Recognize face & mark today. Returns `status`: MARKED, ALREADY_MARKED, UNKNOWN, NO_FACE |
| GET | /api/attendance | `?date=YYYY-MM-DD` (optional) | Attendance records |
| GET | /api/attendance/stats | – | Present days & percentage per student |

## How recognition works
1. Image decoded → grayscale → histogram equalized.
2. Largest face found with `haarcascade_frontalface_default.xml`.
3. Face cropped to 64×64, normalized into a feature vector, stored in SQL.
4. On attendance, the probe vector is compared to every student (normalized correlation). Below `face.match.threshold` → rejected so the user can retry.

## Push to GitHub
```bash
git init && git add . && git commit -m "Initial backend"
git remote add origin https://github.com/<you>/face-attendance-backend.git
git push -u origin main
```
