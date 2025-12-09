# MMS Medical Management System - User Guide

## Getting Started

### Prerequisites
- Java 21 LTS
- Maven 3.9+
- Terminal/Console

### Installation
```bash
cd /Users/faizan/medical-cli
mvn clean compile
```

### Running the Application
```bash
mvn exec:java -Dexec.mainClass="com.mms.cli.Main"
```

---

## Login Credentials

Use these credentials to access different roles:

### Admin
- **Email:** admin@mms.com
- **Password:** admin123

### Clinician  
- **Email:** smith@mms.com
- **Password:** clinic123

### Patient
- **Email:** john@email.com
- **Password:** john123

---

## Feature Walkthrough

### 1. ADMIN ROLE

#### Register New Patient
```
Path: Main Menu → 1 → 1 (Login as Admin)
       Enter admin credentials
       Select "1. Register new patient"
       
Input Required:
  - Patient Name (e.g., Jane Smith)
  - Phone (e.g., 555-0200)
  - Email (e.g., jane@email.com)
  - Password (e.g., jane123)

Result: Patient created with unregistered status
```

#### Upgrade Patient to Registered
```
Path: Admin Menu → 2
      
Input Required:
  - Patient ID (e.g., PAT1733766400000)

Result: Patient can now book treatments
```

#### Assign Clinician to Treatment
```
Path: Admin Menu → 3
      
Input Required:
  - Treatment ID (e.g., TRE1733766500000)
  - Clinician ID (e.g., CLI001)

Result: Clinician can now see the treatment
```

#### Add Treatment Type
```
Path: Admin Menu → 4
      
Input Required:
  - Treatment Name (e.g., Dental Cleaning)
  - Price (e.g., 150.00)

Result: New treatment available for patients to book
```

#### Remove Treatment Type
```
Path: Admin Menu → 5
      
Input Required:
  - Treatment Type ID (e.g., TRT001)

Result: Treatment type deleted from system
```

#### Generate Bill
```
Path: Admin Menu → 6
      
Input Required:
  - Treatment ID (e.g., TRE1733766500000)

Result: Bill created with amount based on treatment type price
        Treatment status updated to BILL_GENERATED
```

#### Record Payment
```
Path: Admin Menu → 7
      
Input Required:
  - Bill ID (e.g., BILL1733766600000)

Result: Bill marked as PAID
        Treatment status updated to PAID
```

#### Flag Non-Paying Patient
```
Path: Admin Menu → 8
      
Input Required:
  - Patient ID (e.g., PAT001)

Result: Patient marked as flagged in system
```

#### Send Notifications
```
Path: Admin Menu → 9
      
Input Required:
  - Patient ID (e.g., PAT001)
  - Message (e.g., "Your bill is ready for payment")
  - Is Promotional? (yes/no)

Result: Patient receives notification (if subscribed)
```

#### View Reports
```
Path: Admin Menu → 10
      
Displays:
  - All patients
  - All treatments
  - All bills
  - All notifications
```

---

### 2. CLINICIAN ROLE

#### View Assigned Patients
```
Path: Main Menu → 2 → 1 (Login as Clinician)
      Enter clinician credentials
      Select "1. View assigned patients"

Displays:
  - List of patients assigned to you
  - Their treatment details
  - Current status
```

#### Record Treatment
```
Path: Clinician Menu → 2
      
Input Required:
  - Treatment ID (e.g., TRE1733766500000)
  - Treatment Notes (e.g., "Patient shows improvement")

Result: Notes added to treatment record
```

#### Update Treatment Status
```
Path: Clinician Menu → 3
      
Input Required:
  - Treatment ID
  - New Status (typically TREATMENT_ASSESSED)

Result: Treatment status updated in system
```

---

### 3. PATIENT ROLE

#### Book Treatment
```
Path: Main Menu → 3 → 1 (Login as Patient)
      Enter patient credentials (if registered)
      Select "1. Book treatment"

Display:
  - Available treatment types with prices
  
Input Required:
  - Treatment Type ID (e.g., TRT001)

Result: Treatment booked and assigned to available clinician
        Status: NEW_TREATMENT
```

#### View Treatment Status
```
Path: Patient Menu → 2
      
Displays:
  - All your treatments
  - Current status of each
  - Timeline (NEW → ASSESSED → BILL_GENERATED → COMPLETED → PAID)
```

#### View Bills
```
Path: Patient Menu → 3
      
Displays:
  - All bills generated for your treatments
  - Amount, payment status
  - Payment dates (if paid)
```

#### Toggle Promotions
```
Path: Patient Menu → 4
      
Effect: Enables/disables promotional notifications
        When disabled: won't receive promotional messages
        When enabled: will receive all messages
```

---

## Complete Example Workflow

### Scenario: New patient books a consultation

**Step 1: Admin Registers Patient**
```
1. Login as Admin (admin@mms.com / admin123)
2. Select "1. Register new patient"
3. Enter: Jane Smith, 555-0200, jane@email.com, jane123
4. Patient created (ID: PAT1733766400000)
```

