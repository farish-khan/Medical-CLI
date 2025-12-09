package com.mms.models;

/**
 * Enum representing the status of a treatment.
 */
public enum TreatmentStatus {
    NEW_TREATMENT("New"),
    TREATMENT_ASSESSED("Assessed"),
    BILL_GENERATED("Bill Generated"),
    COMPLETED("Completed"),
    PAID("Paid");

    private final String displayName;

    TreatmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
