package com.attendance.repository;

import com.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDateOrderByTimeDesc(LocalDate date);
    List<Attendance> findAllByOrderByDateDescTimeDesc();
    boolean existsByStudentIdAndDate(Long studentId, LocalDate date);
    long countByStudentId(Long studentId);
    void deleteByStudentId(Long studentId);

    @Query("select count(distinct a.date) from Attendance a")
    long countDistinctDates();
}
