package com.mms.tests;

import com.mms.controllers.MMSController;
import com.mms.controllers.UserFactory;
import com.mms.exceptions.*;
import com.mms.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for MMS system functionality.
 */
@DisplayName("MMS Medical Management System Tests")
public class MMSSystemTest {
    private MMSController controller;

    @BeforeEach
    public void setUp() {
        controller = MMSController.getInstance();
    }

    // ===== PATIENT MANAGEMENT TESTS =====
    @Test
    @DisplayName("Should register a new patient successfully")
    public void testRegisterPatient() throws InvalidInputException {
        assertDoesNotThrow(() -> {
            controller.registerPatient("Jane Smith", "555-1234", "jane@email.com", "pass123");
        });
    }

    @Test
    @DisplayName("Should throw exception when registering patient with empty name")
    public void testRegisterPatientWithEmptyName() {
        assertThrows(InvalidInputException.class, () -> {
            controller.registerPatient("", "555-1234", "jane@email.com", "pass123");
        });
    }

    @Test
    @DisplayName("Should upgrade patient to registered")
    public void testUpgradePatient() throws UserNotFoundException, InvalidInputException {
        controller.registerPatient("John Test", "555-0001", "john.test@email.com", "test123");
        Patient patient = controller.getAllPatients()
                .stream()
                .filter(p -> p.getEmail().equals("john.test@email.com"))
                .findFirst()
                .orElseThrow();
        
        assertFalse(patient.isRegistered());
        controller.upgradePatient(patient.getId());
        assertTrue(patient.isRegistered());
    }

    @Test
    @DisplayName("Should throw exception when upgrading non-existent patient")
    public void testUpgradeNonExistentPatient() {
        assertThrows(UserNotFoundException.class, () -> {
            controller.upgradePatient("INVALID_ID");
        });
    }

    @Test
    @DisplayName("Should flag patient as non-paying")
    public void testFlagPatient() throws UserNotFoundException {
        Patient patient = controller.getAllPatients().get(0);
        assertFalse(patient.isFlagged());
        controller.flagPatient(patient.getId());
        assertTrue(patient.isFlagged());
    }

    // ===== TREATMENT TYPE TESTS =====
    @Test
    @DisplayName("Should add a new treatment type")
    public void testAddTreatmentType() throws InvalidInputException {
        int initialSize = controller.getAllTreatmentTypes().size();
        controller.addTreatmentType("New Treatment", 1500.0);
        assertEquals(initialSize + 1, controller.getAllTreatmentTypes().size());
    }

    @Test
    @DisplayName("Should throw exception when adding treatment type with invalid price")
    public void testAddTreatmentTypeWithInvalidPrice() {
        assertThrows(InvalidInputException.class, () -> {
            controller.addTreatmentType("Invalid Treatment", -100.0);
        });
    }

