package com.jenkov;

import com.jenkov.CustomHashMap.CustomUtil.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.List; 

import com.jenkov.CustomHashMap.Client;
import com.jenkov.ConcurrentHashMap.*;
import com.jenkov.Util.CourseReader;
import com.jenkov.Util.Course; 

import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Threads; 
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.infra.Blackhole; 


public class MyBenchmark {

    @State(Scope.Benchmark)
    public static class CustomHashMap {
        List<String> courseList = new ArrayList<>();
        CourseCollection hashmap;

        @Setup(Level.Trial)
        public void setup() throws Exception { 
            CourseReader.read();
            Scanner sc = new Scanner(CourseReader.get());

            while (sc.hasNextLine()) courseList.add(sc.nextLine());
            
            hashmap = new CourseCollection();
            for (int i = 0; i < 500; i++) {
                hashmap.put(i, new Course(courseList.get(i), i));
            }
        }
    }

    @State(Scope.Benchmark)
    public static class ConHashMap {
        List<String> courseList = new ArrayList<>();
        ConcurrentHashMap<Integer, Course> hashmap;

        @Setup(Level.Trial)
        public void setup() throws Exception { 
            CourseReader.read();
            Scanner sc = new Scanner(CourseReader.get());

            while (sc.hasNextLine()) courseList.add(sc.nextLine());
            
            hashmap = new ConcurrentHashMap<Integer, Course>();
            for (int i = 0; i < 500; i++) {
                hashmap.put(i, new Course(courseList.get(i), i));
            }
        }
    }
    
    @State(Scope.Thread)
    public static class CustomClient {
        
        Client client;
        Thread clientThread;   

        @Setup(Level.Iteration)
        public void setup(CustomHashMap hashmap) {
            client = new Client();
            client.setCourseCollection(hashmap.hashmap);
            clientThread = new Thread(client);
        }
        @Benchmark @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS)
        @Fork(value = 1)
        @Warmup(iterations = 5, time = 5)
        @Measurement(iterations = 5, time = 2)
        @Threads(50)
        public void benchmarkLowWriteHighRead(Blackhole blackhole) {
            clientThread.run();
            if (ThreadLocalRandom.current().nextInt(100) < 5) {
                List<Course> courses = client.getCourses(); 
                Course course = courses.get(ThreadLocalRandom.current().nextInt(courses.size())); 
                CourseCollection courseCollection = client.getCourseCollection();
                if (course != null) {
                    courseCollection.put(courseCollection.size() + 1 , course); 
                    blackhole.consume(courseCollection);
                    blackhole.consume(courses);
                    blackhole.consume(course);
                }
            }
        }
        @Benchmark @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS)
        @Fork(value = 1)
        @Warmup(iterations = 5, time = 5)
        @Measurement(iterations = 5, time = 2)
        @Threads(50)
        public void benchmarkHighWriteHighRead(Blackhole blackhole) {
            clientThread.run();
              List<Course> courses = client.getCourses(); 
              Course course = courses.get(ThreadLocalRandom.current().nextInt(courses.size())); 
              CourseCollection courseCollection = client.getCourseCollection();
              if (course != null) {
                courseCollection.put(courseCollection.size() + 1 , course); 
                blackhole.consume(courseCollection);
                blackhole.consume(courses);
                blackhole.consume(course);
            }
        }  
    } 

    @State(Scope.Thread)
    public static class ConnClient {

        ConcurrentClient client;
        Thread clientThread;
        
        @Setup(Level.Invocation)
        public void setup(ConHashMap hashmap) { 
            client = new ConcurrentClient();
            client.setCourseCollection(hashmap.hashmap);
            clientThread = new Thread(client);
        }
        @Benchmark @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS)
        @Fork(value = 1)
        @Warmup(iterations = 5, time = 5)
        @Measurement(iterations = 5, time = 2)
        @Threads(50)
        public void benchmarkLowWriteHighRead(Blackhole blackhole) {
            clientThread.run(); 
            if (ThreadLocalRandom.current().nextInt(100) < 5) {
                List<Course> courses = client.getCourses();
                Course course = courses.get(ThreadLocalRandom.current().nextInt(courses.size()));
                ConcurrentHashMap<Integer, Course> courseCollection = client.getCourseCollection();
                if (course != null) {
                    courseCollection.put(courseCollection.size() + 1 , course); 
                    blackhole.consume(courseCollection);
                    blackhole.consume(courses);
                    blackhole.consume(course);
                }
            }
        }
        @Benchmark @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS)
        @Fork(value = 1)
        @Warmup(iterations = 5, time = 5)
        @Measurement(iterations = 5, time = 2)
        @Threads(50)
        public void benchmarkHighWriteHighRead(Blackhole blackhole) {
            clientThread.run(); 
            List<Course> courses = client.getCourses();
            Course course = courses.get(ThreadLocalRandom.current().nextInt(courses.size()));
            ConcurrentHashMap<Integer, Course> courseCollection = client.getCourseCollection();
            if (course != null) {
                courseCollection.put(courseCollection.size() + 1 , course); 
                blackhole.consume(courseCollection);
                blackhole.consume(courses);
                blackhole.consume(course);
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .threads(5)
                .build();

        new Runner(opt).run();
    }

}
