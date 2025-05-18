package com.app.lms.course_management.service;
import com.app.lms.DTO.CourseRequest;
import com.app.lms.DTO.StudentInfoDTO;
import com.app.lms.course_management.model.Course;
import com.app.lms.course_management.model.Enrollment;
import com.app.lms.course_management.model.Lesson;
import com.app.lms.user_management.model.User;
import com.app.lms.course_management.repository.CourseRepository;
import com.app.lms.course_management.repository.EnrollmentRepository;
import com.app.lms.course_management.repository.LessonRepository;
import com.app.lms.user_management.repository.UserRepository;
import com.app.lms.user_management.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.app.lms.DTO.LessonContentDTO;
import com.app.lms.DTO.CourseContentDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, LessonRepository lessonRepository, UserRepository userRepository, UserService userService)
    {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Course createCourse(Course course, Long instructorId) {
        User instructor = userService.findById(instructorId).orElse(null);
        if (instructor == null) {
            throw new RuntimeException("Instructor not found");
        }
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public void addLessonToCourse(Long courseId, Lesson lesson) {
        Course course;
        course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course);
        lessonRepository.save(lesson);
    }

    // Method to retrieve all courses and return them as CourseResponse DTOs
    public List<CourseRequest> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::convertToCourseRequest) // Convert each Course to CourseResponse
                .collect(Collectors.toList());
    }

    // Helper method to convert Course to CourseResponse
    private CourseRequest convertToCourseRequest(Course course) {
        CourseRequest courseResponse = new CourseRequest();
        courseResponse.setId(course.getId());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setDuration(course.getDuration());
        return courseResponse;
    }

    // Method to enroll a student in a course
    public Boolean enrollStudentInCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if student is already enrolled in the course
        if (enrollmentRepository.findAll().stream().anyMatch(e -> e.getStudent().getId().equals(studentId) && e.getCourse().getId().equals(courseId))) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        // Create the enrollment record
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);
        return Boolean.TRUE;
    }


    public List<StudentInfoDTO> getEnrolledStudents(Long courseId) {
        // Fetch all enrollments for the course
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        if (enrollments.isEmpty()) {
            throw new RuntimeException("No students enrolled in this course");
        }

        // Map enrolled students to a list of formatted strings
        return enrollments.stream()
                .map(enrollment -> {
                    User student = enrollment.getStudent();
                    return new StudentInfoDTO(
                            student.getId(),
                            student.getFirstName(),
                            student.getLastName(),
                            student.getEmail()
                    );
                })
                .collect(Collectors.toList());
    }

    public CourseContentDTO mapToCourseDTO(Course course) {
        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setLessons(
                course.getLessons().stream()
                        .map(this::mapToLessonDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private LessonContentDTO mapToLessonDTO(Lesson lesson) {
        LessonContentDTO dto = new LessonContentDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setMediaPaths(lesson.getMediaPaths());
        return dto;
    }


    public CourseContentDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));

        return mapToCourseDTO(course);
    }

    public LessonContentDTO getLesson(Long id){
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + id));
        return mapToLessonDTO(lesson);
    }

    public Course findCourseById(Long id){
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
    }

    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    public void delete(Course course){
        courseRepository.delete(course);
    }

    @Transactional(readOnly = true)
    public boolean isEnrolled(Long courseId, Long studentId) {
        // 1. Verify course existence (throws 404 if not found)
        courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course not found with ID: " + courseId
                ));

        // 2. Perform the enrollment check
        return enrollmentRepository.existsByCourse_IdAndStudent_Id(courseId, studentId);
    }

}
