package com.mms.controllers;

import com.mms.models.*;
import com.mms.exceptions.UserNotFoundException;

/**
 * Factory Pattern implementation for creating User objects.
 * Provides centralized user instantiation logic.
 */
public class UserFactory {

    /**
     * Creates a user object based on role and parameters.
     * 
     * @param role The user role (ADMIN, CLINICIAN, PATIENT)
     * @param id User ID
     * @param name User name
     * @param phone User phone
     * @param email User email
     * @param password User password
     * @param additionalParam1 Additional parameter (department for Admin, specialization for Clinician)
     * @param additionalParam2 Additional parameter (maxPatients for Clinician)
     * @return A User object of the appropriate subclass
     * @throws UserNotFoundException if role is invalid
     */
    public static User createUser(User.UserRole role, String id, String name, String phone, 
                                  String email, String password, String additionalParam1, 
                                  String additionalParam2) throws UserNotFoundException {
        switch (role) {
            case ADMIN:
                return new Admin(id, name, phone, email, password, additionalParam1);
            case CLINICIAN:
                int maxPatients = 5; // default
                try {
                    maxPatients = Integer.parseInt(additionalParam2);
                } catch (NumberFormatException e) {
                    // use default
                }
                return new Clinician(id, name, phone, email, password, additionalParam1, maxPatients);
            case PATIENT:
                return new Patient(id, name, phone, email, password);
            default:
                throw new UserNotFoundException("Invalid user role: " + role);
        }
    }

    /**
     * Simplified factory method for creating users with minimal parameters.
     */
    public static User createUser(User.UserRole role, String id, String name, String email, 
                                  String password) throws UserNotFoundException {
        return createUser(role, id, name, "", email, password, "", "");
    }

    /**
     * Creates an Admin user.
     */
    public static Admin createAdmin(String id, String name, String phone, String email, 
                                    String password, String department) {
        return new Admin(id, name, phone, email, password, department);
    }

    /**
     * Creates a Clinician user.
     */
    public static Clinician createClinician(String id, String name, String phone, String email, 
                                           String password, String specialization, int maxPatients) {
        return new Clinician(id, name, phone, email, password, specialization, maxPatients);
    }

    /**
     * Creates a Patient user.
     */
    public static Patient createPatient(String id, String name, String phone, String email, String password) {
        return new Patient(id, name, phone, email, password);
    }
}
