package com.app.lms.attendance_management.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.lms.attendance_management.model.OTP;
import com.app.lms.course_management.model.Course;

public interface OtpRepository extends JpaRepository<OTP, Long> {
    boolean existsByCodeAndCourse(String code, Course course); // Method to check uniqueness within a course
    OTP findByCodeAndCourse(String code, Course course);
}
