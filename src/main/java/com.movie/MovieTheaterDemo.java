package com.movie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class MovieTheaterDemo {
    public static void main(String[] args) {
        MovieTheater movieTheater = new MovieTheater();
        // Create mappings for 2D array indices -> [0][0] represents seat A1
        movieTheater.createColMap();
        movieTheater.createRowMap();
        String result = "";
        try (Scanner in = new Scanner(new File(args[0]));
             PrintWriter pw = new PrintWriter(new FileOutputStream("Output.txt", false))) {
            while(in.hasNextLine()) {
                String line = in.nextLine();
                String[] wordList = line.split(" ");
                if (wordList.length == 2)
                    result = movieTheater.getSeats(wordList[1]);
                else
                    result = "Error: Malformed input.";
                pw.append(wordList[0] + " " + result + '\n');
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found.");
        }
        movieTheater.printSeatingMap();
        System.out.println("/Users/lon/Documents/GitHub/MovieTheater/Output.txt");
    }
}
