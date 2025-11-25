# Architecture Documentation

## System Architecture Overview

The Employee Management System follows a **layered architecture** pattern, providing clear separation of concerns and maintainability.

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Controllers │  │  FXML Views  │  │     CSS      │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         │                  │                  │               │
│         └──────────────────┼──────────────────┘               │
│                            │                                  │
│                    User Interface                            │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ User Interactions
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Employee    │  │  Department  │  │    Leave     │      │
│  │   Service    │  │   Service    │  │   Service   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         │                  │                  │               │
│         └──────────────────┼──────────────────┘               │
│                            │                                  │
│                    Business Logic                             │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ Data Operations
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                       MODEL LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Employee   │  │  Department  │  │ LeaveRequest │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                            │                                  │
│                    Domain Entities                            │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ Persistence
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   DATA ACCESS LAYER                          │
│  ┌────────────────────────────────────────────────────┐      │
│  │            MongoService (Singleton)                │      │
│  │  - MongoDB Connection Management                  │      │
│  │  - Database Access                                │      │
│  └────────────────────────────────────────────────────┘      │
│                            │                                  │
│                    MongoDB Database                           │
└─────────────────────────────────────────────────────────────┘
```

## Layer Details

### 1. Presentation Layer

**Purpose**: Handles user interface and user interactions

**Components**:
- **Controllers**: JavaFX controllers that manage UI logic
  - `DashboardController`: Main dashboard with statistics
  - `EmployeeController`: Employee list and management
  - `EmployeeFormController`: Employee add/edit form
  - `DepartmentController`: Department list and management
  - `DepartmentFormController`: Department add/edit form
  - `LeaveController`: Leave request list and management
  - `LeaveFormController`: Leave request add form

- **Views**: FXML files defining UI structure
  - `dashboard.fxml`: Main dashboard layout
  - `employee.fxml`: Employee list view
  - `employee-form.fxml`: Employee form view
  - `department.fxml`: Department list view
  - `department-form.fxml`: Department form view
  - `leave.fxml`: Leave request list view
  - `leave-form.fxml`: Leave request form view

- **Styles**: CSS for UI styling
  - `styles.css`: Complete application styling

**Responsibilities**:
- Display data to users
- Capture user input
- Handle user events
- Navigate between views
- Validate user input

### 2. Service Layer

**Purpose**: Implements business logic and data operations

**Components**:
- `EmployeeService`: Employee-related operations
- `DepartmentService`: Department-related operations
- `LeaveService`: Leave request-related operations
- `MongoService`: Database connection management

**Responsibilities**:
- Execute business rules
- Coordinate data operations
- Transform data between layers
- Handle data validation
- Manage transactions

**Key Methods**:
```java
// Common service pattern
+ save(entity): void
+ findAll(): List<Entity>
+ findById(id): Entity
+ delete(id): void
```

### 3. Model Layer

**Purpose**: Represents domain entities and data structures

**Components**:
- `Employee`: Employee entity
- `Department`: Department entity
- `LeaveRequest`: Leave request entity

**Responsibilities**:
- Define data structure
- Store application data
- Provide data access methods
- Represent business concepts

### 4. Data Access Layer

**Purpose**: Manages database connections and data persistence

**Components**:
- `MongoService`: Singleton service for MongoDB connection

**Responsibilities**:
- Establish database connections
- Manage connection lifecycle
- Provide database access
- Handle connection errors

## Design Patterns

### 1. Singleton Pattern
**Used in**: `MongoService`
```java
private static MongoService instance;
public static MongoService getInstance() {
    if (instance == null) {
        instance = new MongoService();
    }
    return instance;
}
```
**Purpose**: Ensure single database connection instance

### 2. MVC Pattern (Model-View-Controller)
- **Model**: Domain entities (Employee, Department, LeaveRequest)
- **View**: FXML files
- **Controller**: JavaFX controllers

**Purpose**: Separate concerns and improve maintainability

### 3. Service Layer Pattern
- Services encapsulate business logic
- Controllers delegate to services
- Services handle data persistence

**Purpose**: Centralize business logic and improve testability

### 4. Repository Pattern (Implicit)
- Services act as repositories
- Abstract data access details
- Provide consistent interface

**Purpose**: Decouple data access from business logic

## Data Flow

### Creating a New Employee

```
1. User clicks "Add Employee" button
   ↓
