# APU-ASC PROJECT - QUICK REFERENCE GUIDE
## Team Member Checklists & Code Structure

---

## 👤 NOORA - Backend Developer

### Quick Role Summary
- **Lead:** Overall project architecture & code quality
- **Primary:** User classes, Service, Payment, Appointment logic
- **Secondary:** QA and code reviews

### Daily Checklist Template
```
April 22:
✓ Created User.java (base class, login/logout)
✓ Created Manager.java, CounterStaff.java, Technician.java, Customer.java
✓ All classes compile without errors
✓ Shared code with team

April 23:
✓ Created Service.java with all methods
✓ Created Report.java with generateReport() method
✓ Updated price method works correctly
✓ Code reviewed and tested

[Continue daily...]
```

### Key Java Classes to Create
```
1. User.java (Abstract/Base Class)
   - Attributes: userId, name, phone, username, password
   - Methods: login(), logout(), abstract updateProfile()

2. Manager.java (extends User)
   - Methods: addStaff(), removeStaff(), setServicePrice(), viewReports()

3. CounterStaff.java (extends User)
   - Methods: addCustomer(), createAppointment(), assignTechnician(), collectPayment()

4. Technician.java (extends User)
   - Methods: viewAssignedAppointments(), updateStatus(), provideFeedback()

5. Customer.java (extends User)
   - Methods: viewAppointmentDetails(), viewPaymentHistory(), addComment()

6. Service.java
   - Attributes: serviceId, serviceName, serviceType, price, duration
   - Methods: updatePrice(), getServiceDetails()

7. Appointment.java
   - Attributes: appointmentId, appointmentDate, appointmentTime, status, vehicleNo, issueDescription
   - Methods: assignTechnician(), updateStatus(), viewDetails()

8. Payment.java
   - Attributes: paymentId, appointmentId, amount, paymentDate, paymentMethod, paymentStatus
   - Methods: processPayment(), calculateTotal()

9. Receipt.java
   - Attributes: receiptId, receiptDate, paymentDetails
   - Methods: generateReceipt(), printReceipt()

10. Feedback.java
    - Attributes: feedbackId, appointmentId, comment, feedbackDate
    - Methods: addFeedback(), viewFeedback()
```

### Code Quality Checklist
- [ ] All classes have proper access modifiers (private attributes, public methods)
- [ ] All methods have meaningful names following camelCase
- [ ] No public static variables (use getters/setters)
- [ ] Each class has ONE primary responsibility
- [ ] Constructors initialize all attributes
- [ ] Proper inheritance hierarchy implemented
- [ ] Method overriding used correctly
- [ ] Comments explain complex logic

---

## 👤 NAMONJE - Data Persistence Specialist

### Quick Role Summary
- **Lead:** All file I/O and data persistence
- **Primary:** Text file management, CRUD operations
- **Secondary:** Customer module if needed

### Daily Checklist Template
```
April 22:
✓ Created FileHandler.java with read/write methods
✓ Tested file operations (create, read, update, delete)
✓ Proper error handling implemented
✓ Shared with team

April 23:
✓ Created UserDataManager.java
✓ Tested loading/saving users from users.txt
✓ File format verified: userId|name|phone|username|password|userType

[Continue daily...]
```

### Data File Specifications

#### users.txt Format
```
userId|name|phone|username|password|userType
U001|Ali Manager|0122334455|alimanager|pass123|MANAGER
U002|Sara CounterStaff|0134556677|sara|pass456|COUNTERSTAFF
U003|Mohamad Technician|0167788990|mohamad|pass789|TECHNICIAN
U004|Fatimah Customer|0198899001|fatimah|pass001|CUSTOMER
```

#### services.txt Format
```
serviceId|serviceName|serviceType|price|duration
S001|Oil Change|NORMAL|150.00|1
S002|Full Service|MAJOR|500.00|3
S003|Brake Check|NORMAL|100.00|1
```

