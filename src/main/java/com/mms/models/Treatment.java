package com.mms.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Treatment entity representing a patient's treatment booking and progress.
 */
public class Treatment {
    private String treatmentId;
    private String patientId;
    private String clinicianId;
    private String treatmentTypeId;
    private TreatmentStatus status;
    private LocalDateTime createdDate;
    private String notes;

    public Treatment(String treatmentId, String patientId, String treatmentTypeId) {
        this.treatmentId = treatmentId;
        this.patientId = patientId;
        this.treatmentTypeId = treatmentTypeId;
        this.status = TreatmentStatus.NEW_TREATMENT;
        this.createdDate = LocalDateTime.now();
        this.notes = "";
        this.clinicianId = null;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public void setClinicianId(String clinicianId) {
        this.clinicianId = clinicianId;
    }

    public String getTreatmentTypeId() {
        return treatmentTypeId;
    }

    public void setTreatmentTypeId(String treatmentTypeId) {
        this.treatmentTypeId = treatmentTypeId;
    }

    public TreatmentStatus getStatus() {
        return status;
    }

    public void setStatus(TreatmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Treatment{" +
                "id='" + treatmentId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", clinicianId='" + clinicianId + '\'' +
                ", typeId='" + treatmentTypeId + '\'' +
                ", status=" + status.getDisplayName() +
                ", created=" + createdDate.format(formatter) +
                '}';
    }
}
