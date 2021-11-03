package com.jenkov.ConcurrentHashMap; 

import com.jenkov.Util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ConcurrentClient implements Runnable {

    private ConcurrentHashMap<Integer, Course> courseCollection;
    private List<Course> courseSchedule;

    public ConcurrentClient() {
        courseSchedule = new ArrayList<>(); 
    }
    public ConcurrentClient(ConcurrentServer server) {
        courseSchedule = new ArrayList<>();
    }

    public void setCourseCollection(ConcurrentHashMap<Integer, Course> courseCollection) {
        this.courseCollection = courseCollection;
    }
    public List<Course> getCourses() { return courseSchedule; }
    public ConcurrentHashMap<Integer, Course> getCourseCollection() { return courseCollection; } 

    public String getSchedule() {
        StringBuilder schedule = new StringBuilder();
        courseSchedule.forEach( course -> schedule.append(course.getName()).append(" > ").append(course.getStartTime()).append(":00 \n") );
        return schedule.toString();
    }

    @Override
    public void run() {
             try {
            int courseListSize = courseCollection.size();
            int selectedID = courseListSize > 0 ? ThreadLocalRandom.current().nextInt(courseListSize) : 0;
            Course selectedCourse = courseCollection.get(selectedID);
            courseSchedule.add(selectedCourse);

            } catch (Exception e) { e.printStackTrace();}

    }

}