#### appointments.txt Format
```
appointmentId|appointmentDate|appointmentTime|status|vehicleNo|issueDescription|technicianId|customerId
A001|2026-04-25|10:00|ASSIGNED|ABC123|Engine Noise|U003|U004
A002|2026-04-26|14:00|PENDING|DEF456|Brake Issue|UNASSIGNED|U005
```

#### payments.txt Format
```
paymentId|appointmentId|amount|paymentDate|paymentMethod|paymentStatus
P001|A001|150.00|2026-04-25|CASH|COMPLETED
P002|A002|500.00|2026-04-26|CARD|PENDING
```

#### feedback.txt Format
```
feedbackId|appointmentId|comment|feedbackDate|feedbackFrom|feedbackFor
F001|A001|Excellent service!|2026-04-25|U004|U003
F002|A002|Good work|2026-04-26|U005|U002
```

### Java Classes to Create
```
1. FileHandler.java
   - Methods: createFile(), readFile(), writeFile(), deleteFile(), fileExists()

2. DataPersistence.java (Interface)
   - Methods: save(), load(), update(), delete()

3. UserDataManager.java (implements DataPersistence)
   - Methods: addUser(), removeUser(), updateUser(), getUserById(), getAllUsers()
   - Data Source: users.txt

4. ServiceDataManager.java (implements DataPersistence)
   - Methods: addService(), updateService(), getService(), getAllServices()
   - Data Source: services.txt

5. AppointmentDataManager.java (implements DataPersistence)
   - Methods: addAppointment(), updateAppointment(), getAppointment(), getAllAppointments()
   - Data Source: appointments.txt

6. PaymentDataManager.java (implements DataPersistence)
   - Methods: addPayment(), updatePayment(), getPayment(), getAllPayments()
   - Data Source: payments.txt

7. FeedbackDataManager.java (implements DataPersistence)
   - Methods: addFeedback(), getFeedback(), getAllFeedback()
   - Data Source: feedback.txt

8. DataValidator.java
   - Methods: validateUser(), validateAppointment(), validatePayment()

9. BackupManager.java
   - Methods: createBackup(), restoreBackup()
```

### File Operations Checklist
- [ ] All files created in correct format (pipe-delimited)
- [ ] Read operations parse data correctly
- [ ] Write operations append without duplicating
- [ ] Update operations modify correct record
- [ ] Delete operations remove correct record
- [ ] No data corruption on file operations
- [ ] File locking/atomic operations for safety
- [ ] Sample data files created for testing
- [ ] Error messages meaningful for debugging

---

## 👤 ZULQARNAIN - Frontend Developer

### Quick Role Summary
- **Lead:** All GUI components and user interface
- **Primary:** Swing/AWT implementation, user panels, dialogs
- **Secondary:** UI integration with backend

### Daily Checklist Template
```
April 22:
✓ Created LoginFrame.java with login form
✓ Created RegistrationDialog.java for new users
✓ Both compile and display correctly
✓ Shared with team

April 23:
✓ Created ManagerPanel.java with staff management
✓ Created StaffManagementDialog.java
✓ Tested with dummy data

[Continue daily...]
```

### GUI Components Structure
```
ApplicationFrame (Main Window)
├── JTabbedPane (Multiple tabs for roles)
├── LoginFrame
│   ├── JTextField (Username)
│   ├── JPasswordField (Password)
│   └── JButton (Login, Register)
│
├── ManagerPanel
│   ├── StaffManagementPanel
│   ├── ServicePricePanel
│   ├── FeedbackPanel
│   └── ReportPanel
│
├── CounterStaffPanel
│   ├── CustomerManagementPanel
│   ├── AppointmentCreationPanel
│   ├── PaymentPanel
│   └── ReceiptPanel
│
├── TechnicianPanel
│   ├── AppointmentDetailsPanel
│   ├── StatusUpdatePanel
│   └── FeedbackPanel
│
└── CustomerPanel
    ├── HistoryPanel
    ├── FeedbackPanel
    └── CommentPanel
```

