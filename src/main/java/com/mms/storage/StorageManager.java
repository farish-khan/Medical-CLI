package com.mms.storage;

import com.mms.models.*;
import com.mms.exceptions.StorageException;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * StorageManager handles all CSV file operations for persistent data storage.
 * Singleton pattern ensures single instance manages all storage operations.
 */
public class StorageManager {
    private static StorageManager instance;
    private static final String STORAGE_DIR = "storage";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private File storageDirectory;

    private StorageManager() throws StorageException {
        this.storageDirectory = new File(STORAGE_DIR);
        if (!storageDirectory.exists()) {
            if (!storageDirectory.mkdirs()) {
                throw new StorageException("Failed to create storage directory");
            }
        }
    }

    public static synchronized StorageManager getInstance() throws StorageException {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    private File getFile(String filename) {
        return new File(storageDirectory, filename);
    }

    // ===== PATIENTS =====
    public void savePatients(List<Patient> patients) throws StorageException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(getFile("patients.csv")))) {
            writer.println("id,name,phone,email,isRegistered,isFlagged,optedInForPromotions");
            for (Patient patient : patients) {
                writer.printf("%s,%s,%s,%s,%b,%b,%b%n",
                        patient.getId(),
                        patient.getName(),
                        patient.getPhone(),
                        patient.getEmail(),
                        patient.isRegistered(),
                        patient.isFlagged(),
                        patient.isOptedInForPromotions());
            }
        } catch (IOException e) {
            throw new StorageException("Failed to save patients: " + e.getMessage(), e);
        }
    }

    public List<Patient> loadPatients() throws StorageException {
        List<Patient> patients = new ArrayList<>();
        File file = getFile("patients.csv");
        
        if (!file.exists()) {
            return patients;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    throw new StorageException("Invalid patient data in CSV");
                }
                
                Patient patient = new Patient(parts[0], parts[1], parts[2], parts[3], "");
                if (parts.length > 4) {
                    patient.setRegistered(Boolean.parseBoolean(parts[4]));
                }
                if (parts.length > 5) {
                    patient.setFlagged(Boolean.parseBoolean(parts[5]));
                }
                if (parts.length > 6) {
                    patient.setOptedInForPromotions(Boolean.parseBoolean(parts[6]));
                }
                patients.add(patient);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to load patients: " + e.getMessage(), e);
        }
        return patients;
    }

    // ===== TREATMENTS =====
    public void saveTreatments(List<Treatment> treatments) throws StorageException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(getFile("treatments.csv")))) {
            writer.println("treatmentId,patientId,clinicianId,treatmentTypeId,status,createdDate,notes");
            for (Treatment treatment : treatments) {
                String clinicianId = treatment.getClinicianId() != null ? treatment.getClinicianId() : "";
                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        treatment.getTreatmentId(),
                        treatment.getPatientId(),
                        clinicianId,
                        treatment.getTreatmentTypeId(),
                        treatment.getStatus().name(),
                        treatment.getCreatedDate().format(DATE_FORMATTER),
                        treatment.getNotes());
            }
        } catch (IOException e) {
            throw new StorageException("Failed to save treatments: " + e.getMessage(), e);
        }
    }

    public List<Treatment> loadTreatments() throws StorageException {
        List<Treatment> treatments = new ArrayList<>();
        File file = getFile("treatments.csv");
        
        if (!file.exists()) {
            return treatments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    throw new StorageException("Invalid treatment data in CSV");
                }
                
                Treatment treatment = new Treatment(parts[0], parts[1], parts[3]);
                if (!parts[2].isEmpty()) {
                    treatment.setClinicianId(parts[2]);
                }
                treatment.setStatus(TreatmentStatus.valueOf(parts[4]));
                if (parts.length > 6) {
                    treatment.setNotes(parts[6]);
                }
                treatments.add(treatment);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to load treatments: " + e.getMessage(), e);
        }
        return treatments;
    }

    // ===== TREATMENT TYPES =====
    public void saveTreatmentTypes(List<TreatmentType> types) throws StorageException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(getFile("treatment_types.csv")))) {
            writer.println("id,name,price");
            for (TreatmentType type : types) {
                writer.printf("%s,%s,%.2f%n", type.getId(), type.getName(), type.getPrice());
            }
        } catch (IOException e) {
            throw new StorageException("Failed to save treatment types: " + e.getMessage(), e);
        }
    }

    public List<TreatmentType> loadTreatmentTypes() throws StorageException {
        List<TreatmentType> types = new ArrayList<>();
        File file = getFile("treatment_types.csv");
        
        if (!file.exists()) {
            return types;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length < 3) {
                    throw new StorageException("Invalid treatment type data in CSV");
                }
                
                TreatmentType type = new TreatmentType(parts[0], parts[1], Double.parseDouble(parts[2]));
                types.add(type);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to load treatment types: " + e.getMessage(), e);
        }
        return types;
    }

    // ===== BILLS =====
    public void saveBills(List<Bill> bills) throws StorageException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(getFile("bills.csv")))) {
            writer.println("billId,patientId,treatmentId,totalAmount,isPaid,createdDate,paidDate");
            for (Bill bill : bills) {
                String paidDate = bill.getPaidDate() != null ? bill.getPaidDate().format(DATE_FORMATTER) : "";
                writer.printf("%s,%s,%s,%.2f,%b,%s,%s%n",
                        bill.getBillId(),
                        bill.getPatientId(),
                        bill.getTreatmentId(),
                        bill.getTotalAmount(),
                        bill.isPaid(),
                        bill.getCreatedDate().format(DATE_FORMATTER),
                        paidDate);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to save bills: " + e.getMessage(), e);
        }
    }

    public List<Bill> loadBills() throws StorageException {
        List<Bill> bills = new ArrayList<>();
        File file = getFile("bills.csv");
        
        if (!file.exists()) {
            return bills;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    throw new StorageException("Invalid bill data in CSV");
                }
                
                Bill bill = new Bill(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                if (parts.length > 4) {
                    bill.setPaid(Boolean.parseBoolean(parts[4]));
                }
                bills.add(bill);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to load bills: " + e.getMessage(), e);
        }
        return bills;
    }

    // ===== NOTIFICATIONS =====
    public void saveNotifications(List<Notification> notifications) throws StorageException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(getFile("notifications.csv")))) {
            writer.println("notificationId,patientId,message,timestamp,isPromotional");
            for (Notification notification : notifications) {
                writer.printf("%s,%s,%s,%s,%b%n",
                        notification.getNotificationId(),
                        notification.getPatientId(),
                        notification.getMessage(),
                        notification.getTimestamp().format(DATE_FORMATTER),
                        notification.isPromotional());
            }
        } catch (IOException e) {
            throw new StorageException("Failed to save notifications: " + e.getMessage(), e);
        }
    }

    public List<Notification> loadNotifications() throws StorageException {
        List<Notification> notifications = new ArrayList<>();
        File file = getFile("notifications.csv");
        
        if (!file.exists()) {
            return notifications;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",", 5);
                if (parts.length < 4) {
                    throw new StorageException("Invalid notification data in CSV");
                }
                
                Notification notification = new Notification(parts[0], parts[1], parts[2], 
                                                             Boolean.parseBoolean(parts[4]));
                notifications.add(notification);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to load notifications: " + e.getMessage(), e);
        }
        return notifications;
    }
}
