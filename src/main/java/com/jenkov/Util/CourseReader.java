package com.jenkov.Util; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class CourseReader {

    public static File file;
    public static StringBuilder fileStr = new StringBuilder();

    private static boolean hasNext = true;

    public static void read() throws Exception {
        try {
            file = new File("./SUNYOswegoCourses.txt");
        } catch (Exception e) { }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String appendableString;
        while (hasNext) {
            appendableString = reader.readLine();
            if (appendableString == null) hasNext = false;
            if (hasNext) fileStr.append(appendableString).append("\n");
        }
    }

    public static String get() { return fileStr.toString(); }

}
