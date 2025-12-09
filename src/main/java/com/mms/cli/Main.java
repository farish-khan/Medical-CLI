package com.mms.cli;

import com.mms.controllers.MMSController;
import com.mms.exceptions.UserNotFoundException;
import com.mms.models.*;
import java.util.Scanner;

/**
 * Main entry point for the MMS Medical Management System.
 * Handles authentication and main menu flow.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static MMSController controller;

    public static void main(String[] args) {
        try {
            controller = MMSController.getInstance();
            showWelcome();
            mainMenu();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void showWelcome() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  Myriad Medical Services (MMS)             ║");
        System.out.println("║  Medical Management System                 ║");
        System.out.println("║  Version 1.0                               ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║           MAIN MENU - LOGIN                ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║  1. Login as Admin                         ║");
            System.out.println("║  2. Login as Clinician                     ║");
            System.out.println("║  3. Login as Patient                       ║");
            System.out.println("║  4. Exit                                   ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("Select option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    loginAsAdmin();
                    break;
                case "2":
                    loginAsClinician();
                    break;
                case "3":
                    loginAsPatient();
                    break;
                case "4":
                    running = false;
                    System.out.println("\n✓ Thank you for using MMS. Goodbye!");
                    break;
                default:
                    System.out.println("✗ Invalid option. Please try again.");
            }
        }
    }

    private static void loginAsAdmin() {
        try {
            System.out.println("\n====== ADMIN LOGIN ======");
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            
            User user = controller.login(email, password);
            
            if (!(user instanceof Admin)) {
                System.out.println("✗ Invalid credentials for Admin account");
                return;
            }
            
            Admin admin = (Admin) user;
            System.out.println("✓ Login successful! Welcome " + admin.getName());
            AdminCLI adminCLI = new AdminCLI(admin, scanner);
            adminCLI.start();
        } catch (UserNotFoundException e) {
            System.out.println("✗ Login failed: " + e.getMessage());
        }
    }

    private static void loginAsClinician() {
        try {
            System.out.println("\n====== CLINICIAN LOGIN ======");
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            
            User user = controller.login(email, password);
            
            if (!(user instanceof Clinician)) {
                System.out.println("✗ Invalid credentials for Clinician account");
                return;
            }
            
            Clinician clinician = (Clinician) user;
            System.out.println("✓ Login successful! Welcome Dr. " + clinician.getName());
            ClinicianCLI clinicianCLI = new ClinicianCLI(clinician, scanner);
            clinicianCLI.start();
        } catch (UserNotFoundException e) {
            System.out.println("✗ Login failed: " + e.getMessage());
        }
    }

    private static void loginAsPatient() {
        try {
            System.out.println("\n====== PATIENT LOGIN ======");
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            
            User user = controller.login(email, password);
            
            if (!(user instanceof Patient)) {
                System.out.println("✗ Invalid credentials for Patient account");
                return;
            }
            
            Patient patient = (Patient) user;
            System.out.println("✓ Login successful! Welcome " + patient.getName());
            PatientCLI patientCLI = new PatientCLI(patient, scanner);
            patientCLI.start();
        } catch (UserNotFoundException e) {
            System.out.println("✗ Login failed: " + e.getMessage());
        }
    }
}
