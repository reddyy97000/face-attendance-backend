package com.attendance.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    private static final String FRONTEND_URL = "https://ss-agency-attendence.vercel.app";

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String indexHtml() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Face Attendance API</title>
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Space+Grotesk:wght@500;700&display=swap">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body {
                        font-family: 'DM Sans', -apple-system, BlinkMacSystemFont, sans-serif;
                        background: #0f172a;
                        color: #f8fafc;
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding: 1.5rem;
                    }
                    .card {
                        background: rgba(30, 41, 59, 0.7);
                        backdrop-filter: blur(12px);
                        border: 1px solid rgba(255, 255, 255, 0.1);
                        border-radius: 1.5rem;
                        padding: 2.5rem;
                        max-width: 540px;
                        width: 100%;
                        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
                    }
                    .badge {
                        display: inline-flex;
                        align-items: center;
                        gap: 0.5rem;
                        background: rgba(34, 197, 94, 0.15);
                        color: #4ade80;
                        padding: 0.35rem 0.85rem;
                        border-radius: 9999px;
                        font-size: 0.875rem;
                        font-weight: 600;
                        margin-bottom: 1.25rem;
                        border: 1px solid rgba(34, 197, 94, 0.3);
                    }
                    .dot {
                        width: 8px;
                        height: 8px;
                        background: #4ade80;
                        border-radius: 50%;
                        box-shadow: 0 0 10px #4ade80;
                    }
                    h1 {
                        font-family: 'Space Grotesk', sans-serif;
                        font-size: 2rem;
                        font-weight: 700;
                        margin-bottom: 0.75rem;
                        background: linear-gradient(to right, #ffffff, #94a3b8);
                        -webkit-background-clip: text;
                        -webkit-text-fill-color: transparent;
                    }
                    p {
                        color: #94a3b8;
                        line-height: 1.6;
                        margin-bottom: 2rem;
                        font-size: 1rem;
                    }
                    .btn {
                        display: block;
                        text-align: center;
                        background: #22c55e;
                        color: #0f172a;
                        font-weight: 700;
                        padding: 0.875rem 1.5rem;
                        border-radius: 0.75rem;
                        text-decoration: none;
                        transition: all 0.2s;
                        font-size: 1rem;
                    }
                    .btn:hover {
                        background: #4ade80;
                        transform: translateY(-1px);
                        box-shadow: 0 10px 20px -5px rgba(34, 197, 94, 0.4);
                    }
                    .endpoints {
                        margin-top: 2rem;
                        padding-top: 1.5rem;
                        border-top: 1px solid rgba(255, 255, 255, 0.08);
                    }
                    .endpoints h2 {
                        font-size: 0.875rem;
                        text-transform: uppercase;
                        letter-spacing: 0.05em;
                        color: #64748b;
                        margin-bottom: 0.75rem;
                    }
                    .endpoint-list {
                        display: flex;
                        flex-direction: column;
                        gap: 0.5rem;
                        font-family: monospace;
                        font-size: 0.85rem;
                    }
                    .endpoint-item {
                        display: flex;
                        justify-content: space-between;
                        padding: 0.4rem 0.6rem;
                        background: rgba(15, 23, 42, 0.5);
                        border-radius: 0.4rem;
                        color: #cbd5e1;
                    }
                    .endpoint-item a {
                        color: #38bdf8;
                        text-decoration: none;
                    }
                    .endpoint-item a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <div class="card">
                    <div class="badge">
                        <span class="dot"></span>
                        Backend API Operational
                    </div>
                    <h1>Face Attendance API</h1>
                    <p>
                        This is the Spring Boot & OpenCV backend REST API. The frontend user interface is hosted and live on Vercel.
                    </p>
                    <a href="https://ss-agency-attendence.vercel.app" class="btn">Open Web Application &rarr;</a>
                    <div class="endpoints">
                        <h2>Available API Endpoints</h2>
                        <div class="endpoint-list">
                            <div class="endpoint-item"><span>Health:</span> <a href="/actuator/health">/actuator/health</a></div>
                            <div class="endpoint-item"><span>Students:</span> <a href="/api/students">/api/students</a></div>
                            <div class="endpoint-item"><span>Attendance:</span> <a href="/api/attendance">/api/attendance</a></div>
                            <div class="endpoint-item"><span>Stats:</span> <a href="/api/attendance/stats">/api/attendance/stats</a></div>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> indexJson() {
        return ResponseEntity.ok(Map.of(
            "service", "Face Attendance Backend API",
            "status", "UP",
            "frontend", FRONTEND_URL,
            "endpoints", Map.of(
                "health", "/actuator/health",
                "students", "/api/students",
                "attendance", "/api/attendance",
                "mark", "/api/attendance/mark",
                "stats", "/api/attendance/stats"
            )
        ));
    }
}
