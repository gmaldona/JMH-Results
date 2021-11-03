package com.jenkov.CustomHashMap;

import com.jenkov.Util.Course;
import com.jenkov.Util.Constants;
import com.jenkov.Util.CourseReader;
import com.jenkov.CustomHashMap.CustomUtil.CourseCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Server {

    private ExecutorService exService;
    private status          futureStatus;

    private List<Client>    clients;
    private List<Future>    clientFutures;
    private List<String>    courseList;

    private int courseCount = 0;
    private CourseCollection courseCollection;

    public Server() throws InterruptedException {
        this.start();
    }

    private void start() throws InterruptedException {

        exService = Executors.newWorkStealingPool();
        clients = new ArrayList<>();
        courseCollection = new CourseCollection();
        courseList = new ArrayList<>();
        Scanner sc = new Scanner(CourseReader.get());

        while (sc.hasNextLine()) { courseList.add(sc.nextLine()); }

        for (int i = 0; i < Constants.INITIAL_COURSE_COUNT; i++) { addCourse(); }

        for (int client_i = 0; client_i < Constants.MAX_READERS; client_i++) {
            clients.add(new Client());
            clients.get(client_i).setCourseCollection(courseCollection);
        }
        System.out.println("Size: " + courseCollection.size());
        clientFutures = clients.stream()
                .map(exService::submit)
                .collect(Collectors.toList());

        futureStatus = status.INPROGRESS;

        //Thread for writing, on a timer, add courses to the HashMap
        Thread updater = new Thread(() -> {
            while (futureStatus == status.INPROGRESS) {
                try {
                    Thread.sleep(Constants.WRITER_TIMER_MILLIS);
                    addCourse();
                    System.out.println("Course Added");
                } catch (InterruptedException ignored) {
                }
            }
        });

        updater.start();

        while (futureStatus == status.INPROGRESS ) {
            futureStatus = (int) clientFutures.stream()
                    .filter(Future::isDone).count() == 0 ? status.INPROGRESS : status.FINISHED;
        }


        updater.interrupt();
        Thread.sleep(1000);
        Iterator<Client> iter = clients.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next().getSchedule());
        }
    }

    
    private void addCourse() {
        if (courseList.size() != 0) {
            String courseTitle = courseList.remove(0);
            courseCollection.put(courseCount, new Course(courseTitle, courseCount));
            courseCount++;
        }
    }
    
    private enum status {
        INPROGRESS, FINISHED;
    }
    
}