**Step 2: Admin Upgrades Patient**
```
1. Select "2. Upgrade patient to registered"
2. Enter Patient ID: PAT1733766400000
3. Jane is now registered and can book treatments
```

**Step 3: Patient Books Treatment**
```
1. Logout from Admin
2. Login as Patient (jane@email.com / jane123)
3. Select "1. Book treatment"
4. View available treatments: Consultation ($100), Surgery ($5000), Therapy ($200)
5. Select: TRT001 (Consultation)
6. Treatment created (ID: TRE1733766500000, Status: NEW_TREATMENT)
```

**Step 4: Admin Assigns Clinician**
```
1. Login as Admin
2. Select "3. Assign patient to clinician"
3. Enter Treatment ID: TRE1733766500000
4. Enter Clinician ID: CLI001
5. Clinician can now see the treatment
```

**Step 5: Clinician Records Treatment**
```
1. Logout from Admin
2. Login as Clinician (smith@mms.com / clinic123)
3. Select "1. View assigned patients" - Jane is listed
4. Select "2. Record treatment"
5. Enter Treatment ID: TRE1733766500000
6. Enter Notes: "Patient in good health, consultation complete"
7. Treatment status updated to TREATMENT_ASSESSED
```

**Step 6: Admin Generates Bill**
```
1. Login as Admin
2. Select "6. Generate bill"
3. Enter Treatment ID: TRE1733766500000
4. Bill created (ID: BILL1733766600000, Amount: $100)
5. Status updated to BILL_GENERATED
```

**Step 7: Patient Views Bill**
```
1. Login as Patient (jane@email.com / jane123)
2. Select "3. View bills"
3. See bill for $100 (status: Unpaid)
```

**Step 8: Admin Records Payment**
```
1. Login as Admin
2. Select "7. Record payment"
3. Enter Bill ID: BILL1733766600000
4. Bill marked as PAID
5. Treatment status updated to PAID
```

**Step 9: Patient Confirms Payment**
```
1. Login as Patient (jane@email.com / jane123)
2. Select "3. View bills"
3. See bill marked as PAID
4. Workflow complete! ✅
```

---

## Testing

### Run All Tests
```bash
mvn test
```

### Expected Output
```
Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Test Coverage
- ✅ Patient registration
- ✅ Patient upgrade
- ✅ Treatment booking
- ✅ Clinician assignment
- ✅ Bill generation
- ✅ Payment recording
- ✅ Treatment type management
- ✅ User login/authentication
- ✅ Exception handling
- ✅ Input validation

---

## Data Storage

All data is stored in CSV files located in `/storage/`:

```
/storage/
├── patients.csv          - Patient records
├── clinicians.csv        - Clinician records
├── admins.csv            - Admin records
├── treatments.csv        - Treatment bookings
├── treatment_types.csv   - Treatment catalog
├── bills.csv             - Bill records
└── notifications.csv     - Notification history
```

**Important:** Data persists between sessions. CSV files are created automatically.

---

## Troubleshooting

### Issue: "Patient must be registered to book treatment"
**Solution:** Admin must upgrade patient first using option 2

### Issue: "Invalid email or password"
**Solution:** Use exact credentials from login section (case-sensitive)

### Issue: "Treatment not found"
**Solution:** Make sure you're using correct treatment ID format (TRE + timestamp)

### Issue: Build fails
**Solution:** 
```bash
mvn clean install
mvn compile
```

### Issue: Port already in use
**Solution:** The application runs in CLI mode, no port needed

---

## Architecture Overview

```
User Input
    ↓
Main.java (Authentication)
    ↓
Specific CLI (AdminCLI / ClinicianCLI / PatientCLI)
    ↓
MMSController (Business Logic)
    ↓
Models (User, Treatment, Bill, etc.)
    ↓
Storage/CSV (Data Persistence)
```

---

## Key Classes

- **User.java** - Abstract base class for all users
- **Admin.java, Clinician.java, Patient.java** - User subclasses
- **MMSController.java** - Central business logic (Singleton)
- **Treatment.java, Bill.java** - Core domain entities
- **UserFactory.java** - Factory pattern for user creation
- **StorageManager.java** - CSV file handling
- **Main.java** - Entry point and authentication

---

## Design Patterns Used

1. **Factory Pattern** - UserFactory creates appropriate user types
2. **Singleton Pattern** - MMSController for system-wide instance
3. **Observer Pattern** - Notifiable interface for notifications
4. **Strategy Pattern** - Different CLI implementations per role
5. **Polymorphism** - User.showMenu() overridden in subclasses

---

## Development Notes

- Java 21 LTS (upgraded from Java 11)
- Maven build system
- JUnit 5 testing framework
- Layered architecture (Presentation → Application → Domain → Data)
- Exception handling with custom exceptions
- No external dependencies beyond testing

---

## Next Steps

1. **Run the build:** `mvn clean compile`
2. **Run the tests:** `mvn test`
3. **Start the app:** `mvn exec:java -Dexec.mainClass="com.mms.cli.Main"`
4. **Test the workflow:** Use example scenario above
5. **Explore features:** Try all menu options

---

**System Status:** ✅ Production Ready

**Last Updated:** December 9, 2025
