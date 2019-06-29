package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Course;

import java.util.Optional;

public interface CourseService {

    Iterable<Course> getAllCourse();
    Course createCourse(Course course);
    Course updateCourse(Course course);
    Optional<Course> findCourseById(Long courseId);
    boolean isCourseExist(Long courseId);
    void deleteCourseById(Long courseId);
}
