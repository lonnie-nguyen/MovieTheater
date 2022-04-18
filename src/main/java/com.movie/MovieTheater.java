package com.movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MovieTheater {
    private int rows = 10;
    private int cols = 20;
    private int totalSeats = rows * cols;
    private int availSeats = totalSeats - getTotalNumOfSeatsInSafetyRows();
    private int[][] seatingMap = new int[rows][cols];
    private int safetyRow = 1;
    private int seatBuffer = 3;
    private Map<Integer, String> rowMap = new HashMap<>();
    private Map<Integer, Integer> colMap = new HashMap<>();

    /**
     * MovieTheater constructor
     */
    public MovieTheater() {
        makeSeatingMap(seatingMap);
    }

    /* Utility methods */

    /**
     * Each index of the 2D array is a possible seat. The method fills rows of seatingMap from
     * left to right with 0's, where a 0 represents that a seat is not reserved or used as a buffer.
     * The method also fills seatingMap with 3's (an arbitrary number) to indicate safety rows.
     * (Eventually, 1 will be used to represent that a seat is reserved and 3 if it is used as a
     * seat buffer.)
     * @param seatingMap 2D array
     */
    public void makeSeatingMap (int[][] seatingMap) {
        int fillSafetyRow = safetyRow + 1, rowCount = 0;
        for (int[] row: seatingMap) {
            if (rowCount % fillSafetyRow == 0)
                Arrays.fill(row, 0);
            else
                Arrays.fill(row, 3);
            ++rowCount;
        }
    }

    /**
     * Print method for seatingMap
     */
    public void printSeatingMap() {
        for (int[] row: seatingMap)
            // Convert each row -> String and print with newline
            System.out.println(Arrays.toString(row));
        System.out.println();
    }

    /**
     * Getter method for 2D array seatingMap
     * @return seatingMap
     */
    public int[][] getSeatingMap() {
        return seatingMap;
    }

    /**
     * Helper method to calculate the total number of seats in the safety rows.
     * @return Amount of seats in the safety rows
     */
    public int getTotalNumOfSeatsInSafetyRows() {
        return (rows / 2) * cols;
    }

    /**
     * Initializes rowMap, which holds mappings for integers to letter rows.
     */
    public void createRowMap() {
        char value = 'A';
        for (int key = 0; key < rows; ++key) {
            rowMap.put(key, Character.toString(value));
            ++value;
        }
    }

    /**
     * Initializes colMap, which holds mappings for the offset numbering of columns.
     */
    public void createColMap() {
        int value = 1;
        for (int key = 0; key < cols; ++key) {
            colMap.put(key, value);
            ++value;
        }
    }

    /**
     * Maps 2D array output to appropriate seat numbering and appends a comma and space. ex. 0 0 -> A1
     * @param resultList Arraylist of strings which denote the index location of seats ex. ["0 0", "0 1", "0 2"]
     * @return String of comma delimited assigned seats
     */
    public String mapResults(ArrayList<String> resultList) {
        String result = "";
        for (String res : resultList) {
            String[] resultArr = res.split(" ");
            String mappedRes = rowMap.get(Integer.parseInt(resultArr[0])) + colMap.get(Integer.parseInt(resultArr[1]));
            result += mappedRes + ", ";
        }
        return result;
    }

    /* Methods used for determining seating allocations */

    /**
     * Called by main method to initiate the process of getting seats. Seating is assigned on a first come, first
     * served basis. Large groups are kept together until there is no more consecutive seating available.
     * @param seats Number of requested seats
     * @return String of comma-delimited list of assigned seats or Error message if request cannot be fulfilled
     */
    public String getSeats(String seats) {
        int numOfSeats;
        try {
            numOfSeats = Integer.parseInt(seats);
        } catch (NumberFormatException e) {
            return "Error: Malformed input.";
        }
        String result = "";
        ArrayList<String> resultList;
        // Check if requested number of seats can be fulfilled
        if (numOfSeats > availSeats)
            return "No reservation made. Amount of seats requested exceeds available seats. ";

        // Start the seat assignment process:
        // First check for consecutive seats -> Requirement: customer satisfaction
        String availSeatLoc = findConsecutiveSeats((numOfSeats));
        String[] asl = availSeatLoc.split(" ");
        int r = Integer.parseInt(asl[0]);
        int c = Integer.parseInt(asl[1]);

        // Consecutive seats found -> call assignSeats
        if (r != -1) {
            resultList = assignSeats(r, c, numOfSeats);
            result = mapResults(resultList);
            return result.substring(0, result.length() - 2); // Use substring to concatenate end comma and space
        } else if (numOfSeats <= availSeats) { // No consecutive seats left to accommodate groups
            // Seating is now slim pickings, split up groups into remaining seats
            resultList = fillRemainingSeats(numOfSeats);
            result = mapResults(resultList);
            return result.substring(0, result.length() - 2);
        }
        return "No reservation made. Amount of seats requested exceeds available seats. ";
    }

    /**
     * Finds consecutive seating. Method starts searching for consecutive seating at location [0][0] of
     * seatingMap. Rows are traversed from left to right. If the number of consecutive seats cannot be
     * accommodated in a partially filled row, the method will advance to the next empty row and start counting
     * consecutive seats. Want to prioritize consecutively seating a large group in a new row before splitting
     * them up. Once consecutive seating is found, method will return the beginning position of the
     * consecutive interval immediately. If no consecutive seating is found, "-1 -1" will be returned, which is
     * recognized by getSeats.
     * @param numOfSeats Number of requested seats
     * @return String location of the start of the consecutive seats or "-1 -1" to indicate no availability
     */
    public String findConsecutiveSeats(int numOfSeats) {
        int consecSeatCount = 0;
        // Variable pointerRow will "point" to the first row position of the consecutive interval
        int pointerRow = 0;
        for (int row = 0; row < seatingMap.length; row += (1 + safetyRow)) {
            // Variable pointerCol will "point" to the first col position of the consecutive interval
            int pointerCol = 0;
            for (int col = 0; col < seatingMap[row].length; ++col) {
                if (seatingMap[row][col] == 0) {
                    ++consecSeatCount;
                    if (consecSeatCount == 1) { // Start of consecSeatCount set pointers
                        pointerRow = row;
                        pointerCol = col;
                    }

                    if (consecSeatCount == numOfSeats) { // Number of consecutive seats found
                        return pointerRow + " " + (pointerCol); // Immediately return the location of pointer
                    } else if (col == (cols - 1) && consecSeatCount < cols) { // Group cannot fit in current row
                        consecSeatCount = 0; // Reached end of a row, reset counter for next row
                    }
                } else if (seatingMap[row][col] == 1 || seatingMap[row][col] == 3) {
                    consecSeatCount = 0; // Encountered a non-reservable seat, reset consecSeatCount to 0
                }
            }
        }
        return "-1 -1"; // No consecutive seats found given numOfSeats
    }

    /**
     * Method starts assigning seating at the specified locations of r and c. Rows are traversed from left to right.
     * 0's in the 2D array are changed to 1's. Location of seats are added to an ArrayList. To assign safety buffer
     * seats, method calls addSafetySeatBuffer.
     * @param r Beginning row index
     * @param c Beginning column index
     * @param numOfSeats Number of requested seats
     * @return ArrayList of index (seating) locations from seatingMap
     */
    public ArrayList<String> assignSeats(int r, int c, int numOfSeats) {
        ArrayList<String> result = new ArrayList<>();
        int seatQty = numOfSeats;
        // Traverse seatingMap via row-major order, skipping every other row as safety requirement
        mainLoop:
        for (int row = r; row < seatingMap.length; row += (1 + safetyRow)) {
            for (int col = c; col < seatingMap[row].length; ++col) {
                seatingMap[row][col] = 1;
                result.add(row + " " + col);
                --seatQty;
                if (seatQty == 0) {
                    availSeats -= numOfSeats;
                    // Handle 3 seat buffer allocation
                    addSafetySeatBuffer(row, col, seatBuffer);
                    break mainLoop;
                }
            }
        }
        return result;
    }

    /**
     * Adds safety buffer seats after the last assigned seat, space permitting.
     * @param row Beginning row index
     * @param col Beginning column index
     * @param seatBuffer Globally defined variable of the class
     */
    public void addSafetySeatBuffer(int row, int col, int seatBuffer) {
        ++col; // Advance col
        int seatQty = seatBuffer;
        // Traverse seatingMap via row-major order
        mainLoop:
        for (int r = row; r < seatingMap.length; ++r) {
            for (int c = col; c < seatingMap[r].length; ++c) {
                seatingMap[r][c] = seatBuffer; // 3 is an arbitrary number, any value other than 0 will work
                --availSeats;
                --seatQty;
                // Create buffer of 3 seats unless the current position is the last seat in a row
                if (seatQty == 0 || c == (cols - 1))
                    break mainLoop;
            }
        }
    }

    /**
     * Method will fill the remaining seats left by splitting up groups. Seats are filled from left to right
     * and from the top to bottom row. Safety buffers will be added as needed.
     * @param numOfSeats Number of requested seats
     * @return ArrayList of index (seating) locations from seatingMap
     */
    public ArrayList<String> fillRemainingSeats(int numOfSeats) {
        ArrayList<String> result = new ArrayList<>();
        // Variable count cross-referenced with numOfSeats
        int count = 0;
        mainLoop:
        for (int row = 0; row < seatingMap.length; row += (1 + safetyRow)) {
            for (int col = 0; col < seatingMap[row].length; ++col) {
                if (seatingMap[row][col] == 0) {
                    seatingMap[row][col] = 1;
                    result.add(row + " " + col);
                    ++count;
                    if (count == numOfSeats) {
                        availSeats -= numOfSeats;
                        // Add safety seat buffer as needed
                        addSafetySeatBuffer(row, col, seatBuffer);
                        break mainLoop;
                    }
                }
            }
        }
        return result;
    }
}