2. EmployeeController.handleAdd()
   ↓
3. EmployeeFormController opens
   ↓
4. User fills form and clicks "Save"
   ↓
5. EmployeeFormController.handleSave()
   ↓
6. EmployeeService.save(employee)
   ↓
7. MongoService.getDatabase()
   ↓
8. MongoDB Collection.insertOne()
   ↓
9. EmployeeController.refreshTable()
   ↓
10. UI updates with new employee
```

### Viewing Statistics

```
1. DashboardController.initialize()
   ↓
2. EmployeeService.findAll()
   ↓
3. DepartmentService.findAll()
   ↓
4. LeaveService.findAll()
   ↓
5. Calculate statistics
   ↓
6. Update UI labels
```

## Database Schema

### MongoDB Collections

#### employees
```javascript
{
  _id: ObjectId,
  name: String,
  email: String,
  position: String,
  departmentId: String,
  hireDate: Date
}
```

#### departments
```javascript
{
  _id: ObjectId,
  name: String,
  description: String
}
```

#### leave_requests
```javascript
{
  _id: ObjectId,
  employeeId: String,
  startDate: Date,
  endDate: Date,
  reason: String,
  status: String  // "PENDING", "APPROVED", "REJECTED"
}
```

## Component Interactions

### Controller-Service Interaction

```java
// Controller
EmployeeController {
    private EmployeeService employeeService;
    
    void handleAdd() {
        Employee employee = new Employee();
        employeeService.save(employee);
    }
}

// Service
EmployeeService {
    private MongoCollection<Document> collection;
    
    void save(Employee employee) {
        Document doc = convertToDocument(employee);
        collection.insertOne(doc);
    }
}
```

### Service-Model Interaction

```java
// Service converts between Model and Document
Employee mapToEmployee(Document doc) {
    Employee emp = new Employee();
    emp.setId(doc.getObjectId("_id").toString());
    emp.setName(doc.getString("name"));
    // ... more mappings
    return emp;
}
```

## Error Handling

### Current Approach
- Basic exception handling in controllers
- Service methods may throw exceptions
- UI shows alert dialogs for errors

### Future Improvements
- Centralized error handling
- Custom exception classes
- Error logging
- User-friendly error messages

## Security Considerations

### Current State
- No authentication/authorization
- Direct database access
- No input sanitization

### Recommendations
- Add user authentication
- Implement role-based access control
- Validate and sanitize all inputs
- Use prepared statements (if using SQL)
- Encrypt sensitive data

## Scalability Considerations

### Current Architecture
- Single-threaded UI (JavaFX)
- Direct MongoDB connection
- In-memory data loading

### Future Enhancements
- Background data loading
- Caching layer
- Connection pooling
- Pagination for large datasets
- Async operations

## Testing Strategy

### Recommended Testing Layers

1. **Unit Tests**: Service layer methods
2. **Integration Tests**: Database operations
3. **UI Tests**: Controller behavior
4. **End-to-End Tests**: Complete user flows

### Testing Tools
- JUnit for unit tests
- TestFX for JavaFX UI tests
- Mockito for mocking dependencies

## Deployment Architecture

### Development
```
Developer Machine
├── JavaFX Application
├── MongoDB (Local)
└── IDE (IntelliJ/Eclipse)
```

### Production (Recommended)
```
Application Server
├── JavaFX Application (Packaged)
└── MongoDB Server (Remote)
```

## Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| UI Framework | JavaFX | 17.0.2 |
| Language | Java | 11+ |
| Database | MongoDB | 4.0+ |
| Build Tool | Maven | 3.6+ |
| UI Markup | FXML | - |
| Styling | CSS | - |

## Future Architecture Improvements

1. **Add Dependency Injection**: Use Spring or Guice
2. **Implement Caching**: Add Redis or in-memory cache
3. **Add API Layer**: RESTful API for web access
4. **Microservices**: Split into separate services
5. **Event-Driven**: Add message queue for async operations
6. **Cloud Deployment**: Docker containers, Kubernetes

## Conclusion

The current architecture provides:
- ✅ Clear separation of concerns
- ✅ Maintainable code structure
- ✅ Scalable design
- ✅ Easy to understand and modify

The layered approach allows for:
- Independent testing of each layer
- Easy replacement of components
- Clear data flow
- Maintainable codebase