    @Test
    @DisplayName("Should remove a treatment type")
    public void testRemoveTreatmentType() throws InvalidInputException, TreatmentNotFoundException {
        controller.addTreatmentType("Removable Treatment", 500.0);
        String typeId = controller.getAllTreatmentTypes()
                .stream()
                .filter(t -> t.getName().equals("Removable Treatment"))
                .findFirst()
                .orElseThrow()
                .getId();
        
        int initialSize = controller.getAllTreatmentTypes().size();
        controller.removeTreatmentType(typeId);
        assertEquals(initialSize - 1, controller.getAllTreatmentTypes().size());
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent treatment type")
    public void testRemoveNonExistentTreatmentType() {
        assertThrows(TreatmentNotFoundException.class, () -> {
            controller.removeTreatmentType("INVALID_TYPE_ID");
        });
    }

    // ===== TREATMENT BOOKING TESTS =====
    @Test
    @DisplayName("Should throw exception when unregistered patient books treatment")
    public void testBookTreatmentAsUnregisteredPatient() throws InvalidInputException {
        // Register a new patient specifically for this test
        controller.registerPatient("Unreg Patient", "555-9999", "unreg.test123@test.com", "test");
        final Patient unregisteredPatient = controller.getAllPatients().stream()
                .filter(p -> p.getEmail().equals("unreg.test123@test.com"))
                .findFirst()
                .orElseThrow();
        
        String treatmentTypeId = controller.getAllTreatmentTypes().get(0).getId();
        
        assertThrows(InvalidInputException.class, () -> {
            controller.bookTreatment(unregisteredPatient.getId(), treatmentTypeId);
        });
    }

    @Test
    @DisplayName("Should book treatment for registered patient")
    public void testBookTreatmentAsRegisteredPatient() throws Exception {
        Patient patient = controller.getAllPatients().get(0);
        patient.upgrade();
        String treatmentTypeId = controller.getAllTreatmentTypes().get(0).getId();
        
        int initialSize = controller.getPatientTreatments(patient.getId()).size();
        controller.bookTreatment(patient.getId(), treatmentTypeId);
        assertEquals(initialSize + 1, controller.getPatientTreatments(patient.getId()).size());
    }

    // ===== ASSIGNMENT TESTS =====
    @Test
    @DisplayName("Should assign clinician to treatment")
    public void testAssignClinician() throws Exception {
        Patient patient = controller.getAllPatients().get(0);
        patient.upgrade();
        String treatmentTypeId = controller.getAllTreatmentTypes().get(0).getId();
        controller.bookTreatment(patient.getId(), treatmentTypeId);
        
        Treatment treatment = controller.getPatientTreatments(patient.getId()).get(0);
        String clinicianId = controller.getAllClinicians().get(0).getId();
        
        assertNull(treatment.getClinicianId());
        controller.assignClinician(treatment.getTreatmentId(), clinicianId);
        assertEquals(clinicianId, treatment.getClinicianId());
    }

    @Test
    @DisplayName("Should throw exception when assigning to non-existent clinician")
    public void testAssignNonExistentClinician() throws Exception {
        Patient patient = controller.getAllPatients().get(0);
        patient.upgrade();
        String treatmentTypeId = controller.getAllTreatmentTypes().get(0).getId();
        controller.bookTreatment(patient.getId(), treatmentTypeId);
        
        Treatment treatment = controller.getPatientTreatments(patient.getId()).get(0);
        
        assertThrows(UserNotFoundException.class, () -> {
            controller.assignClinician(treatment.getTreatmentId(), "INVALID_CLINICIAN");
        });
    }

    // ===== BILLING TESTS =====
    @Test
    @DisplayName("Should generate bill for treatment")
    public void testGenerateBill() throws Exception {
        Patient patient = controller.getAllPatients().get(0);
        patient.upgrade();
        TreatmentType treatmentType = controller.getAllTreatmentTypes().get(0);
        controller.bookTreatment(patient.getId(), treatmentType.getId());
        
        Treatment treatment = controller.getPatientTreatments(patient.getId()).get(0);
        Bill bill = controller.generateBill(treatment.getTreatmentId());
        
        assertNotNull(bill);
        assertEquals(treatmentType.getPrice(), bill.getTotalAmount());
        assertFalse(bill.isPaid());
    }

    @Test
    @DisplayName("Should calculate bill total correctly")
    public void testCalculateBillTotal() throws InvalidInputException {
        java.util.List<TreatmentType> treatments = java.util.List.of(
            new TreatmentType("T1", "Treatment 1", 100.0),
            new TreatmentType("T2", "Treatment 2", 200.0),
            new TreatmentType("T3", "Treatment 3", 300.0)
        );
        
        double total = Bill.calculateTotal(treatments);
        assertEquals(600.0, total);
    }

    @Test
    @DisplayName("Should record payment and mark bill as paid")
    public void testRecordPayment() throws Exception {
        Patient patient = controller.getAllPatients().get(0);
        patient.upgrade();
        TreatmentType treatmentType = controller.getAllTreatmentTypes().get(0);
        controller.bookTreatment(patient.getId(), treatmentType.getId());
        
        Treatment treatment = controller.getPatientTreatments(patient.getId()).get(0);
        Bill bill = controller.generateBill(treatment.getTreatmentId());
        
        assertFalse(bill.isPaid());
        controller.recordPayment(bill.getBillId());
        assertTrue(bill.isPaid());
    }

    // ===== NOTIFICATION TESTS =====
    @Test
    @DisplayName("Should send notification to patient")
    public void testSendNotification() throws UserNotFoundException {
        Patient patient = controller.getAllPatients().get(0);
        
        int initialSize = controller.getPatientNotifications(patient.getId()).size();
        controller.sendNotification(patient.getId(), "Test notification", false);
        assertEquals(initialSize + 1, controller.getPatientNotifications(patient.getId()).size());
    }

    @Test
    @DisplayName("Should throw exception when sending notification to non-existent patient")
    public void testSendNotificationToNonExistentPatient() {
        assertThrows(UserNotFoundException.class, () -> {
            controller.sendNotification("INVALID_PATIENT", "Test", false);
        });
    }

    // ===== AUTHENTICATION TESTS =====
    @Test
    @DisplayName("Should successfully login with valid credentials")
    public void testValidLogin() throws UserNotFoundException {
        User user = controller.login("admin@mms.com", "admin123");
        assertNotNull(user);
        assertEquals("admin@mms.com", user.getEmail());
    }

    @Test
    @DisplayName("Should throw exception with invalid credentials")
    public void testInvalidLogin() {
        assertThrows(UserNotFoundException.class, () -> {
            controller.login("invalid@email.com", "wrongpassword");
        });
    }

    // ===== USER FACTORY TESTS =====
    @Test
    @DisplayName("Should create Admin user via Factory")
    public void testCreateAdminViaFactory() throws UserNotFoundException {
        Admin admin = UserFactory.createAdmin("ADM002", "Dr. Admin2", "555-1000", 
                                             "admin2@mms.com", "admin456", "Operations");
        
        assertNotNull(admin);
        assertEquals("Dr. Admin2", admin.getName());
        assertEquals(User.UserRole.ADMIN, admin.getRole());
    }

    @Test
    @DisplayName("Should create Clinician user via Factory")
    public void testCreateClinicianViaFactory() throws UserNotFoundException {
        Clinician clinician = UserFactory.createClinician("CLI002", "Dr. Jones", "555-2000",
                                                         "jones@mms.com", "clinic456", "Neurology", 8);
        
        assertNotNull(clinician);
        assertEquals("Dr. Jones", clinician.getName());
        assertEquals(User.UserRole.CLINICIAN, clinician.getRole());
        assertEquals("Neurology", clinician.getSpecialization());
    }

    @Test
    @DisplayName("Should create Patient user via Factory")
    public void testCreatePatientViaFactory() throws UserNotFoundException {
        Patient patient = UserFactory.createPatient("PAT002", "Alice Brown", "555-3000",
                                                   "alice@email.com", "alice123");
        
        assertNotNull(patient);
        assertEquals("Alice Brown", patient.getName());
        assertEquals(User.UserRole.PATIENT, patient.getRole());
    }

    // ===== POLYMORPHISM TESTS =====
    @Test
    @DisplayName("Should demonstrate polymorphism with showMenu()")
    public void testPolymorphismShowMenu() throws UserNotFoundException {
        Admin admin = UserFactory.createAdmin("ADM003", "Dr. Test", "555-0000", 
                                             "test@mms.com", "test123", "Test");
        Clinician clinician = UserFactory.createClinician("CLI003", "Dr. Test2", "555-0001",
                                                         "test2@mms.com", "test456", "Test", 5);
        Patient patient = UserFactory.createPatient("PAT003", "Test Patient", "555-0002",
                                                   "patient@test.com", "pass");
        
        // Just verify they don't throw exceptions and are callable
        assertDoesNotThrow(admin::showMenu);
        assertDoesNotThrow(clinician::showMenu);
        assertDoesNotThrow(patient::showMenu);
    }

    // ===== PATIENT PROPERTIES TESTS =====
    @Test
    @DisplayName("Should toggle promotions for patient")
    public void testTogglePromotions() {
        Patient patient = controller.getAllPatients().get(0);
        boolean initialState = patient.isOptedInForPromotions();
        
        patient.togglePromotions();
        assertEquals(!initialState, patient.isOptedInForPromotions());
        
        patient.togglePromotions();
        assertEquals(initialState, patient.isOptedInForPromotions());
    }

    @Test
    @DisplayName("Should update treatment status")
    public void testUpdateTreatmentStatus() throws Exception {
        // Register a fresh patient for this test
        controller.registerPatient("Status Test Patient", "555-8888", "status.test789@email.com", "test123");
        final Patient testPatient = controller.getAllPatients().stream()
                .filter(p -> p.getEmail().equals("status.test789@email.com"))
                .findFirst()
                .orElseThrow();
        
        testPatient.upgrade();
        
        TreatmentType treatmentType = controller.getAllTreatmentTypes().get(0);
        controller.bookTreatment(testPatient.getId(), treatmentType.getId());
        
        Treatment treatment = controller.getPatientTreatments(testPatient.getId()).get(0);
        assertEquals(TreatmentStatus.NEW_TREATMENT, treatment.getStatus());
        
        controller.updateTreatmentStatus(treatment.getTreatmentId(), TreatmentStatus.TREATMENT_ASSESSED);
        assertEquals(TreatmentStatus.TREATMENT_ASSESSED, treatment.getStatus());
    }
}
