package com.mms.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Bill entity representing a bill for a treatment.
 */
public class Bill {
    private String billId;
    private String patientId;
    private String treatmentId;
    private double totalAmount;
    private boolean isPaid;
    private LocalDateTime createdDate;
    private LocalDateTime paidDate;

    public Bill(String billId, String patientId, String treatmentId, double totalAmount) {
        this.billId = billId;
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.totalAmount = totalAmount;
        this.isPaid = false;
        this.createdDate = LocalDateTime.now();
        this.paidDate = null;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
        if (paid) {
            this.paidDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public void markPaid() {
        this.isPaid = true;
        this.paidDate = LocalDateTime.now();
    }

    public static double calculateTotal(java.util.List<TreatmentType> treatmentTypes) {
        return treatmentTypes.stream().mapToDouble(TreatmentType::getPrice).sum();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Bill{" +
                "id='" + billId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", treatmentId='" + treatmentId + '\'' +
                ", amount=" + totalAmount +
                ", isPaid=" + isPaid +
                ", created=" + createdDate.format(formatter) +
                '}';
    }
}
