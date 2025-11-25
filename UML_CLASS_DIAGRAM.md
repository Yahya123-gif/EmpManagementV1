# UML Class Diagram

This document describes the class structure and relationships in the Employee Management System.

## Class Diagram Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         APPLICATION LAYER                        │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────┐
│      App         │
├──────────────────┤
│ +main(String[])  │
│ +start(Stage)    │
└──────────────────┘
         │
         │ launches
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CONTROLLER LAYER                            │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────┐
│  DashboardController     │
├──────────────────────────┤
│ -totalEmployeesLabel     │
│ -totalDepartmentsLabel   │
│ -pendingLeavesLabel      │
│ -approvedLeavesLabel     │
│ -employeeService         │
│ -departmentService       │
│ -leaveService           │
├──────────────────────────┤
│ +initialize()           │
│ +showDashboard()        │
│ +openEmployees()        │
│ +openDepartments()      │
│ +openLeaves()           │
│ -loadStats()            │
│ -loadScene()            │
└──────────────────────────┘
         │
         │ uses
         ▼
┌──────────────────────────┐      ┌──────────────────────────┐
│  EmployeeController      │      │ DepartmentController    │
├──────────────────────────┤      ├──────────────────────────┤
│ -employeeTable           │      │ -departmentTable         │
│ -employeeList            │      │ -departmentList          │
│ -employeeService         │      │ -departmentService       │
│ -departmentService       │      ├──────────────────────────┤
├──────────────────────────┤      │ +initialize()            │
│ +initialize()           │      │ +handleAdd()             │
│ +handleAdd()            │      │ +handleEdit()            │
│ +handleEdit()           │      │ +handleDelete()          │
│ +handleDelete()         │      │ +handleDashboard()       │
│ +refreshTable()         │      │ +refreshTable()          │
│ -showEmployeeForm()     │      │ -showDepartmentForm()    │
└──────────────────────────┘      └──────────────────────────┘
         │                                  │
         │ uses                             │ uses
         ▼                                  ▼
┌──────────────────────────┐      ┌──────────────────────────┐
│ EmployeeFormController   │      │ DepartmentFormController  │
├──────────────────────────┤      ├──────────────────────────┤
│ -nameField               │      │ -nameField               │
│ -emailField              │      │ -descriptionArea         │
│ -positionField           │      │ -department              │
│ -departmentCombo         │      │ -departmentController    │
│ -hireDatePicker          │      │ -departmentService       │
│ -employee                │      ├──────────────────────────┤
│ -employeeController      │      │ +setDepartment()         │
│ -employeeService         │      │ +handleSave()            │
│ -departmentService       │      │ +handleCancel()         │
├──────────────────────────┤      └──────────────────────────┘
│ +setEmployee()          │
│ +handleSave()           │
│ +handleCancel()         │
└──────────────────────────┘

┌──────────────────────────┐
│   LeaveController        │
├──────────────────────────┤
│ -leaveTable              │
│ -leaveList               │
│ -leaveService           │
├──────────────────────────┤
│ +initialize()           │
│ +handleAdd()            │
│ +handleAccept()         │
│ +handleRefuse()         │
│ +handleDelete()         │
│ +refreshTable()         │
│ -setupTable()           │
└──────────────────────────┘
         │
         │ uses
         ▼
┌──────────────────────────┐
│  LeaveFormController     │
├──────────────────────────┤
│ -employeeCombo           │
│ -startDatePicker         │
│ -endDatePicker           │
│ -reasonArea              │
│ -leaveController        │
│ -leaveService           │
│ -employeeService        │
├──────────────────────────┤
│ +setLeaveController()   │
│ +handleSave()           │
│ +handleCancel()         │
└──────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                            │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────┐
│     MongoService         │
├──────────────────────────┤
│ -instance                │
│ -mongoClient             │
│ -database                │
├──────────────────────────┤
│ +getInstance()           │
│ +getDatabase()           │
└──────────────────────────┘
         │
         │ used by
         ▼
