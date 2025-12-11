package com.mms.controllers;

import com.mms.models.*;
import com.mms.exceptions.*;
import com.mms.storage.StorageManager;
import java.util.*;

/**
 * MMSController manages core business logic for the MMS system.
 * Singleton pattern for system-wide access.
 */
public class MMSController {
    private static MMSController instance;
    private StorageManager storageManager;
    
    private List<Patient> patients;
    private List<Clinician> clinicians;
    private List<Admin> admins;
    private List<Treatment> treatments;
    private List<TreatmentType> treatmentTypes;
    private List<Bill> bills;
    private List<Notification> notifications;

    private MMSController() throws StorageException {
        this.storageManager = StorageManager.getInstance();
        this.patients = new ArrayList<>();
        this.clinicians = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.treatments = new ArrayList<>();
        this.treatmentTypes = new ArrayList<>();
        this.bills = new ArrayList<>();
        this.notifications = new ArrayList<>();
        loadDataFromStorage();
        if (patients.isEmpty() || admins.isEmpty()) {
            initializeSampleData();
            saveAllData();
        }
    }

    public static synchronized MMSController getInstance() throws StorageException {
        if (instance == null) {
            instance = new MMSController();
        }
        return instance;
    }

    private void loadDataFromStorage() throws StorageException {
        this.patients = storageManager.loadPatients();
        this.clinicians = storageManager.loadClinicians();
        this.admins = storageManager.loadAdmins();
        this.treatments = storageManager.loadTreatments();
        this.treatmentTypes = storageManager.loadTreatmentTypes();
        this.bills = storageManager.loadBills();
        this.notifications = storageManager.loadNotifications();
    }

    private void saveAllData() throws StorageException {
        storageManager.savePatients(patients);
        storageManager.saveClinicians(clinicians);
        storageManager.saveAdmins(admins);
        storageManager.saveTreatments(treatments);
        storageManager.saveTreatmentTypes(treatmentTypes);
        storageManager.saveBills(bills);
        storageManager.saveNotifications(notifications);
    }

    private void initializeSampleData() {
        // Initialize with sample data for testing
        admins.add(new Admin("ADM001", "Dr. Admin", "555-0001", "admin@mms.com", "admin123", "Management"));
        clinicians.add(new Clinician("CLI001", "Dr. Smith", "555-0010", "smith@mms.com", "clinic123", "Cardiology", 10));
        patients.add(new Patient("PAT001", "John Doe", "555-0100", "john@email.com", "john123"));
        
        treatmentTypes.add(new TreatmentType("TRT001", "Consultation", 100.0));
        treatmentTypes.add(new TreatmentType("TRT002", "Surgery", 5000.0));
        treatmentTypes.add(new TreatmentType("TRT003", "Therapy", 200.0));
    }

