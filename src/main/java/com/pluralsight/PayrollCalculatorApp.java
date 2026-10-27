package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class PayrollCalculatorApp {

    public static void main(String[] args) {
        /// list of available files inside project
        String[] availableFiles = {
                "employees"
        };
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the file to process: ");
        String userInput = scanner.nextLine().trim();

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
        try (BufferedReader bufReader = new BufferedReader(new FileReader(selectedFile))) {
            String input;
            System.out.println("\n--- Payroll Processing Report ___");
            System.out.printf("%-10s %-20s %-10s%n", "ID", "Name", "Gross Pay");
            System.out.println("------------------------------------------");

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

                // display formatted employee details
                System.out.printf("%-10d %-20s $%-9.2f%n",
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getGrossPay());
            }

        } catch (IOException e) {
            System.out.println("ERROR: Could not read file" + selectedFile + ".");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("ERROR: File contains invalid numerical data.");

        }
    }
}