### Java Classes to Create
```
1. LoginFrame.java (extends JFrame)
   - Components: username field, password field, login button
   - Method: actionPerformed() for login

2. RegistrationDialog.java (extends JDialog)
   - Components: all user fields
   - Validation before registration

3. MainApplicationFrame.java (extends JFrame)
   - Contains JTabbedPane for different user roles
   - MenuBar with logout option
   - Continuous operation logic

4. ManagerPanel.java (extends JPanel)
   - Buttons: Add Staff, Update Staff, Delete Staff
   - Buttons: Set Service Prices, View Feedback, Generate Report
   - Methods: populate data from backend

5. CounterStaffPanel.java (extends JPanel)
   - Buttons: Add Customer, Create Appointment, Collect Payment
   - TablePanel: Display customers and appointments
   - Methods: refresh data, handle button clicks

6. TechnicianPanel.java (extends JPanel)
   - Display: Assigned appointments in table
   - Buttons: View Details, Update Status, Provide Feedback
   - Methods: auto-refresh assigned appointments

7. CustomerPanel.java (extends JPanel)
   - Display: Service history, payment history in tables
   - Buttons: View Feedback, Add Comment
   - Methods: display personal records only

8. DialogUtils.java (Utility Class)
   - Methods: showMessage(), showError(), showConfirmation()
   - Provides consistent dialog style

9. UIConstants.java (Utility Class)
   - Constants: Colors, Fonts, Sizes for consistency
```

### UI Design Checklist
- [ ] Professional appearance (colors, fonts)
- [ ] Intuitive navigation between roles
- [ ] All required buttons present and functional
- [ ] Input validation on all forms
- [ ] Consistent styling across all panels
- [ ] Error messages clear and helpful
- [ ] Success feedback for user actions
- [ ] Tables display data properly
- [ ] Dialogs modal and responsive
- [ ] System never crashes or locks up

### Integration Checklist
- [ ] LoginFrame correctly validates credentials with User classes
- [ ] All panels connect to appropriate data managers
- [ ] Buttons call backend methods correctly
- [ ] Data displays refresh after modifications
- [ ] All role-based restrictions enforced
- [ ] System runs continuously without errors
- [ ] Performance acceptable with test data

---

## 👤 ESTELLA - Documentation Lead

### Quick Role Summary
- **Lead:** Report writing and documentation
- **Primary:** Test planning, OOP documentation, screenshots
- **Secondary:** Quality assurance coordination

### Daily Checklist Template
```
April 22:
✓ Created comprehensive test plan
✓ Defined test cases for all features
✓ Created documentation template
✓ Shared with team

April 23:
✓ Reviewed Noora's code
✓ Created code review feedback
✓ Started OOP outline
✓ Prepared test data specifications

[Continue daily...]
```

### Documentation Template Sections
```
REPORT TABLE OF CONTENTS:
1. Cover Page
2. Table of Contents
3. Design Solution
   3.1 Class Diagram (with explanation)
   3.2 Use Case Diagram (with explanation)
4. System Overview
5. Implementation Details
   5.1 Class Descriptions
   5.2 Method Descriptions
   5.3 Screenshots with explanations
6. Object-Oriented Concepts
   6.1 Inheritance
   6.2 Polymorphism
   6.3 Encapsulation
   6.4 Abstraction
   6.5 Composition
   6.6 Association
7. Features Demonstration
   7.1 Manager Features
   7.2 Counter Staff Features
   7.3 Technician Features
   7.4 Customer Features
8. Test Results
   8.1 Unit Testing
   8.2 Integration Testing
   8.3 System Testing
9. Additional Features (if any)
10. Limitations and Future Improvements
11. Conclusion
12. References
13. Appendix
    13.1 Code Snippets
    13.2 Text File Formats
    13.3 Sample Data Files
```

### OOP Concepts Documentation

#### Inheritance Example
```
Parent Class: User
├─ Subclass: Manager
├─ Subclass: CounterStaff
├─ Subclass: Technician
└─ Subclass: Customer

Document:
- Show class hierarchy diagram
- Explain why User is parent
- Show code for each subclass
- Explain method overriding in each subclass
```

