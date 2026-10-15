package com.attendance;

import nu.pattern.OpenCV;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AttendanceApplication {
    public static void main(String[] args) {
        OpenCV.loadLocally(); // loads native OpenCV bundled in the openpnp jar
        SpringApplication.run(AttendanceApplication.class, args);
    }
}