┌──────────────────────────┐      ┌──────────────────────────┐
│   EmployeeService        │      │  DepartmentService       │
├──────────────────────────┤      ├──────────────────────────┤
│ -collection              │      │ -collection              │
├──────────────────────────┤      ├──────────────────────────┤
│ +save(Employee)          │      │ +save(Department)        │
│ +findAll()               │      │ +findAll()                │
│ +findById(String)       │      │ +findById(String)         │
│ +delete(String)          │      │ +delete(String)           │
│ -mapToEmployee()         │      │ -mapToDepartment()       │
└──────────────────────────┘      └──────────────────────────┘

┌──────────────────────────┐
│     LeaveService         │
├──────────────────────────┤
│ -collection              │
│ -employeeService         │
├──────────────────────────┤
│ +save(LeaveRequest)      │
│ +findAll()               │
│ +findById(String)        │
│ +delete(String)          │
│ +updateStatus(String,    │
│            String)        │
│ -mapToLeaveRequest()     │
└──────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                          MODEL LAYER                             │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────┐
│      Employee            │
├──────────────────────────┤
│ -id: String              │
│ -name: String            │
│ -email: String           │
│ -position: String        │
│ -departmentId: String    │
│ -hireDate: LocalDate     │
├──────────────────────────┤
│ +getId()                 │
│ +setId(String)           │
│ +getName()               │
│ +setName(String)         │
│ +getEmail()              │
│ +setEmail(String)        │
│ +getPosition()           │
│ +setPosition(String)     │
│ +getDepartmentId()       │
│ +setDepartmentId(String) │
│ +getHireDate()           │
│ +setHireDate(LocalDate)  │
└──────────────────────────┘

┌──────────────────────────┐
│     Department           │
├──────────────────────────┤
│ -id: String              │
│ -name: String            │
│ -description: String    │
├──────────────────────────┤
│ +getId()                 │
│ +setId(String)           │
│ +getName()               │
│ +setName(String)         │
│ +getDescription()        │
│ +setDescription(String)  │
└──────────────────────────┘

┌──────────────────────────┐
│    LeaveRequest          │
├──────────────────────────┤
│ -id: String              │
│ -employeeId: String      │
│ -employeeName: String    │
│ -startDate: LocalDate    │
│ -endDate: LocalDate      │
│ -reason: String          │
│ -status: String          │
├──────────────────────────┤
│ +getId()                 │
│ +setId(String)           │
│ +getEmployeeId()         │
│ +setEmployeeId(String)   │
│ +getEmployeeName()        │
│ +setEmployeeName(String) │
│ +getStartDate()          │
│ +setStartDate(LocalDate) │
│ +getEndDate()            │
│ +setEndDate(LocalDate)   │
│ +getReason()             │
│ +setReason(String)       │
│ +getStatus()             │
│ +setStatus(String)       │
└──────────────────────────┘
```

## Relationships

### Inheritance
- None (all classes are independent)

### Composition
- **Controllers** contain references to **Services**
- **Services** contain references to **MongoService** (Singleton)
- **Services** work with **Model** classes

### Dependencies
- **Controllers** depend on **Services** for business logic
- **Services** depend on **Models** for data structure
- **Services** depend on **MongoService** for database connection
- **Controllers** depend on **JavaFX** components

### Associations
- **Employee** has a relationship with **Department** (via departmentId)
- **LeaveRequest** has a relationship with **Employee** (via employeeId)
- **LeaveService** uses **EmployeeService** to fetch employee names

## Class Responsibilities

### App
- Entry point of the application
- Initializes JavaFX application
- Loads the dashboard view

### Controllers
- Handle user interactions
- Update UI components
- Coordinate between views and services
- Manage navigation between screens

### Services
- Implement business logic
- Handle data persistence operations
- Manage MongoDB collections
- Transform data between MongoDB documents and Java objects

### Models
- Represent domain entities
- Store application data
- Provide getters and setters for data access

### MongoService
- Singleton pattern for database connection
- Provides centralized MongoDB access
- Manages connection lifecycle

## Data Flow

```
User Action → Controller → Service → MongoDB
                ↓
            Update UI ← Service ← MongoDB
```

## Design Patterns Used

1. **Singleton Pattern**: `MongoService` - ensures single database connection
2. **MVC Pattern**: Separation of Model, View (FXML), and Controller
3. **Service Layer Pattern**: Business logic separated from controllers
4. **Repository Pattern**: Services act as repositories for data access

