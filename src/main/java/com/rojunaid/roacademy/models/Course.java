package com.rojunaid.roacademy.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @ManyToOne
    private Course parentCourse;

    @OneToMany(mappedBy = "parentCourse")
    private Set<Course> preRequisiteCourses = new HashSet<>();

    @ManyToOne
    private Grade grade;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getPreRequisiteCourses() {
        return preRequisiteCourses;
    }

    public void setPreRequisiteCourses(Set<Course> preRequisiteCourses) {
        this.preRequisiteCourses = preRequisiteCourses;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Course getParentCourse() {
        return parentCourse;
    }

    public void setParentCourse(Course parentCourse) {
        this.parentCourse = parentCourse;
    }
}
