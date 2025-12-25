package utils;

import java.util.Scanner;

/** -----------A helper method for taking input(InputReader) using Scanner. ----------*/
public class ConsoleInputReader implements InputReader, AutoCloseable {

    private final Scanner scanner;

    /** Creates a new console input reader with System.in. */
    public ConsoleInputReader() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println(
                            "Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }
    //----reading contact
    public String readContact(String prompt) {
        String contact;

        while (true) {
            System.out.print(prompt);
            contact = scanner.nextLine().trim();

            // Accept digits only, length 10–15 (you can adjust)
            if (contact.matches("\\d{10,15}")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid contact number.");
            }
        }
        return contact;
    }


    @Override
    public double readDouble(String prompt, double min) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min) {
                    break;
                } else {
                    System.out.println(
                            "Invalid input. Please enter a number greater than or equal to " + min + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }

    public String readString(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = scanner.nextLine().trim();

            // checking for emptiness
            if (value.isEmpty()) {  // Changed: removed the '!'
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }

            // Check for numbers (Digits 0-9)
            if (value.matches(".*\\d.*")) {
                System.out.println("Error: Name cannot contain numbers. Please enter a valid name.");
                continue;
            }

            break;
        }
        return value;
    }


    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}