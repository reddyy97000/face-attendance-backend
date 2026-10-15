package com.attendance;

import nu.pattern.OpenCV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AttendanceApplication {
    private static final Logger log = LoggerFactory.getLogger(AttendanceApplication.class);

    public static void main(String[] args) {
        try {
            OpenCV.loadLocally();
            log.info("OpenCV native libraries loaded successfully");
        } catch (Throwable t) {
            log.warn("Could not load native OpenCV: {}", t.getMessage());
        }
        SpringApplication.run(AttendanceApplication.class, args);
    }
}
