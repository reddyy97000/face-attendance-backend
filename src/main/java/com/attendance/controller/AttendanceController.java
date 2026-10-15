package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.service.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final StudentRepository students;
    private final AttendanceRepository attendance;
    private final FaceRecognitionService face;

    @Value("${face.match.threshold:0.70}")
    private double threshold;

    public AttendanceController(StudentRepository s, AttendanceRepository a, FaceRecognitionService f) {
        this.students = s; this.attendance = a; this.face = f;
    }

    public record MarkRequest(String image) {}

    /** Status: MARKED | ALREADY_MARKED | UNKNOWN | NO_FACE */
    @PostMapping("/mark")
    public Map<String, Object> mark(@RequestBody(required = false) MarkRequest req) {
        Map<String, Object> res = new HashMap<>();
        if (req == null || req.image() == null || req.image().isBlank()) {
            res.put("status", "NO_FACE");
            return res;
        }
        float[] probe = face.extractFeatures(req.image());
        if (probe == null) { res.put("status", "NO_FACE"); return res; }

        Student best = null; double bestScore = 0;
        for (Student s : students.findAll()) {
            if (s.getFaceFeatures() == null) continue;
            double score = face.similarity(probe, face.deserialize(s.getFaceFeatures()));
            if (score > bestScore) { bestScore = score; best = s; }
        }
        res.put("confidence", bestScore);
        if (best == null || bestScore < threshold) { res.put("status", "UNKNOWN"); return res; }

        res.put("student", best);
        LocalDate today = LocalDate.now();
        if (attendance.existsByStudentIdAndDate(best.getId(), today)) {
            res.put("status", "ALREADY_MARKED"); return res;
        }
        Attendance a = new Attendance();
        a.setStudent(best); a.setDate(today); a.setTime(LocalTime.now().withNano(0));
        a.setConfidence(bestScore);
        attendance.save(a);
        res.put("status", "MARKED");
        res.put("record", a);
        return res;
    }

    @GetMapping
    public List<Attendance> records(@RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return date == null ? attendance.findAllByOrderByDateDescTimeDesc()
                            : attendance.findByDateOrderByTimeDesc(date);
    }

    /** Attendance percentage per student over all class days recorded. */
    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        long totalDays = Math.max(attendance.countDistinctDates(), 1);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Student s : students.findAll()) {
            long present = attendance.countByStudentId(s.getId());
            Map<String, Object> m = new HashMap<>();
            m.put("student", s);
            m.put("present", present);
            m.put("totalDays", totalDays);
            m.put("percentage", Math.round(present * 1000.0 / totalDays) / 10.0);
            list.add(m);
        }
        return ResponseEntity.ok(list);
    }
}
