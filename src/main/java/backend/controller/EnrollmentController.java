package backend.controller;

import backend.dto.EnrollmentRequest;
import backend.entity.Course;
import backend.entity.Enrollment;
import backend.entity.User;
import backend.service.CourseService;
import backend.service.EnrollmentService;
import backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final CourseService courseService;

    public EnrollmentController(
            EnrollmentService enrollmentService,
            UserService userService,
            CourseService courseService) {

        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> enroll(
            @Valid @RequestBody EnrollmentRequest request) {

        User user = userService.getUserById(request.getUserId())
                .orElse(null);

        Course course = courseService.getCourseById(request.getCourseId())
                .orElse(null);

        if (user == null || course == null) {
            return ResponseEntity.notFound().build();
        }

        if (enrollmentService.isUserEnrolled(
                request.getUserId(),
                request.getCourseId())) {

            return ResponseEntity.badRequest()
                    .body("User is already enrolled");
        }

        Enrollment enrollment = new Enrollment(user, course);

        return ResponseEntity.ok(
                enrollmentService.createEnrollment(enrollment)
        );
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkEnrollment(
            @RequestParam Long userId,
            @RequestParam Long courseId) {

        return ResponseEntity.ok(
                enrollmentService.isUserEnrolled(userId, courseId)
        );
    }
}