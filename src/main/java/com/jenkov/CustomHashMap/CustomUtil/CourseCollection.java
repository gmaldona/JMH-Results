package com.jenkov.CustomHashMap.CustomUtil;

import com.jenkov.Util.Course;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CourseCollection {

    private HashMap<Integer, Course> courseHashMap;

    private final ReentrantReadWriteLock RW_LOCK = new ReentrantReadWriteLock(true);
    private final Lock rLock = RW_LOCK.readLock();
    private final Lock wLock = RW_LOCK.writeLock();

    public CourseCollection() {
        courseHashMap = new HashMap<>();
    }

    public synchronized Course getCourse(Integer courseID) {
        rLock.lock();
        try { return courseHashMap.get(courseID); }
        finally { rLock.unlock(); }
    }

    public synchronized Course put(Integer courseID, Course course) {
        wLock.lock();
        try {
            return courseHashMap.put(courseID, course);
        } finally { wLock.unlock(); }
    }

    public synchronized int size() {
        rLock.lock();
        try {
            return courseHashMap.size();
        } finally { rLock.unlock(); }
    }

    public synchronized HashMap<Integer, Course> getAll() {
        rLock.lock();
        try {
            return courseHashMap;
        } finally { rLock.unlock(); }
    }

}
