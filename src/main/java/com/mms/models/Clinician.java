package com.mms.models;

/**
 * Clinician user class - has limited access to assigned patients and treatments.
 */
public class Clinician extends User {
    private String specialization;
    private int maxPatients;

    public Clinician(String id, String name, String phone, String email, String password, 
                     String specialization, int maxPatients) {
        super(id, name, phone, email, password, UserRole.CLINICIAN);
        this.specialization = specialization;
        this.maxPatients = maxPatients;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getMaxPatients() {
        return maxPatients;
    }

    public void setMaxPatients(int maxPatients) {
        this.maxPatients = maxPatients;
    }

    @Override
    public void showMenu() {
        System.out.println("\n====== CLINICIAN MENU ======");
        System.out.println("1. View assigned patients");
        System.out.println("2. Record treatment");
        System.out.println("3. Update treatment status");
        System.out.println("4. Logout");
        System.out.println("============================");
        System.out.print("Select option: ");
    }

    @Override
    public String toString() {
        return "Clinician{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
