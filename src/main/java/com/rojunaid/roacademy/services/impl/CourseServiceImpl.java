package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Iterable<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public boolean isCourseExist(Long courseId) {
        return courseRepository.existsById(courseId);
    }

    @Override
    public void deleteCourseById(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}
