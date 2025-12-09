package com.mms.models;

/**
 * Admin user class - has full system access.
 */
public class Admin extends User {
    private String department;

    public Admin(String id, String name, String phone, String email, String password, String department) {
        super(id, name, phone, email, password, UserRole.ADMIN);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public void showMenu() {
        System.out.println("\n====== ADMIN MENU ======");
        System.out.println("1. Register new patient");
        System.out.println("2. Upgrade patient to registered");
        System.out.println("3. Assign patient to clinician");
        System.out.println("4. Add treatment type");
        System.out.println("5. Remove treatment type");
        System.out.println("6. Generate bill");
        System.out.println("7. Record payment");
        System.out.println("8. Flag non-paying patient");
        System.out.println("9. Send notifications");
        System.out.println("10. View patients / treatments / reports");
        System.out.println("11. Logout");
        System.out.println("========================");
        System.out.print("Select option: ");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
