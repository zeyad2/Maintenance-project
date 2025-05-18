package com.app.lms.assessment_management.controller;

import com.app.lms.dto.FeedbackRequest;
import com.app.lms.assessment_management.model.Assignment;
import com.app.lms.assessment_management.model.Feedback;
import com.app.lms.assessment_management.model.Submission;
import com.app.lms.assessment_management.service.AssignmentService;
import com.app.lms.assessment_management.service.FeedbackService;
import com.app.lms.config.JwtConfig;
import com.app.lms.course_management.model.Course;
import com.app.lms.course_management.service.CourseService;
import com.app.lms.notification_management.event_bus.event_bus;
import com.app.lms.notification_management.event_bus.events.AssignmentCreatedEvent;
import com.app.lms.notification_management.event_bus.events.FeedbackCreatedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import com.app.lms.assessment_management.service.SubmissionService;
import java.util.List;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final JwtConfig jwtConfig;
    private final SubmissionService submissionService;
    private final FeedbackService feedbackService;
    private final event_bus event_bus;
    private final CourseService courseService;

    private static final String ROLE_INSTRUCTOR = "INSTRUCTOR";
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String UNAUTHORIZED_OWNERSHIP = "Unauthorized: You do not own this course";

    public AssignmentController(AssignmentService assignmentService, JwtConfig jwtConfig, SubmissionService submissionService, FeedbackService feedbackService, event_bus event_bus, CourseService courseService) {
        this.assignmentService = assignmentService;
        this.jwtConfig = jwtConfig;
        this.submissionService = submissionService;
        this.feedbackService = feedbackService;
        this.courseService = courseService;
        this.event_bus = event_bus;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createAssignment(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("courseId") Long courseId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("deadline") String deadline) {

        String role = jwtConfig.getRoleFromToken(token);
        Long instructorId = jwtConfig.getUserIdFromToken(token);
//        if (!ROLE_INSTRUCTOR.equals(role)) {
//            return new ResponseEntity<>(UNAUTHORIZED, HttpStatus.FORBIDDEN);
//        }
//        Course course = courseService.findCourseById(courseId);
//        if (!course.getInstructor().getId().equals(instructorId)) {
//            return new ResponseEntity<>(UNAUTHORIZED_OWNERSHIP, HttpStatus.FORBIDDEN);
//        }

        try {
            LocalDateTime parsedDeadline = LocalDateTime.parse(deadline);

            Assignment assignment = new Assignment();
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(parsedDeadline);

            Assignment created = assignmentService.createAssignment(assignment, courseId, file);

            AssignmentCreatedEvent event = new AssignmentCreatedEvent(created.getId());
            event_bus.publish(event);

            return new ResponseEntity<>("Assignment created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating assignment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitSolution(
            @RequestHeader("Authorization") String token,
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("file") MultipartFile file) {

        // Validate the student's role
        String role = jwtConfig.getRoleFromToken(token);
        Long studentId = jwtConfig.getUserIdFromToken(token);

        if (!"STUDENT".equals(role)) {
            return new ResponseEntity<>(UNAUTHORIZED, HttpStatus.FORBIDDEN);
        }
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        boolean enrolled = courseService.isEnrolled(assignment.getCourse().getId(), studentId);
        if(!enrolled){
            return ResponseEntity.status(403).body("You must be enrolled in the course to be able to view its content");
        }

        try {
            submissionService.submitSolution(assignmentId, studentId, file);
            return new ResponseEntity<>("Solution submitted successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error submitting solution: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getAllAssignments(@RequestHeader("Authorization") String token, @PathVariable Long courseId) {
        String role = jwtConfig.getRoleFromToken(token);
        Long studentId = jwtConfig.getUserIdFromToken(token);

        if (!"STUDENT".equals(role)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
        }
        boolean enrolled = courseService.isEnrolled(courseId, studentId);
        if(!enrolled){
            return ResponseEntity.status(403).body("You must be enrolled in the course to be able to view its content");
        }

        List<Assignment> assignments = assignmentService.getAllAssignments(courseId);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    @GetMapping("/submission/{assignmentId}")
    public ResponseEntity<?> getAllSubmissions(@RequestHeader("Authorization") String token, @PathVariable Long assignmentId) {
        String role = jwtConfig.getRoleFromToken(token);
        Long instructorId = jwtConfig.getUserIdFromToken(token);

        if (!ROLE_INSTRUCTOR.equals(role)) {
            return new ResponseEntity<>(UNAUTHORIZED, HttpStatus.FORBIDDEN);
        }
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        if(!assignment.getCourse().getInstructor().getId().equals(instructorId)){
            return new ResponseEntity<>(UNAUTHORIZED_OWNERSHIP, HttpStatus.FORBIDDEN);
        }

        List<Submission> submissions = submissionService.getAllSubmissions(assignmentId);
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @PostMapping("/feedback")
    public ResponseEntity<String> createFeedback(@RequestHeader("Authorization") String token, @RequestBody FeedbackRequest feedbackRequest) {
        String role = jwtConfig.getRoleFromToken(token);
        Long instructorId = jwtConfig.getUserIdFromToken(token);

        if (!ROLE_INSTRUCTOR.equals(role)) {
            return new ResponseEntity<>(UNAUTHORIZED, HttpStatus.FORBIDDEN);
        }
        Course course = submissionService.getSubmission(feedbackRequest.getSubmissionID()).getAssignment().getCourse();
        if(!course.getInstructor().getId().equals(instructorId)){
            return new ResponseEntity<>(UNAUTHORIZED_OWNERSHIP, HttpStatus.FORBIDDEN);
        }

        try
        {
            Feedback feedback = feedbackService.giveFeedback(feedbackRequest);
            FeedbackCreatedEvent event = new FeedbackCreatedEvent(feedback.getId());
            event_bus.publish(event);
            return new ResponseEntity<>("Feedback given successfully", HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/feedback/{submissionId}")
    public ResponseEntity<?> getFeedback(@RequestHeader("Authorization") String token, @PathVariable Long submissionId) {

        String role = jwtConfig.getRoleFromToken(token);
        Long studentId = jwtConfig.getUserIdFromToken(token);

        if (!"STUDENT".equals(role)) {
            return new ResponseEntity<>(UNAUTHORIZED, HttpStatus.FORBIDDEN);
        }
        if(!submissionService.getSubmission(submissionId).getStudent().getId().equals(studentId)){
            return new ResponseEntity<>(UNAUTHORIZED_OWNERSHIP, HttpStatus.FORBIDDEN);
        }

        try {
            FeedbackRequest feedback = feedbackService.getFeedbackBySubmission(submissionId);
            if (feedback == null) {
                return new ResponseEntity<>("Feedback not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(feedback, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
