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

    public AdminCLI(Admin admin, Scanner scanner) throws StorageException {
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
        
        Patient newPatient = controller.registerPatientAndReturn(name, phone, email, password);
        System.out.println("\n✓ Patient registered successfully!");
        System.out.println("  Patient ID: " + newPatient.getId());
        System.out.println("  Name: " + newPatient.getName());
        System.out.println("  Email: " + newPatient.getEmail());
    }

    private void upgradePatient() throws UserNotFoundException, StorageException {
        System.out.print("Enter patient ID to upgrade: ");
        String patientId = scanner.nextLine().trim();
        controller.upgradePatient(patientId);
        System.out.println("✓ Patient upgraded to registered");
    }

    private void assignPatientToClinician() throws UserNotFoundException, TreatmentNotFoundException, StorageException {
        System.out.print("\nEnter treatment ID: ");
        String treatmentId = scanner.nextLine().trim();
        
        // Show available clinicians
        List<Clinician> clinicians = controller.getAllClinicians();
        if (clinicians.isEmpty()) {
            System.out.println("No clinicians available.");
            return;
        }
        
        System.out.println("\n📋 Available Clinicians:");
        for (Clinician c : clinicians) {
            System.out.printf("  - ID: %-20s Name: %-25s Specialization: %s\n", 
                c.getId(), c.getName(), c.getSpecialization());
        }
        
        System.out.print("\nEnter clinician ID: ");
        String clinicianId = scanner.nextLine().trim();
        controller.assignClinician(treatmentId, clinicianId);
        System.out.println("✓ Clinician assigned successfully");
    }

    private void addTreatmentType() throws InvalidInputException, StorageException {
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

    private void removeTreatmentType() throws TreatmentNotFoundException, StorageException {
        System.out.print("Enter treatment type ID to remove: ");
        String typeId = scanner.nextLine().trim();
        controller.removeTreatmentType(typeId);
        System.out.println("✓ Treatment type removed successfully");
    }

    private void generateBill() throws TreatmentNotFoundException, StorageException {
        System.out.print("Enter treatment ID: ");
        String treatmentId = scanner.nextLine().trim();
        Bill bill = controller.generateBill(treatmentId);
        System.out.println("✓ Bill generated: " + bill);
    }

    private void recordPayment() throws TreatmentNotFoundException, StorageException {
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

    private void sendNotifications() throws UserNotFoundException, StorageException {
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
                viewPatients();
                break;
            case "2":
                viewTreatmentTypes();
                break;
            case "3":
                viewBills();
                break;
            case "4":
                viewTreatments();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private void viewPatients() {
        List<Patient> patients = controller.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("\nNo patients found.");
            return;
        }
        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         ALL PATIENTS                             ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        for (Patient p : patients) {
            System.out.printf("║ ID: %-20s Name: %-30s ║\n", p.getId(), p.getName());
            System.out.printf("║ Email: %-20s Phone: %-30s ║\n", p.getEmail(), p.getPhone());
            System.out.printf("║ Registered: %-10s Flagged: %-10s Promotions: %-10s ║\n", 
                p.isRegistered(), p.isFlagged(), p.isOptedInForPromotions());
            System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        }
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }

    private void viewTreatmentTypes() {
        List<TreatmentType> types = controller.getAllTreatmentTypes();
        if (types.isEmpty()) {
            System.out.println("\nNo treatment types found.");
            return;
        }
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║                 TREATMENT TYPES                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        for (TreatmentType t : types) {
            System.out.printf("║ ID: %-20s Name: %-25s ║\n", t.getId(), t.getName());
            System.out.printf("║ Price: $%.2f                                           ║\n", t.getPrice());
            System.out.println("╠═══════════════════════════════════════════════════════╣");
        }
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    private void viewBills() {
        List<Bill> bills = controller.getAllBills();
        if (bills.isEmpty()) {
            System.out.println("\nNo bills found.");
            return;
        }
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         ALL BILLS                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
        for (Bill b : bills) {
            System.out.printf("║ Bill ID: %-25s Amount: $%.2f ║\n", b.getBillId(), b.getTotalAmount());
            System.out.printf("║ Patient ID: %-35s ║\n", b.getPatientId());
            System.out.printf("║ Status: %s                                              ║\n", 
                b.isPaid() ? "PAID" : "PENDING");
            System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
        }
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
    }

    private void viewTreatments() {
        System.out.println("\nView treatments for patient?");
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine().trim();
        List<Treatment> treatments = controller.getPatientTreatments(patientId);
        if (treatments.isEmpty()) {
            System.out.println("No treatments found for this patient.");
            return;
        }
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    PATIENT TREATMENTS                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
        for (Treatment t : treatments) {
            System.out.printf("║ Treatment ID: %-29s ║\n", t.getTreatmentId());
            System.out.printf("║ Status: %-45s ║\n", t.getStatus().getDisplayName());
            System.out.printf("║ Clinician: %-44s ║\n", 
                t.getClinicianId() != null ? t.getClinicianId() : "Not assigned");
            System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
        }
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
    }

    private void logout() {
        System.out.println("✓ Logged out successfully");
        isLoggedIn = false;
    }
}
