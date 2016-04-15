package com.example.ll.demo02.rxjava;

/**
 * Created by Administrator on 2016/4/15.
 */
public class Student {
    private String name;
    private Course[] courses;

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
