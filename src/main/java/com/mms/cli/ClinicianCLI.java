package com.mms.cli;

import com.mms.controllers.MMSController;
import com.mms.exceptions.*;
import com.mms.models.*;
import java.util.List;
import java.util.Scanner;

/**
 * ClinicianCLI handles the clinician menu and clinician operations.
 */
public class ClinicianCLI {
    private Clinician clinician;
    private MMSController controller;
    private Scanner scanner;
    private boolean isLoggedIn;

    public ClinicianCLI(Clinician clinician, Scanner scanner) {
        this.clinician = clinician;
        this.controller = MMSController.getInstance();
        this.scanner = scanner;
        this.isLoggedIn = true;
    }

    public void start() {
        while (isLoggedIn) {
            clinician.showMenu();
            String choice = scanner.nextLine().trim();
            processChoice(choice);
        }
    }

    private void processChoice(String choice) {
        try {
            switch (choice) {
                case "1":
                    viewAssignedPatients();
                    break;
                case "2":
                    recordTreatment();
                    break;
                case "3":
                    updateTreatmentStatus();
                    break;
                case "4":
                    logout();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAssignedPatients() {
        List<Treatment> assignedTreatments = controller.getClinicianTreatments(clinician.getId());
        if (assignedTreatments.isEmpty()) {
            System.out.println("No patients assigned yet.");
            return;
        }
        
        System.out.println("\n====== ASSIGNED PATIENTS ======");
        for (Treatment treatment : assignedTreatments) {
            try {
                Patient patient = controller.getPatient(treatment.getPatientId());
                System.out.println("Patient: " + patient.getName() + " - Treatment: " + treatment);
            } catch (UserNotFoundException e) {
                System.out.println("Patient not found: " + e.getMessage());
            }
        }
        System.out.println("================================");
    }

    private void recordTreatment() throws TreatmentNotFoundException {
        System.out.print("Enter treatment ID: ");
        String treatmentId = scanner.nextLine().trim();
        System.out.print("Enter treatment notes: ");
        String notes = scanner.nextLine().trim();
        
        Treatment treatment = controller.getTreatment(treatmentId);
        treatment.setNotes(notes);
        System.out.println("✓ Treatment notes recorded");
    }

    private void updateTreatmentStatus() throws TreatmentNotFoundException, InvalidInputException {
        System.out.print("Enter treatment ID: ");
        String treatmentId = scanner.nextLine().trim();
        System.out.println("Select new status:");
        System.out.println("1. NEW_TREATMENT");
        System.out.println("2. TREATMENT_ASSESSED");
        System.out.println("3. BILL_GENERATED");
        System.out.println("4. COMPLETED");
        System.out.println("5. PAID");
        System.out.print("Select: ");
        String statusChoice = scanner.nextLine().trim();
        
        TreatmentStatus status = switch (statusChoice) {
            case "1" -> TreatmentStatus.NEW_TREATMENT;
            case "2" -> TreatmentStatus.TREATMENT_ASSESSED;
            case "3" -> TreatmentStatus.BILL_GENERATED;
            case "4" -> TreatmentStatus.COMPLETED;
            case "5" -> TreatmentStatus.PAID;
            default -> throw new InvalidInputException("Invalid status selection");
        };
        
        controller.updateTreatmentStatus(treatmentId, status);
        System.out.println("✓ Treatment status updated to " + status.getDisplayName());
    }

    private void logout() {
        System.out.println("✓ Logged out successfully");
        isLoggedIn = false;
    }
}