#### Polymorphism Example
```
Method: login() - Different in each user type
Document:
- Show abstract method in User class
- Show implementation in each subclass
- Explain how same method name, different behavior
- Show code examples
```

#### Encapsulation Example
```
Class: User
- Private attributes (userId, name, phone, etc.)
- Public getters/setters
Document:
- Show private/public access modifiers
- Explain why attributes are private
- Show getter/setter methods
- Explain benefits of encapsulation
```

### Screenshot Checklist
Capture these scenarios:
- [ ] Login screen (Manager/CounterStaff/Technician/Customer)
- [ ] Main application frame
- [ ] Manager panel - all features
- [ ] CounterStaff panel - all features
- [ ] Technician panel - all features
- [ ] Customer panel - all features
- [ ] Sample dialogs (Add, Edit, Delete)
- [ ] Error message example
- [ ] Success message example
- [ ] Data tables populated with data
- [ ] Receipt generation
- [ ] Report generation
- [ ] Feedback/Comments submission
- [ ] System running continuously (multiple sessions)

### Test Plan Template
```
TEST CASE 1: User Login
Objective: Verify login functionality for all user types
Steps:
1. Open application
2. Enter username and password for Manager
3. Click Login button
Expected Result: Manager panel opens successfully
Status: PASS/FAIL
Notes: 

TEST CASE 2: Create New Appointment
Objective: Verify appointment creation by counter staff
Steps:
1. Login as Counter Staff
2. Click "Create Appointment"
3. Fill appointment details
4. Assign to technician
5. Click Save
Expected Result: Appointment saved, appears in technician's list
Status: PASS/FAIL
Notes:

[Continue for all features...]
```

### Documentation Writing Checklist
- [ ] Grammar and spelling correct throughout
- [ ] Professional formatting (margins, fonts, headers)
- [ ] All diagrams include explanations
- [ ] Code snippets are properly formatted
- [ ] Screenshots have descriptive captions
- [ ] OOP concepts clearly explained
- [ ] Technical accuracy verified
- [ ] References properly cited
- [ ] Table of contents matches page numbers
- [ ] Cover page complete with all required info
- [ ] Report is PDF (not Word) for submission

---

## 🔄 TEAM INTEGRATION POINTS

### Interface Between Noora (Backend) & Namonje (Data)
```
Noora creates classes:
- User, Manager, CounterStaff, Technician, Customer
- Service, Appointment, Payment, Receipt, Feedback

Namonje needs to:
- Create managers for each entity type
- Implement save/load for Noora's classes
- Call methods: user.getId(), user.getName(), etc.

Agreement:
- Getter/setter method names must match exactly
- All entities must have a unique ID attribute
- toString() method standard format for file storage
```

### Interface Between Zulqarnain (GUI) & Noora (Backend)
```
Zulqarnain creates:
- LoginFrame calls: new UserDataManager().validate(username, password)
- ManagerPanel buttons call: manager.addStaff(), manager.setServicePrice()
- PaymentPanel calls: payment.processPayment(), receipt.generateReceipt()

Agreement:
- All backend methods must return appropriate data types
- Exceptions must be thrown for errors
- Callbacks/listeners for async operations
- Clear parameter and return value documentation
```

### Interface Between Zulqarnain (GUI) & Namonje (Data)
```
Zulqarnain needs to:
- Load data: new ServiceDataManager().getAllServices()
- Display in tables: service.getId(), service.getName(), service.getPrice()
- Update data: manager.updateAppointment(appointmentId, newStatus)

Agreement:
- Consistent method naming for retrieving data
- Data objects must be serializable to files
- Refresh mechanisms after data modifications
```

### Coordination with Estella (Testing)
```
Estella needs access to:
- Source code from all members (for code review)
- Sample data files from Namonje
- UI screenshots from Zulqarnain
- Test results from all members

Coordination:
- Weekly test status meetings
- Bug reports documented and assigned
- Code review feedback provided
- Documentation quality verified
```

