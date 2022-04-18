package com.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MovieTheaterTest {
    private MovieTheater movieTheater;
    @BeforeEach
    void setUp() {
        movieTheater = new MovieTheater();
    }

    @Test
    void testGetSeatsWithBadInput() {
        String actual = movieTheater.getSeats("#");
        String expected = "Error: Malformed input.";
        assertEquals(expected, actual);
    }

    @Test
    void testGetSeatsWhenNotEnoughSeats() {
        String actual = movieTheater.getSeats("101");
        String expected = "No reservation made. Amount of seats requested exceeds available seats. ";
        assertEquals(expected, actual);
    }

    @Test
    void testGetSeats() {
        ArrayList<String> input = new ArrayList<>(Arrays.asList("R001 5", "R002 3", "R003 1", "R004 1"));
        ArrayList<String> actual = new ArrayList<>();
        movieTheater.createColMap();
        movieTheater.createRowMap();
        String result = "";
        for(String s: input) {
            String[] str = s.split(" ");
            result = movieTheater.getSeats(str[1]);
            actual.add(str[0] + " " + result);
        }
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "R001 A1, A2, A3, A4, A5", "R002 A9, A10, A11", "R003 A15", "R004 A19"));
        assertEquals(expected, actual);
    }

    @Test
    void testFindConsecutiveSeats() {
        ArrayList<String> input = new ArrayList<>(Arrays.asList("R001 5", "R002 3", "R003 1", "R004 1"));
        movieTheater.createColMap();
        movieTheater.createRowMap();
        // Populate seating map
        for(String s: input) {
            String[] str = s.split(" ");
            movieTheater.getSeats(str[1]);
        }
        // Return index (in the form of a string) of first consecutive seat
        String actual = movieTheater.findConsecutiveSeats(4);
        String expected = "2 0";
        assertEquals(expected, actual);
    }

    @Test
    void testAssignSeats() {
        ArrayList<String> input = new ArrayList<>(Arrays.asList("R001 5", "R002 3", "R003 1", "R004 1"));
        movieTheater.createColMap();
        movieTheater.createRowMap();
        // Populate seating map
        for (String s : input) {
            String[] str = s.split(" ");
            movieTheater.getSeats(str[1]);
        }
        // Return ArrayList of index (seating) locations from seatingMap
        ArrayList<String> actual = movieTheater.assignSeats(2, 0, 4);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "2 0", "2 1", "2 2", "2 3"));
        assertEquals(expected, actual);
    }
}