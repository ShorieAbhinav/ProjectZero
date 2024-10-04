import java.io.*;
import java.util.*;

public class Main {

    // Max constants for pilots and coordinates
    private static final int MAX_PILOTS = 20;
    private static final int MAX_COORDINATES = 16;

    public static void main(String[] args) {
        // 3D array to store coordinates for each pilot
        double[][][] coordinates = new double[MAX_PILOTS][MAX_COORDINATES][2];
        // Array to store pilot names
        String[] pilotNames = new String[MAX_PILOTS];

        // Read the input file and store the data
        int numberOfPilots = readInputFile(coordinates, pilotNames);

        // Calculate areas for each pilot and write to output file
        writeOutputFile(coordinates, pilotNames, numberOfPilots);
    }

    // Read input file and store data in arrays
    public static int readInputFile(double[][][] coordinates, String[] pilotNames) {
        // Create the scanner to read from the console
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the name of the input file: ");
        String fileName = scanner.nextLine();

        int pilotCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null && pilotCount < MAX_PILOTS) {
                int i = 0;
                // Find the pilot's name before the first space
                while (i < line.length() && line.charAt(i) != ' ') {
                    i++;
                }
                // Extract the pilot name
                pilotNames[pilotCount] = line.substring(0, i);

                // Move past the space after the name
                i++;

                // Manually parse coordinates (x,y pairs)
                int coordCount = 0;
                while (i < line.length() && coordCount < MAX_COORDINATES) {
                    // Read x value (up to comma)
                    StringBuilder xValue = new StringBuilder();
                    while (i < line.length() && line.charAt(i) != ',') {
                        xValue.append(line.charAt(i));
                        i++;
                    }
                    i++; // Skip the comma

                    // Read y value (up to space or end of line)
                    StringBuilder yValue = new StringBuilder();
                    while (i < line.length() && line.charAt(i) != ' ') {
                        yValue.append(line.charAt(i));
                        i++;
                    }
                    i++; // Skip the space

                    // Store the coordinates
                    coordinates[pilotCount][coordCount][0] = Double.parseDouble(xValue.toString());
                    coordinates[pilotCount][coordCount][1] = Double.parseDouble(yValue.toString());
                    coordCount++;
                }
                pilotCount++;
            }
        } catch (IOException e) {
            System.out.println("Oops! There was an error reading the file: " + e.getMessage());
        }

        // Close the scanner after getting input from the user
        scanner.close();

        return pilotCount;
    }

    // Calculate the area for a given pilot's coordinates
    public static double calculateArea(double[][] pilotCoordinates, int numCoords) {
        double area = 0.0;
        for (int i = 0; i < numCoords - 1; i++) {
            double x1 = pilotCoordinates[i][0];
            double y1 = pilotCoordinates[i][1];
            double x2 = pilotCoordinates[i + 1][0];
            double y2 = pilotCoordinates[i + 1][1];

            area += (x1 + x2) * (y2 - y1);
        }
        return Math.abs(area / 2.0);
    }

    // Write the output (pilot names and areas) to a file
    public static void writeOutputFile(double[][][] coordinates, String[] pilotNames, int numberOfPilots) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("pilot_areas.txt"))) {
            for (int i = 0; i < numberOfPilots; i++) {
                int numCoords = countCoordinates(coordinates[i]);
                double area = calculateArea(coordinates[i], numCoords);

                // Write to file in the required format
                writer.printf("%s\t%.2f%n", pilotNames[i], area);
            }
            System.out.println("File 'pilot_areas.txt' has been successfully generated.");
        } catch (IOException e) {
            System.out.println("Error: Could not write to file. Please check if you have write permissions or if the file is open elsewhere.");
        }
    }

    // Count the number of coordinates for a pilot
    public static int countCoordinates(double[][] pilotCoordinates) {
        int count = 0;
        for (double[] coord : pilotCoordinates) {
            if (coord[0] != 0.0 || coord[1] != 0.0) {
                count++;
            }
        }
        return count;
    }
}