    // ===== PATIENT MANAGEMENT =====
    public void registerPatient(String name, String phone, String email, String password) 
            throws InvalidInputException, StorageException {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            throw new InvalidInputException("Name and email cannot be empty");
        }
        Patient patient = new Patient("PAT" + System.currentTimeMillis(), name, phone, email, password);
        patients.add(patient);
        storageManager.savePatients(patients);
    }

    public Patient registerPatientAndReturn(String name, String phone, String email, String password) 
            throws InvalidInputException, StorageException {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            throw new InvalidInputException("Name and email cannot be empty");
        }
        Patient patient = new Patient("PAT" + System.currentTimeMillis(), name, phone, email, password);
        patients.add(patient);
        storageManager.savePatients(patients);
        return patient;
    }

    public Patient getPatient(String patientId) throws UserNotFoundException {
        return patients.stream()
                .filter(p -> p.getId().equals(patientId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Patient not found: " + patientId));
    }

    public void upgradePatient(String patientId) throws UserNotFoundException, StorageException {
        Patient patient = getPatient(patientId);
        patient.upgrade();
        storageManager.savePatients(patients);
    }

    public void flagPatient(String patientId) throws UserNotFoundException, StorageException {
        Patient patient = getPatient(patientId);
        patient.markFlagged();
        storageManager.savePatients(patients);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    // ===== TREATMENT MANAGEMENT =====
    public void bookTreatment(String patientId, String treatmentTypeId) 
            throws UserNotFoundException, TreatmentNotFoundException, InvalidInputException, StorageException {
        Patient patient = getPatient(patientId);
        if (!patient.isRegistered()) {
            throw new InvalidInputException("Patient must be registered to book treatment");
        }
        
        // Verify treatment type exists
        treatmentTypes.stream()
                .filter(t -> t.getId().equals(treatmentTypeId))
                .findFirst()
                .orElseThrow(() -> new TreatmentNotFoundException("Treatment type not found"));
        
        Treatment treatment = new Treatment("TRE" + System.currentTimeMillis(), patientId, treatmentTypeId);
        treatments.add(treatment);
        storageManager.saveTreatments(treatments);
    }

    public void assignClinician(String treatmentId, String clinicianId) 
            throws TreatmentNotFoundException, UserNotFoundException, StorageException {
        Treatment treatment = treatments.stream()
                .filter(t -> t.getTreatmentId().equals(treatmentId))
                .findFirst()
                .orElseThrow(() -> new TreatmentNotFoundException("Treatment not found"));
        
        // Verify clinician exists
        clinicians.stream()
                .filter(c -> c.getId().equals(clinicianId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Clinician not found"));
        
        treatment.setClinicianId(clinicianId);
        storageManager.saveTreatments(treatments);
    }

    public void updateTreatmentStatus(String treatmentId, TreatmentStatus status) 
            throws TreatmentNotFoundException, StorageException {
        Treatment treatment = treatments.stream()
                .filter(t -> t.getTreatmentId().equals(treatmentId))
                .findFirst()
                .orElseThrow(() -> new TreatmentNotFoundException("Treatment not found"));
        
        treatment.setStatus(status);
        storageManager.saveTreatments(treatments);
    }

    public Treatment getTreatment(String treatmentId) throws TreatmentNotFoundException {
        return treatments.stream()
                .filter(t -> t.getTreatmentId().equals(treatmentId))
                .findFirst()
                .orElseThrow(() -> new TreatmentNotFoundException("Treatment not found"));
    }

    public List<Treatment> getPatientTreatments(String patientId) {
        return treatments.stream()
                .filter(t -> t.getPatientId().equals(patientId))
                .toList();
    }

    public List<Treatment> getClinicianTreatments(String clinicianId) {
        return treatments.stream()
                .filter(t -> t.getClinicianId() != null && t.getClinicianId().equals(clinicianId))
                .toList();
    }

    // ===== TREATMENT TYPE MANAGEMENT =====
    public void addTreatmentType(String name, double price) throws InvalidInputException, StorageException {
        if (name == null || name.isEmpty() || price <= 0) {
            throw new InvalidInputException("Invalid treatment type data");
        }
        TreatmentType type = new TreatmentType("TRT" + System.currentTimeMillis(), name, price);
        treatmentTypes.add(type);
        storageManager.saveTreatmentTypes(treatmentTypes);
    }

    public void removeTreatmentType(String treatmentTypeId) throws TreatmentNotFoundException, StorageException {
        boolean removed = treatmentTypes.removeIf(t -> t.getId().equals(treatmentTypeId));
        if (!removed) {
            throw new TreatmentNotFoundException("Treatment type not found");
        }
        storageManager.saveTreatmentTypes(treatmentTypes);
    }

    public TreatmentType getTreatmentType(String typeId) throws TreatmentNotFoundException {
        return treatmentTypes.stream()
                .filter(t -> t.getId().equals(typeId))
                .findFirst()
                .orElseThrow(() -> new TreatmentNotFoundException("Treatment type not found"));
    }

    public List<TreatmentType> getAllTreatmentTypes() {
        return new ArrayList<>(treatmentTypes);
    }

    // ===== BILLING MANAGEMENT =====
    public Bill generateBill(String treatmentId) 
            throws TreatmentNotFoundException, StorageException {
        Treatment treatment = getTreatment(treatmentId);
        TreatmentType type = getTreatmentType(treatment.getTreatmentTypeId());
        
        Bill bill = new Bill("BILL" + System.currentTimeMillis(), treatment.getPatientId(), 
                            treatmentId, type.getPrice());
        bills.add(bill);
        treatment.setStatus(TreatmentStatus.BILL_GENERATED);
        storageManager.saveBills(bills);
        storageManager.saveTreatments(treatments);
        
        return bill;
    }

    public void recordPayment(String billId) throws TreatmentNotFoundException, StorageException {
        Bill bill = bills.stream()
                .filter(b -> b.getBillId().equals(billId))
                .findFirst()
                .orElseThrow(() -> new TreatmentNotFoundException("Bill not found"));
        
        bill.markPaid();
        
        // Update treatment status to PAID
        Treatment treatment = getTreatment(bill.getTreatmentId());
        treatment.setStatus(TreatmentStatus.PAID);
        storageManager.saveBills(bills);
        storageManager.saveTreatments(treatments);
    }

    public Bill getBill(String billId) throws TreatmentNotFoundException {
        return bills.stream()
                .filter(b -> b.getBillId().equals(billId))
                .findFirst()
                .orElseThrow(() -> new TreatmentNotFoundException("Bill not found"));
    }

    public List<Bill> getPatientBills(String patientId) {
        return bills.stream()
                .filter(b -> b.getPatientId().equals(patientId))
                .toList();
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    // ===== NOTIFICATION MANAGEMENT =====
    public void sendNotification(String patientId, String message, boolean isPromotional) 
            throws UserNotFoundException, StorageException {
        Patient patient = getPatient(patientId);
        Notification notification = new Notification("NOT" + System.currentTimeMillis(), 
                                                     patientId, message, isPromotional);
        notifications.add(notification);
        patient.receiveNotification(notification);
        storageManager.saveNotifications(notifications);
    }

    public List<Notification> getPatientNotifications(String patientId) {
        return notifications.stream()
                .filter(n -> n.getPatientId().equals(patientId))
                .toList();
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    // ===== USER AUTHENTICATION =====
    public User login(String email, String password) throws UserNotFoundException {
        // Check admins
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        
        // Check clinicians
        for (Clinician clinician : clinicians) {
            if (clinician.getEmail().equals(email) && clinician.getPassword().equals(password)) {
                return clinician;
            }
        }
        
        // Check patients
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email) && patient.getPassword().equals(password)) {
                return patient;
            }
        }
        
        throw new UserNotFoundException("Invalid email or password");
    }

    public List<Clinician> getAllClinicians() {
        return new ArrayList<>(clinicians);
    }

    public Clinician getClinician(String clinicianId) throws UserNotFoundException {
        return clinicians.stream()
                .filter(c -> c.getId().equals(clinicianId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Clinician not found"));
    }
}
