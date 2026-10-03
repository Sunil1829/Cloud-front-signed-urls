package backend.service;

import backend.entity.Enrollment;
import backend.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public boolean isUserEnrolled(Long userId, Long courseId) {
        return enrollmentRepository
                .findByUserIdAndCourseId(userId, courseId)
                .isPresent();
    }

    public Optional<Enrollment> getEnrollment(Long userId, Long courseId) {
        return enrollmentRepository
                .findByUserIdAndCourseId(userId, courseId);
    }
}