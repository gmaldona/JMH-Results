package com.jenkov.ConcurrentHashMap; 

import com.jenkov.Util.Course;
import com.jenkov.Util.Constants;
import com.jenkov.Util.CourseReader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ConcurrentServer {

    private ExecutorService exService;
    private volatile status futureStatus;

    private int clientCount; 
    private List<ConcurrentClient> clients;
    private List<Future> clientFutures;
    private List<String> courseList;

    private ConcurrentHashMap<Integer, Course> courseCollection;
    private ConcurrentHashMap<Integer, Boolean> boolMap = new ConcurrentHashMap<>();

    private int courseCount = 0;

    public ConcurrentServer(int clientCount) {
        this.clientCount = clientCount;
    } 

    public ConcurrentServer() {
        this.clientCount = Constants.MAX_READERS;
    }

    public void setup() { 
        System.out.println("Concurrent HashMap");
        exService = Executors.newWorkStealingPool();
        clients = new ArrayList<>();
        courseCollection = new ConcurrentHashMap<>(Constants.INITIAL_COURSE_COUNT, 0.75f, Constants.MAX_READERS);
        courseList = new ArrayList<>();
        boolMap.put(0, true);
        Scanner sc = new Scanner(CourseReader.get());

        while (sc.hasNextLine()) {
            courseList.add(sc.nextLine());
        }

        for (int i = 0; i < Constants.INITIAL_COURSE_COUNT; i++) {
            addCourse();
        }

        for (int client_i = 0; client_i < clientCount; client_i++) {
            clients.add(new ConcurrentClient(this));
            clients.get(client_i).setCourseCollection(courseCollection);
        }

        System.out.println("Size: " + courseCollection.size());
        futureStatus = status.INPROGRESS;
    }

    public void RStart() {
        clientFutures = clients.stream()
                .map(exService::submit)
                .collect(Collectors.toList());

        boolean breakout = false;
        while (!breakout) {
            breakout = clientFutures.stream().allMatch(Future::isDone);
        }
        boolMap.put(0, false);
        clients.stream().map(ConcurrentClient::getSchedule).forEach(System.out::println);
    }

    public void RWStart() {
        clientFutures = clients.stream()
                .map(exService::submit)
                .collect(Collectors.toList());
        //Thread for writing, on a timer, add courses to the HashMap
        Thread updater = new Thread(() -> {
            while (boolMap.get(0)) {
                addCourse();
                try {
                    Thread.sleep(Constants.WRITER_TIMER_MILLIS);
                    System.out.println("Course Added");
                } catch (Exception ignored) {
                }
            }
        });
        updater.start();

        boolean breakout = false;
        while (!breakout) {
            breakout = clientFutures.stream().allMatch(Future::isDone);
        }
        boolMap.put(0, false);
        updater.interrupt();
        clients.stream().map(ConcurrentClient::getSchedule).forEach(System.out::println);
    }

    private enum status {
        INPROGRESS, FINISHED;
    }

    private void addCourse() {
        if (courseList.size() != 0) {
            String courseTitle = courseList.remove(0);
            courseCollection.put(courseCount, new Course(courseTitle, courseCount));
            courseCount++;
        }
    }

}