package backend.dto;

import jakarta.validation.constraints.NotNull;

public class EnrollmentRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long courseId;

    public EnrollmentRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}