---

## 📊 PROGRESS TRACKING DASHBOARD

### Week 1 (April 21-28) Target: 70% Code Complete
```
Status: [████████░░] 80%

Noora (Backend):
  User Classes:     [████████░░] 90%
  Service/Payment:  [████████░░] 85%
  Business Logic:   [███████░░░] 75%

Namonje (Data):
  FileHandler:      [████████░░] 90%
  User Manager:     [████████░░] 85%
  Other Managers:   [███████░░░] 75%

Zulqarnain (GUI):
  Login/Register:   [████████░░] 90%
  Manager Panel:    [████████░░] 85%
  Other Panels:     [███████░░░] 75%

Estella (Docs):
  Planning:         [██████░░░░] 60%
  Test Plan:        [████████░░] 80%
  OOP Outline:      [██████░░░░] 60%
```

### Week 2 (April 29-30) Target: Integration Complete
```
Status: [██████░░░░] 60%

Integration:      [███░░░░░░░] 30%
Testing:          [██████░░░░] 60%
Documentation:    [████████░░] 80%
```

### Final Day (May 1) Target: 100% Complete
```
Status: [██████████] 100%

All code:         [██████████] 100%
All tests:        [██████████] 100%
All docs:         [██████████] 100%
Ready for submit: [██████████] 100%
```

---

## 🎯 DAILY STAND-UP TEMPLATE (5 minutes)

**Date:** April 22
**Facilitator:** Noora

### Noora
- Yesterday: Created User base class
- Today: Create Manager, CounterStaff, Technician, Customer classes
- Blockers: None

### Namonje
- Yesterday: Created FileHandler class
- Today: Create UserDataManager class
- Blockers: Need User class structure from Noora (in progress)

### Zulqarnain
- Yesterday: Created LoginFrame
- Today: Create RegistrationDialog
- Blockers: Need User class methods from Noora for validation

### Estella
- Yesterday: Created test plan
- Today: Review Noora's code, start OOP documentation
- Blockers: Need code from Noora to review

---

## ⚡ CODE STANDARDS

### Naming Conventions
```
Classes:        UserManager, AppointmentPanel (PascalCase)
Methods:        getUserName(), createAppointment() (camelCase)
Variables:      userId, userName, isActive (camelCase)
Constants:      MAX_USERS, DEFAULT_SERVICE_PRICE (UPPER_SNAKE_CASE)
Booleans:       isValid, hasPermission (is/has prefix)
```

### File Operations Standards
```
// DO:
StringBuilder sb = new StringBuilder();
try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    String line;
    while ((line = br.readLine()) != null) {
        // Process line
    }
} catch (IOException e) {
    e.printStackTrace();
}

// DON'T:
FileReader fr = new FileReader(file);  // Not closed properly!
Scanner sc = new Scanner(file);         // Resource leak!
```

### Class Structure Order
```
public class MyClass {
    // 1. Constants
    private static final int MAX_SIZE = 100;
    
    // 2. Static variables
    private static int counter = 0;
    
    // 3. Instance variables (attributes)
    private String id;
    private String name;
    
    // 4. Constructors
    public MyClass(String id, String name) {...}
    
    // 5. Public methods
    public String getId() {...}
    
    // 6. Private methods
    private void initialize() {...}
    
    // 7. toString() and equals()
    @Override
    public String toString() {...}
}
```

---

## 📞 CONTACT & ESCALATION

### For Technical Issues:
1. Try to solve within your area
2. Ask specific team member
3. Group discussion if complex
4. Document solution for future

### For Blocking Issues:
1. Notify project lead (Noora) immediately
2. Work on alternative task
3. Escalate if not resolved in 1 hour

### For Urgent Issues on May 1:
1. Contact ALL team members
2. Quick decision-making
3. Prioritize submission

---

**Good luck! Execute with focus and precision! 🚀**
