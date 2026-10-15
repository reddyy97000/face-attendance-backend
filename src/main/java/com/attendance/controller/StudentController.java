package com.attendance.controller;

import com.attendance.model.Student;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.service.FaceRecognitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository students;
    private final AttendanceRepository attendance;
    private final FaceRecognitionService face;

    public StudentController(StudentRepository s, AttendanceRepository a, FaceRecognitionService f) {
        this.students = s; this.attendance = a; this.face = f;
    }

    public record RegisterRequest(String name, String rollNo, String section, String image) {}

    @GetMapping
    public List<Student> list() { return students.findAll(); }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (req.name() == null || req.name().isBlank() || req.rollNo() == null || req.rollNo().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "Name and roll number are required"));
        if (req.image() == null || req.image().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "Face photo is required"));
        if (students.existsByRollNo(req.rollNo()))
            return ResponseEntity.badRequest().body(Map.of("error", "Roll number already registered"));
        float[] features = face.extractFeatures(req.image());
        if (features == null)
            return ResponseEntity.badRequest().body(Map.of("error", "No face detected. Please retake the photo."));

        Student s = new Student();
        s.setName(req.name().trim());
        s.setRollNo(req.rollNo().trim());
        s.setSection(req.section());
        s.setPhoto(req.image());
        s.setFaceFeatures(face.serialize(features));
        return ResponseEntity.ok(students.save(s));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        attendance.deleteByStudentId(id);
        students.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
