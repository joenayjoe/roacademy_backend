package com.rojunaid.roacademy.dto.error;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class CourseDTO {

    @NotEmpty
    @Column(unique = true)
    private String name;
    private Long gradeId;
    private Optional<Long> parentCourseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Optional<Long> getParentCourseId() {
        return parentCourseId;
    }

    public void setParentCourseId(Optional<Long> parentCourseId) {
        this.parentCourseId = parentCourseId;
    }
}
