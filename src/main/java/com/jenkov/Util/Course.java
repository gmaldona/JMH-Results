package com.jenkov.Util;

import java.util.concurrent.ThreadLocalRandom;

public class Course {

    private String name;
    private int id;
    private int startTime;

    public Course(String name, int id) {
        this.name = name;
        this.id   = id;

        startTime = ThreadLocalRandom.current().nextInt(8) + 9;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized int getStartTime() {
        return startTime;
    }

    public synchronized String toString() {
        return name + " > " + startTime + ":00";
    }
}
