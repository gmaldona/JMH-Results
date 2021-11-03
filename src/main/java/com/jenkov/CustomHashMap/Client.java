package com.jenkov.CustomHashMap;

import com.jenkov.CustomHashMap.CustomUtil.CourseCollection;
import com.jenkov.Util.Constants;
import com.jenkov.Util.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Client implements Runnable {

    private CourseCollection courseCollection;
    private List<Course> courseSchedule;
    private boolean isDone = false;

    public Client() {
            courseSchedule = new ArrayList<>();
    }

    public void setCourseCollection(CourseCollection courseCollection) {
        this.courseCollection = courseCollection;
    }

    public CourseCollection getCourseCollection() { return courseCollection; }

    public List<Course> getCourses() {
        return courseSchedule;
    }

    public String getSchedule() {
        while (!isDone) {System.out.print("");}
        StringBuilder schedule = new StringBuilder();
        courseSchedule.forEach( course -> schedule.append(course.getName()).append(" > ").append(course.getStartTime()).append(":00 \n") );
        return schedule.toString();
    }

    @Override
    public void run() {
        try {
        int selectedID = courseCollection.size() > 0 ? ThreadLocalRandom.current().nextInt(courseCollection.size()) : 0;
        Course selectedCourse = courseCollection.getCourse(selectedID);
        courseSchedule.add(selectedCourse);

        } catch (Exception e) { e.printStackTrace(); }

        isDone = true;
    }
}
