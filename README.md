# JMH Results for a Concurrent System
Benchmarking custom concurrent hash map versus java.util.concurrent.HashMap. 

<html>
<head>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <h1>Using Java Microbenchmarking Harness for Benchmarking a Concurrent Client-Server Course Scheduling System</h1>
    <h3> Written by Gregory Maldonado </h3>
    <p> 
        The system being tested is a simulation of a client-server course scheduling system. With respect to the system, a client requests courses from the server and can add the course to a schedule. If the 
        client does not want the course anymore, the client and remove the course from the schedule and send it back to the server. This is a simulation of a student registering for a course and if the student wants
        to deregister from the course, another student can register for the course. <br>
        This is a concurrent system is which a client represents a thread and all threads share a collection of courses. All clients can read from the data store at the same time but only one
        client can write to the data store at once. The benchmarks are measuring the performance of a custom implementation of a shared data store versus the implementation of a concurrent hashmap found
        with in the java.util.concurrent library. The following data was collected with Java Microbenchmark Harness (JMH). <br>
        The source code can be found on <a href="https://github.com/gmaldona/JMH-Results">Github. </a> 
    </p>
    <p> 
        <div>
        <h2> Java Microbenchmark Harness Running on a Core i5 with 10 Threads on MacOS </h2>
        <div class="row" style="margin: auto; padding: 100px; ">
            <div class="column" style="padding-right: 100px;">
                <img src="CSC375/data/JMH Running on MacOS with 10 Threads.png"/>
            </div>
            <div class="column" style="padding-left: 100px;">
                <img src="CSC375/data/JMH Running on MacOS with 10 Threads-2.png">
            </div>
        </div>
        </div>
        <div>
            <h2> Java Microbenchmark Harness Running on SUNY Oswego's Rho Server with 10 Threads on Linux </h2>
        <div class="row" style="margin: auto; padding: 100px; ">
            <div class="column" style="padding-right: 100px;">
                <img src="CSC375/data/JMH Running on Server Rho with 10 Threads.png"/>
            </div>
            <div class="column" style="padding-left: 100px;">
                <img src="CSC375/data/JMH Running on Server Rho with 10 Threads-2.png">
            </div>
        </div>
        </div>
        <div>
            <h2> Java Microbenchmark Harness Running on SUNY Oswego's Rho Server with 50 Threads on Linux </h2>
        <div class="row" style="margin: auto; padding: 100px; ">
            <div class="column" style="padding-right: 100px;">
                <img src="CSC375/data/JMH Running on Server Rho with 50 Threads.png"/>
            </div>
            <div class="column" style="padding-left: 100px;">
                <img src="CSC375/data/JMH Running on Server Rho with 50 Threads-2.png">
            </div>
        </div>
        </div>
    </p> 
</body>

</html>
