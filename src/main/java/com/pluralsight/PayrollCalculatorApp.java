package com.pluralsight;

import java.io.*;
import java.util.Scanner;

public class PayrollCalculatorApp {

    public static void main(String[] args) {
        /// list of available files inside project
        String[] availableFiles = {
                "employees.csv",
                "inventory.csv",
                "products.csv",
        };
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name of the file to process: ");
        String userInput = scanner.nextLine().trim();

        // prompt the name of the payroll file to create[cite:24]
        System.out.println("Enter the name of the payroll file to create: ");
        String outputFile = scanner.nextLine().trim();

        // close scanner
        scanner.close();

        // validate user input using equalsignorecase
        String selectedFile = null;
        for (String file : availableFiles) {
            if (file.equalsIgnoreCase(userInput)) {
                selectedFile = file;
                break;

            }
        }
        // handle invalid input
        if (selectedFile == null) {
            System.out.println("ERROR: Could not find file matching" + " " + userInput + "." + " " + "Please check your spelling!");
            return;
        }
        // TRY with resources
        try (BufferedReader bufReader = new BufferedReader(new FileReader(selectedFile));
        BufferedWriter bufWriter = new BufferedWriter(new FileWriter(outputFile))) {

    String input;
        //  print visual table header to the console
            System.out.println("\n--- Payroll Processing Report ---");
            System.out.printf("%-10s %-20s %-10s%n", "ID", "Name", "Gross Pay");
            System.out.println("------------------------------------------");

        // header for out new payroll file
            bufWriter.write("ID|NAME|GROSS PAY\n");



            // read line by line until NULL
            while ((input = bufReader.readLine()) != null) {
                // skip empty lines or header text if any exist in the file
                if (input.trim().isEmpty() || input.toLowerCase().contains("id") || input.toLowerCase().contains("hours")) {
                    continue; // skip this iteration and move to the next line
                }
                // split line by the pipe character |
                String[] tokens = input.split("\\|");
                // ensure the line has all 4 expected columns before parsing
                if (tokens.length < 4) {
                    continue;
                }
                // parse tokens into variables matching employee
                int id = Integer.parseInt(tokens[0]);
                String name = tokens[1];
                double hoursWorked = Double.parseDouble(tokens[2]);
                double payRate = Double.parseDouble(tokens[3]);

                // create a new employee object with parsed data
                Employee employee = new Employee(id, name, hoursWorked, payRate);

                /// display formatted table row on the console
                System.out.printf("%-10d %-20s $%-9.2f%n",
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getGrossPay());

                // formatted new string for the new file: ID|NAME|GROSS PAY
                String linetoWrite = String.format("%d|%s|%.2f%n",
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getGrossPay());
                // write thr formatted line to our new file
                bufWriter.write(linetoWrite);
            }

            System.out.println("------------------------------------------");
            System.out.println("SUCCESS: Payroll report has been written to " + outputFile);

        } catch (IOException e) {
            System.out.println("ERROR: Could not read file" + selectedFile + ".");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("ERROR: File contains invalid numerical data.");

        }
    }
}
