package com.tjit.sms;

import com.tjit.sms.dao.StudentDAO;
import com.tjit.sms.entity.Student;

import java.util.Scanner;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static StudentDAO dao;

    public static void main(String[] args) {
        try {
            dao = new StudentDAO();
        } catch (Exception e) {
            System.out.println("Could not connect to the database: " + e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewStudent();
                    case 3 -> updateStudent();
                    case 4 -> deleteStudent();
                    case 5 -> running = false;
                    default -> System.out.println("Invalid choice. Please enter 1-5.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }

        dao.close();
        System.out.println("\nThank you for using Student Management System!");
        System.out.println("Application closed.");
    }

    private static void printMenu() {
        System.out.println("\n====================================");
        System.out.println("       STUDENT MANAGEMENT SYSTEM");
        System.out.println("====================================\n");
        System.out.println("1. Add Student");
        System.out.println("2. View Student");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Exit\n");
    }

    private static void addStudent() {
        String name = readText("Enter Student Name: ");
        String email = readText("Enter Email: ");
        String course = readText("Enter Course: ");
        String phone = readText("Enter Phone: ");

        Long id = dao.addStudent(new Student(name, email, course, phone));
        System.out.println("\nStudent added successfully!");
        System.out.println("Student ID: " + id);
    }

    private static void viewStudent() {
        Long id = readLong("Enter Student ID: ");
        Student s = dao.findStudent(id);

        if (s == null) {
            System.out.println("\nStudent not found with ID: " + id);
            return;
        }
        System.out.println("\nStudent Details");
        System.out.println("-----------------------------");
        System.out.println("ID      : " + s.getId());
        System.out.println("Name    : " + s.getName());
        System.out.println("Email   : " + s.getEmail());
        System.out.println("Course  : " + s.getCourse());
        System.out.println("Phone   : " + s.getPhone());
        System.out.println("-----------------------------");
    }

    private static void updateStudent() {
        Long id = readLong("Enter Student ID: ");
        String newCourse = readText("\nEnter New Course: ");
        String newPhone = readText("Enter New Phone: ");

        if (dao.updateStudent(id, newCourse, newPhone)) {
            System.out.println("\nStudent updated successfully!");
        } else {
            System.out.println("\nStudent not found with ID: " + id);
        }
    }

    private static void deleteStudent() {
        Long id = readLong("Enter Student ID: ");

        if (dao.deleteStudent(id)) {
            System.out.println("\nStudent deleted successfully!");
        } else {
            System.out.println("\nStudent not found with ID: " + id);
        }
    }

    // ---------- input helpers ----------
    private static String readText(String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = scanner.nextLine().trim();
            if (value.isEmpty()) System.out.println("Input cannot be empty.");
        } while (value.isEmpty());
        return value;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static Long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric ID.");
            }
        }
    }
}