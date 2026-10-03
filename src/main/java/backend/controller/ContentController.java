package backend.controller;

import backend.entity.Course;
import backend.entity.User;
import backend.service.CourseService;
import backend.service.EnrollmentService;
import backend.service.S3PresignedUrlService;
import backend.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final S3PresignedUrlService s3PresignedUrlService;

    public ContentController(
            UserService userService,
            CourseService courseService,
            EnrollmentService enrollmentService,
            S3PresignedUrlService s3PresignedUrlService) {

        this.userService = userService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.s3PresignedUrlService = s3PresignedUrlService;
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourseContent(
            @PathVariable Long courseId,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        boolean enrolled = enrollmentService.isUserEnrolled(
                user.getId(),
                course.getId()
        );

        if (!enrolled) {
            return ResponseEntity
                    .status(403)
                    .body("You are not enrolled in this course");
        }
        String videoUrl =
                s3PresignedUrlService.generatePresignedUrl(
                        course.getVideoKey()
                );

        return ResponseEntity.ok(videoUrl);
    }
}