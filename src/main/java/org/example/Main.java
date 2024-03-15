package org.example;

import com.sun.source.tree.ClassTree;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    static Connection connection;

    public static void main(String[] args)
    {
        //<editor-fold desc="TA: Insert Info HERE!">
        String databaseName = "***";
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;
        String user = "postgres";
        String password = "****";
        //</editor-fold

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url,user,password);
        }catch(Exception e) {
            System.out.println("Problem interacting with Database! " + e.toString());
            return;
        }

        //Now user menu
        System.out.println(" *** Welcome to 3005 A3Q1 !  *** ");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("Please select an option:");
            System.out.println("1. Get all students");
            System.out.println("2. Add a student");
            System.out.println("3. Update student email");
            System.out.println("4. Delete a student");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    getAllStudents();
                    break;
                case 2:
                    System.out.print("Enter first name: ");
                    String firstName = scanner.next();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.next();
                    System.out.print("Enter email: ");
                    String email = scanner.next();
                    System.out.print("Enter enrollment date with format YYYY-MM-DD : ");
                    String enrollmentDate = scanner.next();

                    addStudent(firstName, lastName, email, enrollmentDate);
                    break;
                case 3:
                    System.out.print("Enter student ID: ");
                    int studentId = scanner.nextInt();
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.next();

                    updateStudentEmail(studentId, newEmail);
                    break;
                case 4:
                    System.out.print("Enter student ID: ");
                    int studentIdToDelete = scanner.nextInt();
                    deleteStudent(studentIdToDelete);

                    break;
                case 0:
                    System.out.println("0 selected, Exiting");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();


    }

    //REQUIRED FUNCTIONS

    static void getAllStudents()
    {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            System.out.println("------GETTING ALL STUDENTS FROM DATABSE----------");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("student_id"));
                System.out.println("Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Enrollment Date: " + resultSet.getDate("enrollment_date"));
                System.out.println("***");
            }
            System.out.println("------------------------------");
        } catch (Exception e) {
            System.out.println("Error getting all students from database: " + e.getMessage());
        }

    }

    static void addStudent(String firstName, String lastName, String Email, String enDate) {
        System.out.println(" --- Adding student with:");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + Email);
        System.out.println("EnrolmentDate: " +enDate);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO students (first_name, last_name, email, enrollment_date) ")
                    .append("SELECT '").append(firstName).append("', '").append(lastName)
                    .append("', '").append(Email).append("', '").append(enDate).append("'");

            String query = sb.toString();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding student to database: " + e.getMessage());
        }
    }

    static void updateStudentEmail(int student_id, String new_email) {
        System.out.println(" --- Updating email for student with ID: " + student_id);
        System.out.println("New Email: " + new_email);

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE students SET email = '").append(new_email).append("' ");
            sb.append("WHERE student_id = ").append(student_id);

            Statement statement = connection.createStatement();
            int studentsAffected = statement.executeUpdate(sb.toString());

            if (studentsAffected > 0) {
                System.out.println("Email updated successfully!");
            } else {
                System.out.println("No student found with ID: " + student_id);
            }
        } catch (Exception e) {
            System.out.println("Error updating student email: " + e.getMessage());
        }
    }

    static void deleteStudent(int student_id) {
        System.out.println(" --- Deleting student with ID: " + student_id);

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM students WHERE student_id = ").append(student_id);

            Statement statement = connection.createStatement();
            int studentsAffected = statement.executeUpdate(sb.toString());

            if (studentsAffected > 0) {
                System.out.println("Student with ID " + student_id + " deleted successfully!");
            } else {
                System.out.println("No student found with ID: " + student_id);
            }
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }


    ////









}