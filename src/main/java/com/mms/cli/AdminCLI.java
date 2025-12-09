package com.mms.cli;

import com.mms.controllers.MMSController;
import com.mms.exceptions.*;
import com.mms.models.*;
import java.util.List;
import java.util.Scanner;

/**
 * AdminCLI handles the admin menu and admin operations.
 */
public class AdminCLI {
    private Admin admin;
    private MMSController controller;
    private Scanner scanner;
    private boolean isLoggedIn;

    public AdminCLI(Admin admin, Scanner scanner) {
        this.admin = admin;
        this.controller = MMSController.getInstance();
        this.scanner = scanner;
        this.isLoggedIn = true;
    }

    public void start() {
        while (isLoggedIn) {
            admin.showMenu();
            String choice = scanner.nextLine().trim();
            processChoice(choice);
        }
    }

    private void processChoice(String choice) {
        try {
            switch (choice) {
                case "1":
                    registerNewPatient();
                    break;
                case "2":
                    upgradePatient();
                    break;
                case "3":
                    assignPatientToClinician();
                    break;
                case "4":
                    addTreatmentType();
                    break;
                case "5":
                    removeTreatmentType();
                    break;
                case "6":
                    generateBill();
                    break;
                case "7":
                    recordPayment();
                    break;
                case "8":
                    flagNonPayingPatient();
                    break;
                case "9":
                    sendNotifications();
                    break;
                case "10":
                    viewReports();
                    break;
                case "11":
                    logout();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registerNewPatient() throws InvalidInputException {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        controller.registerPatient(name, phone, email, password);
        System.out.println("✓ Patient registered successfully");
    }

    private void upgradePatient() throws UserNotFoundException {
        System.out.print("Enter patient ID to upgrade: ");
        String patientId = scanner.nextLine().trim();
        controller.upgradePatient(patientId);
        System.out.println("✓ Patient upgraded to registered");
    }

    private void assignPatientToClinician() throws UserNotFoundException, TreatmentNotFoundException {
        System.out.print("Enter treatment ID: ");
        String treatmentId = scanner.nextLine().trim();
        System.out.print("Enter clinician ID: ");
        String clinicianId = scanner.nextLine().trim();
        controller.assignClinician(treatmentId, clinicianId);
        System.out.println("✓ Clinician assigned successfully");
    }

    private void addTreatmentType() throws InvalidInputException {
        System.out.print("Enter treatment name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter price: ");
        try {
            double price = Double.parseDouble(scanner.nextLine().trim());
            controller.addTreatmentType(name, price);
            System.out.println("✓ Treatment type added successfully");
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid price format");
        }
    }

    private void removeTreatmentType() throws TreatmentNotFoundException {
        System.out.print("Enter treatment type ID to remove: ");
        String typeId = scanner.nextLine().trim();
        controller.removeTreatmentType(typeId);
        System.out.println("✓ Treatment type removed successfully");
    }

    private void generateBill() throws TreatmentNotFoundException {
        System.out.print("Enter treatment ID: ");
        String treatmentId = scanner.nextLine().trim();
        Bill bill = controller.generateBill(treatmentId);
        System.out.println("✓ Bill generated: " + bill);
    }

    private void recordPayment() throws TreatmentNotFoundException {
        System.out.print("Enter bill ID: ");
        String billId = scanner.nextLine().trim();
        controller.recordPayment(billId);
        System.out.println("✓ Payment recorded successfully");
    }

    private void flagNonPayingPatient() throws UserNotFoundException {
        System.out.print("Enter patient ID to flag: ");
        String patientId = scanner.nextLine().trim();
        controller.flagPatient(patientId);
        System.out.println("✓ Patient flagged successfully");
    }

    private void sendNotifications() throws UserNotFoundException {
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine().trim();
        System.out.print("Enter message: ");
        String message = scanner.nextLine().trim();
        System.out.print("Is promotional (y/n): ");
        boolean isPromotional = scanner.nextLine().trim().equalsIgnoreCase("y");
        controller.sendNotification(patientId, message, isPromotional);
        System.out.println("✓ Notification sent successfully");
    }

    private void viewReports() {
        System.out.println("\n====== REPORTS ======");
        System.out.println("1. View all patients");
        System.out.println("2. View all treatment types");
        System.out.println("3. View all bills");
        System.out.println("4. View all treatments");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                List<Patient> patients = controller.getAllPatients();
                patients.forEach(System.out::println);
                break;
            case "2":
                List<TreatmentType> types = controller.getAllTreatmentTypes();
                types.forEach(System.out::println);
                break;
            case "3":
                List<Bill> bills = controller.getAllBills();
                bills.forEach(System.out::println);
                break;
            case "4":
                // Show all treatments
                System.out.println("(Treatments would be displayed here)");
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private void logout() {
        System.out.println("✓ Logged out successfully");
        isLoggedIn = false;
    }
}
