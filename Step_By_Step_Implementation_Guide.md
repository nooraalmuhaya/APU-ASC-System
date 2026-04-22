#  APU-ASC ASSIGNMENT - COMPLETE IMPLEMENTATION GUIDE
## Step-by-Step Technical Setup & Workflow

**Document Version:** 1.0  
**Created:** April 21, 2026  
**Deadline:** May 1, 2026  
**Team:** Noora, Namonje, Zulqarnain, Estella

---

##  TABLE OF CONTENTS
1. [Tools & Environment Setup](#tools--environment-setup)
2. [Project Structure & File Organization](#project-structure--file-organization)
3. [Development Workflow & Collaboration](#development-workflow--collaboration)
4. [Step-by-Step Implementation Process](#step-by-step-implementation-process)
5. [File Creation Checklist](#file-creation-checklist)
6. [Integration Points & Data Flow](#integration-points--data-flow)
7. [Testing & Quality Assurance](#testing--quality-assurance)
8. [Submission Preparation](#submission-preparation)

---

# TOOLS & ENVIRONMENT SETUP

## 🔧 REQUIRED TOOLS

### 1. Java Development Kit (JDK)
**Recommended Version:** JDK 11 or JDK 17 (LTS)

**Installation:**
- **Windows:** Download from https://www.oracle.com/java/technologies/javase-downloads.html
- **Mac:** `brew install openjdk`
- **Linux:** `sudo apt-get install openjdk-11-jdk`

**Verification:**
```bash
java -version
javac -version
```

**Expected Output:**
```
java version "11.0.x" or higher
javac version "11.0.x" or higher
```

---

### 2. IDE (Choose One)

#### Option A: IntelliJ IDEA Community Edition (RECOMMENDED)
- **Download:** https://www.jetbrains.com/idea/download/
- **Advantages:** Best Java support, excellent refactoring tools, integrated testing
- **Setup:** Default settings work fine
- **Free:** Community Edition has all features you need

### 3. Version Control 

#### Git + GitHub (For backup & collaboration)
**Installation:**
```bash
# Windows: https://git-scm.com/download/win
# Mac: brew install git
# Linux: sudo apt-get install git

# Verify
git --version
```

**Setup Repository:**
```bash
# Create folder
mkdir APU-ASC-System
cd APU-ASC-System

# Initialize git
git init

# Add remote (create free GitHub account first)
git remote add origin https://github.com/nooraalmuhaya/APU-ASC-System.git
```

**Daily Git Commands:**
```bash
# Before starting work
git pull

# After work session (daily backup)
git add .
git commit -m "Day 5: Backend classes complete"
git push
```

---

### 4. Text Editors (For Documentation)

**For Reports & Documentation:**
- **Microsoft Word**
  https://cloudmails-my.sharepoint.com/:w:/g/personal/tp088968_mail_apu_edu_my/IQCsx-76UlRDToHCF_T4bP6GAYiwhExaRLwPJFMCItvZdp8?e=Q3pAj0


# PROJECT STRUCTURE & FILE ORGANIZATION

##  COMPLETE PROJECT FOLDER STRUCTURE

```
APU-ASC-System/
│
├── src/                                    [All Java source files]
│   └── com/asu/
│       │
│       ├── model/                          [Data model classes]
│       │   ├── User.java                   [Abstract user base class]
│       │   ├── Manager.java                [Manager subclass]
│       │   ├── CounterStaff.java           [Counter staff subclass]
│       │   ├── Technician.java             [Technician subclass]
│       │   ├── Customer.java               [Customer subclass]
│       │   ├── Service.java                [Service class]
│       │   ├── Appointment.java            [Appointment class]
│       │   ├── Payment.java                [Payment class]
│       │   ├── Receipt.java                [Receipt class]
│       │   ├── Feedback.java               [Feedback class]
│       │   └── Report.java                 [Report class]
│       │
│       ├── data/                           [Data persistence layer]
│       │   ├── DataPersistence.java        [Interface for CRUD]
│       │   ├── UserDataManager.java        [User data operations]
│       │   ├── ServiceDataManager.java     [Service data operations]
│       │   ├── AppointmentDataManager.java [Appointment data operations]
│       │   ├── PaymentDataManager.java     [Payment data operations]
│       │   ├── FeedbackDataManager.java    [Feedback data operations]
│       │   ├── FileHandler.java            [File I/O operations]
│       │   ├── DataValidator.java          [Data validation]
│       │   └── BackupManager.java          [Backup operations]
│       │
│       ├── ui/                             [GUI components]
│       │   ├── UIConstants.java            [Colors, fonts, sizes]
│       │   ├── LoginFrame.java             [Login window]
│       │   ├── MainApplicationFrame.java   [Main application window]
│       │   ├── DialogUtils.java            [Dialog utilities]
│       │   │
│       │   └── panels/                     [User role panels]
│       │       ├── ManagerPanel.java       [Manager interface]
│       │       ├── CounterStaffPanel.java  [Counter staff interface]
│       │       ├── TechnicianPanel.java    [Technician interface]
│       │       └── CustomerPanel.java      [Customer interface]
│       │
│       ├── dialogs/                        [Dialog windows]
│       │   ├── RegistrationDialog.java     [User registration]
│       │   ├── AppointmentDialog.java      [Create appointment]
│       │   ├── PaymentDialog.java          [Process payment]
│       │   ├── StaffManagementDialog.java  [Manage staff]
│       │   └── FeedbackDialog.java         [Submit feedback]
│       │
│       ├── util/                           [Utility classes]
│       │   ├── BusinessLogic.java          [Business logic methods]
│       │   └── ValidationUtils.java        [Input validation]
│       │
│       └── main/                           [Entry point]
│           └── APUAutomotiveServiceApp.java [Main class]
│
├── data/                                   [Text data files]
│   ├── users.txt                           [User records]
│   ├── services.txt                        [Service records]
│   ├── appointments.txt                    [Appointment records]
│   ├── payments.txt                        [Payment records]
│   └── feedback.txt                        [Feedback records]
│
├── docs/                                   [Documentation files]
│   ├── Report.pdf                          [Final report]
│   ├── TestResults.md                      [Test results]
│   ├── UMLDiagrams/                        [UML diagrams]
│   │   ├── ClassDiagram.png
│   │   └── UseCaseDiagram.png
│   └── Screenshots/                        [Program screenshots]
│       ├── LoginScreen.png
│       ├── ManagerPanel.png
│       └── ...
│
├── bin/                                    [Compiled .class files]
│   └── com/asu/
│       ├── model/
│       ├── data/
│       ├── ui/
│       └── ...
│
├── README.txt                              [Instructions to run]
├── build.sh                                [Compilation script]
├── run.sh                                  [Execution script]
└── .gitignore                              [Git ignore file]
```

## CODE SHARING & COLLABORATION METHODS

###  GitHub **
```bash
# Setup (One time)
git init
git config user.name "Your Name"
git config user.email "your.email@example.com"

# Daily workflow
git pull origin main                    # Get latest code from team
git add .                               # Add your changes
git commit -m "Day 5: Backend classes"  # Save locally
git push origin main                    # Share with team

# If conflict (two people edited same file)
# Resolve conflicts, then:
git add .
git commit -m "Resolved merge conflict"
git push origin main
```


# STEP-BY-STEP IMPLEMENTATION PROCESS

#### For All Members:
```bash
# 1. Install JDK
# Download from https://www.oracle.com/java/technologies/javase-downloads.html
# Or use command:
#   Windows: choco install openjdk11
#   Mac: brew install openjdk@11
#   Linux: sudo apt-get install openjdk-11-jdk

# 2. Verify installation
java -version
javac -version

# 3. Install IDE (Choose one)
# Option A: IntelliJ IDEA Community Edition

# 4. Create project folder
mkdir APU-ASC-System
cd APU-ASC-System

# 5. Create folder structure
mkdir -p src/com/asu/{model,data,ui,dialogs,util,main}
mkdir -p bin
mkdir -p data
mkdir -p docs/screenshots
```

### **Step 2: Create Base Project in IDE (30 minutes)**

#### **IntelliJ IDEA Steps:**
1. File → New → Project
2. Select "Java" → Next
3. Name: `APU-ASC-System`
4. Location: Your project folder
5. Click "Create"
6. Right-click on `src` → New → Package
7. Name: `com.asu.model`
8. Repeat for: `com.asu.data`, `com.asu.ui`, `com.asu.dialogs`, `com.asu.util`

#### **Eclipse Steps:**
1. File → New → Java Project
2. Project name: `APU-ASC-System`
3. Click "Finish"
4. Right-click on `src` → New → Package
5. Name: `com.asu.model`
6. Repeat for other packages

### **Step 3: Setup Git Repository (20 minutes)**

```bash
# 1. Initialize Git
cd APU-ASC-System
git init

# 2. Create .gitignore file
echo "bin/
.classpath
.project
.settings/
*.class
*.jar
*~
.DS_Store
.idea/
*.iml" > .gitignore

# 3. Initial commit
git add .
git commit -m "Initial project setup"

# 4. Add remote (create GitHub account first at github.com)
git remote add origin https://github.com/yourusername/APU-ASC-System.git
git branch -M main
git push -u origin main
```


# FILE CREATION CHECKLIST

## 📋 COMPLETE FILE LIST WITH PRIORITIES

### **WEEK 1 - ESSENTIAL FILES (Must be done by April 26)**

```
BACKEND (Noora) - 11 Files
☐ com/asu/model/User.java                    [Abstract base class]
☐ com/asu/model/Manager.java                 [Extends User]
☐ com/asu/model/CounterStaff.java            [Extends User]
☐ com/asu/model/Technician.java              [Extends User]
☐ com/asu/model/Customer.java                [Extends User]
☐ com/asu/model/Service.java
☐ com/asu/model/Appointment.java
☐ com/asu/model/Payment.java
☐ com/asu/model/Receipt.java
☐ com/asu/model/Feedback.java
☐ com/asu/model/Report.java

DATA LAYER (Namonje) - 9 Files
☐ com/asu/data/DataPersistence.java          [Interface]
☐ com/asu/data/FileHandler.java
☐ com/asu/data/UserDataManager.java
☐ com/asu/data/ServiceDataManager.java
☐ com/asu/data/AppointmentDataManager.java
☐ com/asu/data/PaymentDataManager.java
☐ com/asu/data/FeedbackDataManager.java
☐ com/asu/data/DataValidator.java
☐ com/asu/data/BackupManager.java

GUI (Zulqarnain) - 10+ Files
☐ com/asu/ui/UIConstants.java
☐ com/asu/ui/LoginFrame.java
☐ com/asu/ui/panels/ManagerPanel.java
☐ com/asu/ui/panels/CounterStaffPanel.java
☐ com/asu/ui/panels/TechnicianPanel.java
☐ com/asu/ui/panels/CustomerPanel.java
☐ com/asu/dialogs/RegistrationDialog.java
☐ com/asu/dialogs/AppointmentDialog.java
☐ com/asu/dialogs/PaymentDialog.java
☐ com/asu/dialogs/StaffManagementDialog.java
☐ com/asu/ui/DialogUtils.java

DATA FILES (Namonje) - 5 Files
☐ data/users.txt                             [Sample users]
☐ data/services.txt                          [Sample services]
☐ data/appointments.txt                      [Sample appointments]
☐ data/payments.txt                          [Sample payments]
☐ data/feedback.txt                          [Sample feedback]

DOCUMENTATION (Estella) - 5 Files
☐ TestPlan.md                                [15+ test cases]
☐ CodeReviewChecklist.md
☐ TestResults_Phase1.md
☐ TestResults_Phase2.md
☐ TestResults_Phase3.md
```

### **WEEK 2 - INTEGRATION & DOCUMENTATION FILES**

```
INTEGRATION (All) - 2 Files
☐ com/asu/main/APUAutomotiveServiceApp.java  [Main entry point]
☐ com/asu/ui/MainApplicationFrame.java       [Main window]

UTILITIES (Noora) - 2 Files
☐ com/asu/util/BusinessLogic.java
☐ com/asu/util/ValidationUtils.java

FINAL DOCUMENTATION (Estella) - 6+ Files
☐ docs/Report.pdf                            [Complete report]
☐ docs/OOP_Concepts_Section.md
☐ docs/System_Architecture.md
☐ docs/Limitations_Conclusion.md
☐ docs/Screenshots_with_Captions.md
☐ docs/References.md
☐ docs/Screenshots/                          [15+ screenshot files]
```

### **SUBMISSION FILES**

```
☐ APU_ASC_System.zip                         [All source code]
☐ APU_ASC_Report.pdf                         [Final report]
☐ README.txt                                 [Instructions]
```

---

# INTEGRATION POINTS & DATA FLOW

##  HOW EVERYTHING CONNECTS

### **Data Flow Diagram**

```
User Input (UI)
    ↓
LoginFrame → UserDataManager → users.txt
    ↓
(Login validation)
    ↓
MainApplicationFrame (opens appropriate panel)
    ↓
┌─────────────┬──────────────┬──────────────┬──────────────┐
│             │              │              │              │
ManagerPanel  CounterStaffPanel TechnicianPanel CustomerPanel
    ↓              ↓              ↓              ↓
┌─────────────┬──────────────┬──────────────┬──────────────┐
│             │              │              │              │
User Model  Service Model  Appointment    Payment Model
Data Mgr    Data Mgr       Data Mgr       Data Mgr
    ↓              ↓              ↓              ↓
┌─────────────┬──────────────┬──────────────┬──────────────┐
│             │              │              │              │
users.txt  services.txt  appointments.txt payments.txt
            feedback.txt
```

### **Method Call Flow Example: Creating an Appointment**

```
1. User clicks "Create Appointment" in CounterStaffPanel
   ↓
2. AppointmentDialog opens
   ↓
3. User fills form and clicks "Create"
   ↓
4. Dialog validates data using ValidationUtils.validateAppointment()
   ↓
5. If valid, creates Appointment object
   ↓
6. Calls AppointmentDataManager.addAppointment(appointment)
   ↓
7. DataManager converts Appointment to String format
   ↓
8. FileHandler.readFile("appointments.txt") gets all appointments
   ↓
9. Adds new appointment to list
   ↓
10. FileHandler.writeToFile("appointments.txt", updatedList)
   ↓
11. Shows success message to user
   ↓
12. CounterStaffPanel.refreshAppointmentList() reloads from file
```

---

## 🔗 CLASS DEPENDENCIES

### **Which Classes Depend On Which**

```
UIFrames depend on:
├── User (for login info)
├── UserDataManager (to validate login)
├── AppointmentDataManager (to load appointments)
├── PaymentDataManager (to load payments)
├── FeedbackDataManager (to load feedback)
└── DialogUtils (for common dialogs)

DataManagers depend on:
├── FileHandler (to read/write files)
├── DataValidator (to validate data)
├── Model classes (to create/update objects)
└── BackupManager (for backup operations)

Model classes depend on:
└── Nothing (they're independent)

BusinessLogic depends on:
├── All Model classes
├── All DataManagers
└── ValidationUtils
```

### **Import Statements Guide**

```java
// In LoginFrame.java
import com.asu.model.*;
import com.asu.data.*;
import javax.swing.*;
import java.io.IOException;

// In UserDataManager.java
import com.asu.model.User;
import java.io.IOException;
import java.util.*;

// In ManagerPanel.java
import com.asu.model.*;
import com.asu.data.*;
import com.asu.ui.*;
import javax.swing.*;
import javax.swing.table.*;
```

---

# TESTING & QUALITY ASSURANCE

## ✅ TESTING STRATEGY

### **Unit Testing (Individual Components)**

Each developer tests their own code as they write it.

**Noora's Backend Testing:**
```java
// Test User class instantiation
Manager manager = new Manager("M001", "Ali", "0122334455", "ali", "pass");
System.out.println(manager.toString());
// Expected: M001|Ali|0122334455|ali|pass|MANAGER

// Test method functionality
manager.addStaff(technician);
System.out.println("Staff added successfully");

// Test exception handling
try {
    // Test invalid data
} catch (Exception e) {
    System.out.println("Exception caught correctly");
}
```

**Namonje's Data Testing:**
```java
// Test FileHandler
FileHandler.createFile("users.txt");
System.out.println(FileHandler.fileExists("users.txt")); // Should be true

// Test UserDataManager
User newUser = new Manager(...);
UserDataManager.addUser(newUser);
User retrievedUser = UserDataManager.getUserById("M001");
System.out.println(retrievedUser.toString()); // Should match
```

**Zulqarnain's GUI Testing:**
```
Manual testing:
1. Run application
2. Click on login button (should show error if empty)
3. Enter valid credentials (should open main frame)
4. Click logout (should return to login)
5. Check window resizing (should maintain layout)
```

### **Integration Testing (Components Together)**

Test how components work together.

```
Test Scenario 1: Manager Login
1. Open application
2. Enter manager credentials
3. Verify manager panel opens
4. Verify staff list loads from database
5. Verify staff management buttons work

Test Scenario 2: Create Appointment
1. Login as counter staff
2. Click "Create Appointment"
3. Fill appointment details
4. Click "Create"
5. Verify appointment appears in technician's list
6. Verify data persists (close and reopen app)
```

### **System Testing (Full Workflows)**

Test complete user workflows.

```
Complete Workflow Test:
1. Manager login → manage staff
2. Counter staff login → create appointment → collect payment
3. Technician login → view assignments → update status
4. Customer login → view history → add comment
5. Verify all data is correctly saved in text files
```

---

# SUBMISSION PREPARATION

##  FINAL SUBMISSION STEPS

### **Step 1: Code Cleanup (April 30)**

```bash
# Remove all debug prints
grep -r "System.out.println" src/
# (Replace with actual logging if needed)

# Check for unused imports
# (Use IDE's code cleanup feature)

# Format code consistently
# IntelliJ: Code → Reformat Code

# Final compilation
javac -d bin src/com/asu/model/*.java
javac -d bin src/com/asu/data/*.java
javac -d bin src/com/asu/ui/*.java
javac -d bin src/com/asu/dialogs/*.java
```

### **Step 2: Create Submission Package**

```bash
# 1. Create final folder structure
mkdir APU_ASC_System_Final
cd APU_ASC_System_Final

# 2. Copy all source files
cp -r ../src .
cp -r ../data .

# 3. Create README.txt
cat > README.txt << 'EOF'
APU AUTOMOTIVE SERVICE CENTRE (APU-ASC)
========================================

HOW TO COMPILE:
  javac -d bin src/com/asu/model/*.java
  javac -d bin src/com/asu/data/*.java
  javac -d bin src/com/asu/ui/*.java
  javac -d bin src/com/asu/dialogs/*.java
  javac -d bin src/com/asu/util/*.java
  javac -d bin src/com/asu/main/*.java

HOW TO RUN:
  java -cp bin com.asu.ui.LoginFrame

DEFAULT LOGIN CREDENTIALS:
  Manager:      username: alimanager, password: pass123
  CounterStaff: username: sara, password: pass456
  Technician:   username: mohamad, password: pass789
  Customer:     username: fatimah, password: pass001

DATA FILES:
  All data stored in text files in 'data/' folder
  Format: Pipe-delimited (|) fields per line

FEATURES IMPLEMENTED:
  ✓ User login/logout for 4 roles
  ✓ Staff management
  ✓ Service management
  ✓ Appointment creation & assignment
  ✓ Payment processing & receipts
  ✓ Feedback submission
  ✓ History viewing
  ✓ Data persistence to text files

LIMITATIONS:
  - No database (text files only)
  - Single instance application
  - No network functionality
  - Performance limited by file size

TEAM:
  Noora: Backend Development
  Namonje: Data Persistence
  Zulqarnain: Frontend Development
  Estella: Documentation & QA
EOF

# 4. Create ZIP file
zip -r APU_ASC_System.zip src data README.txt
```

### **Step 3: Finalize Report**

```
Report.pdf should include:
☐ Cover page (all required info)
☐ Table of contents
☐ UML diagrams (class + use case)
☐ System overview
☐ OOP concepts (6 pages):
    - Inheritance explanation + code examples
    - Polymorphism explanation + code examples
    - Encapsulation explanation + code examples
    - Abstraction explanation + code examples
    - Composition explanation + code examples
    - Association explanation + code examples
☐ Implementation details with 15+ screenshots
☐ Test results (all test cases executed)
☐ Limitations and conclusion
☐ References
☐ Appendix (text file samples)
```

### **Step 4: Final Checks**

```
Before submission:
☐ Code compiles without errors
☐ Application runs without crashes
☐ All 4 user roles work
☐ All features functional
☐ Data persists to text files
☐ All UI elements present
☐ Report is complete and professional
☐ README.txt is clear and accurate
☐ ZIP file created successfully
☐ Both files ready for upload
```


### **Commands We'll Use Daily**

```bash
# Check Java version
java -version

# Compile all Java files
javac -d bin src/com/asu/**/*.java

# Run application
java -cp bin com.asu.ui.LoginFrame

# Git workflow
git pull              # Get latest
git add .
git commit -m "message"
git push              # Share with team
```

### **Common Problems & Solutions**

| Problem | Solution |
|---------|----------|
| "javac not found" | Add Java to PATH environment variable |
| "File not found" | Check data/ folder exists, use correct filenames |
| "Class not found" | Check package names and import statements |
| "UI doesn't show" | Check JFrame.setVisible(true) is called |
| "Data not saving" | Check file permissions, data format correct |
| "Merge conflict" | Edit file, resolve conflict, commit again |

---


