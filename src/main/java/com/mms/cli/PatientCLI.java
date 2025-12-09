package com.mms.cli;

import com.mms.controllers.MMSController;
import com.mms.exceptions.*;
import com.mms.models.*;
import java.util.List;
import java.util.Scanner;

/**
 * PatientCLI handles the patient menu and patient operations.
 */
public class PatientCLI {
    private Patient patient;
    private MMSController controller;
    private Scanner scanner;
    private boolean isLoggedIn;

    public PatientCLI(Patient patient, Scanner scanner) {
        this.patient = patient;
        this.controller = MMSController.getInstance();
        this.scanner = scanner;
        this.isLoggedIn = true;
    }

    public void start() {
        while (isLoggedIn) {
            patient.showMenu();
            String choice = scanner.nextLine().trim();
            processChoice(choice);
        }
    }

    private void processChoice(String choice) {
        try {
            switch (choice) {
                case "1":
                    bookTreatment();
                    break;
                case "2":
                    viewTreatmentStatus();
                    break;
                case "3":
                    viewBills();
                    break;
                case "4":
                    togglePromotions();
                    break;
                case "5":
                    logout();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void bookTreatment() throws UserNotFoundException, TreatmentNotFoundException, InvalidInputException {
        if (!patient.isRegistered()) {
            System.out.println("You must be registered to book a treatment. Please contact admin.");
            return;
        }
        
        System.out.println("\n====== AVAILABLE TREATMENTS ======");
        List<TreatmentType> types = controller.getAllTreatmentTypes();
        for (TreatmentType type : types) {
            System.out.println(type.getId() + ": " + type.getName() + " - $" + type.getPrice());
        }
        System.out.println("==================================");
        
        System.out.print("Enter treatment type ID to book: ");
        String treatmentTypeId = scanner.nextLine().trim();
        
        controller.bookTreatment(patient.getId(), treatmentTypeId);
        System.out.println("✓ Treatment booked successfully");
    }

    private void viewTreatmentStatus() {
        List<Treatment> treatments = controller.getPatientTreatments(patient.getId());
        if (treatments.isEmpty()) {
            System.out.println("No treatments booked yet.");
            return;
        }
        
        System.out.println("\n====== YOUR TREATMENTS ======");
        for (Treatment treatment : treatments) {
            System.out.println(treatment);
        }
        System.out.println("=============================");
    }

    private void viewBills() {
        List<Bill> bills = controller.getPatientBills(patient.getId());
        if (bills.isEmpty()) {
            System.out.println("No bills yet.");
            return;
        }
        
        System.out.println("\n====== YOUR BILLS ======");
        double totalAmount = 0;
        for (Bill bill : bills) {
            System.out.println(bill);
            totalAmount += bill.getTotalAmount();
        }
        System.out.println("Total: $" + totalAmount);
        System.out.println("========================");
    }

    private void togglePromotions() {
        patient.togglePromotions();
        String status = patient.isOptedInForPromotions() ? "enabled" : "disabled";
        System.out.println("✓ Promotional notifications " + status);
    }

    private void logout() {
        System.out.println("✓ Logged out successfully");
        isLoggedIn = false;
    }
}
