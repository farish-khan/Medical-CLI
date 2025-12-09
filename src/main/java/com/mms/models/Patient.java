package com.mms.models;

/**
 * Patient user class - can book treatments, view bills, manage notifications.
 * Implements Notifiable interface for notification system.
 */
public class Patient extends User implements Notifiable {
    private boolean isRegistered;
    private boolean isFlagged;
    private boolean optedInForPromotions;

    public Patient(String id, String name, String phone, String email, String password) {
        super(id, name, phone, email, password, UserRole.PATIENT);
        this.isRegistered = false;
        this.isFlagged = false;
        this.optedInForPromotions = true;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isOptedInForPromotions() {
        return optedInForPromotions;
    }

    public void setOptedInForPromotions(boolean optedInForPromotions) {
        this.optedInForPromotions = optedInForPromotions;
    }

    public void upgrade() {
        this.isRegistered = true;
    }

    public void togglePromotions() {
        this.optedInForPromotions = !this.optedInForPromotions;
    }

    public void markFlagged() {
        this.isFlagged = true;
    }

    @Override
    public void receiveNotification(Notification notification) {
        if (this.optedInForPromotions || !notification.isPromotional()) {
            System.out.println("\n[NOTIFICATION for " + this.getName() + "] " + notification.getMessage());
        }
    }

    @Override
    public void showMenu() {
        System.out.println("\n====== PATIENT MENU ======");
        System.out.println("1. Book treatment");
        System.out.println("2. View treatment status");
        System.out.println("3. View bills");
        System.out.println("4. Enable/disable promotions");
        System.out.println("5. Logout");
        System.out.println("=========================");
        System.out.print("Select option: ");
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", isRegistered=" + isRegistered +
                ", isFlagged=" + isFlagged +
                '}';
    }
}
