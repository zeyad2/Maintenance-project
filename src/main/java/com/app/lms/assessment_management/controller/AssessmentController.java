package com.app.lms.assessment_management.controller;

import com.app.lms.DTO.FeedbackDTO;
import com.app.lms.DTO.QuizAttemptDTO;
import com.app.lms.assessment_management.service.PerformanceService;
import com.app.lms.config.JwtConfig;
import com.app.lms.course_management.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    private final PerformanceService performanceService;
    private final JwtConfig jwtConfig;
    private final CourseService courseService;

    public AssessmentController(PerformanceService performanceService, JwtConfig jwtConfig, CourseService courseService) {
        this.performanceService = performanceService;
        this.jwtConfig = jwtConfig;
        this.courseService = courseService;
    }

    @GetMapping("")
    public ResponseEntity<?> getStudentPerformance(@RequestHeader("Authorization") String token,@RequestParam Long studentId, @RequestParam Long courseId) {
        String role = jwtConfig.getRoleFromToken(token);
        Long instructorId = jwtConfig.getUserIdFromToken(token);
        if(!"INSTRUCTOR".equals(role) && !"ADMIN".equals(role)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
        }
        if("INSTRUCTOR".equals(role)) {
            if(!courseService.findCourseById(courseId).getInstructor().getId().equals(instructorId)) {
                return new ResponseEntity<>("You are not the owner of this course",HttpStatus.FORBIDDEN);
            }
        }

        List<QuizAttemptDTO> quizAttempts = performanceService.getQuizAttemptsByStudentId(studentId, courseId);
        List<FeedbackDTO> feedbacks = performanceService.getFeedbacksByStudentId(studentId, courseId);

        Map<String, Object> response = new HashMap<>();
        response.put("Quiz attempts", quizAttempts);
        response.put("Feedbacks", feedbacks);

        return ResponseEntity.ok(response);
    }
}
