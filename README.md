# Employee Management System

A modern JavaFX desktop application for managing employees, departments, and leave requests with MongoDB database integration.

## 📋 Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Architecture](#architecture)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### Dashboard
- Real-time statistics overview
- Quick navigation to all modules
- Visual stat cards with icons
- Last updated timestamp

### Employee Management
- ✅ Add new employees
- ✏️ Edit employee information
- 🗑️ Delete employees
- 📊 View all employees in a modern table
- 🔍 Search and filter capabilities

### Department Management
- ✅ Create departments
- ✏️ Update department details
- 🗑️ Remove departments
- 📋 List all departments

### Leave Request Management
- ✅ Create leave requests
- ✅ Approve leave requests
- ❌ Reject leave requests
- 🗑️ Delete leave requests
- 📊 View all leave requests with status indicators
- 🔗 Link leave requests to employees

## 🛠️ Technologies

- **Java 11+** - Programming language
- **JavaFX 17** - UI framework
- **MongoDB 4.0+** - NoSQL database
- **Maven** - Build tool and dependency management
- **FXML** - UI markup language
- **CSS** - Styling

## 📁 Project Structure

```
employee-management/
├── src/
│   └── main/
│       ├── java/
│       │   ├── app/
│       │   │   └── App.java                 # Main application entry point
│       │   ├── controllers/                # JavaFX controllers
│       │   │   ├── DashboardController.java
│       │   │   ├── EmployeeController.java
│       │   │   ├── EmployeeFormController.java
│       │   │   ├── DepartmentController.java
│       │   │   ├── DepartmentFormController.java
│       │   │   ├── LeaveController.java
│       │   │   └── LeaveFormController.java
│       │   ├── model/                       # Data models
│       │   │   ├── Employee.java
│       │   │   ├── Department.java
│       │   │   └── LeaveRequest.java
│       │   └── services/                    # Business logic layer
│       │       ├── MongoService.java        # MongoDB connection
│       │       ├── EmployeeService.java
│       │       ├── DepartmentService.java
│       │       └── LeaveService.java
│       └── resources/
│           ├── styles.css                   # Application styles
│           └── views/                       # FXML views
│               ├── dashboard.fxml
│               ├── employee.fxml
│               ├── employee-form.fxml
│               ├── department.fxml
│               ├── department-form.fxml
│               ├── leave.fxml
│               └── leave-form.fxml
├── pom.xml                                  # Maven configuration
├── README.md                                 # This file
├── ARCHITECTURE.md                           # Architecture documentation
└── UML_CLASS_DIAGRAM.md                     # UML class diagram

```

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java Development Kit (JDK) 11 or higher**
  ```bash
  java -version
  ```

- **Apache Maven 3.6+**
  ```bash
  mvn -version
  ```

- **MongoDB 4.0+** installed and running
  ```bash
  mongod --version
  ```

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd employee-management
```

### 2. Start MongoDB

Make sure MongoDB is running on your system:

```bash
# On Linux/Mac
mongod

# On Windows
mongod.exe

# Or if installed as a service
sudo systemctl start mongod
```

MongoDB should be running on `localhost:27017` (default port).

### 3. Build the Project

```bash
mvn clean compile
```

### 4. Run the Application

```bash
mvn javafx:run
```

Or compile and run manually:

```bash
mvn clean package
java --module-path /path/to/javafx/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp target/classes:target/dependency/* \
     app.App
```

## 💻 Usage

### Starting the Application

1. Launch the application using `mvn javafx:run`
2. The dashboard will open showing statistics

### Managing Employees

1. Click **"Employees"** in the sidebar
2. Click **"➕ Add Employee"** to create a new employee
3. Fill in the form:
   - Name
   - Email
   - Position
   - Department (select from dropdown)
   - Hire Date
4. Click **"Save"** to add the employee
5. Select an employee and click **"✏️ Edit"** to modify
6. Select an employee and click **"🗑️ Delete"** to remove

### Managing Departments

1. Click **"Departments"** in the sidebar
2. Click **"➕ Add Department"** to create a new department
3. Enter department name and description
4. Click **"Save"**
5. Use **"✏️ Edit"** or **"🗑️ Delete"** as needed

### Managing Leave Requests

1. Click **"Leave Requests"** in the sidebar
2. Click **"➕ Add Leave"** to create a new request
3. Select employee, dates, and reason
4. Click **"Save"** (status will be PENDING)
5. Select a request and click:
   - **"✅ Accept"** to approve
   - **"❌ Refuse"** to reject
   - **"🗑️ Delete"** to remove

## 🏗️ Architecture

The application follows a **layered architecture** pattern:

```
┌─────────────────────────────────────┐
│      Presentation Layer (JavaFX)    │
│  Controllers + FXML Views + CSS     │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│         Service Layer                │
│  Business Logic + Data Operations     │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│         Model Layer                  │
│  Domain Entities (POJOs)            │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Data Access Layer (MongoDB)    │
│  MongoDB Connection + Collections    │
└─────────────────────────────────────┘
```

For detailed architecture documentation, see [ARCHITECTURE.md](ARCHITECTURE.md).

## 🎨 UI/UX Features

- **Modern Design**: Gradient colors and smooth animations
- **Responsive Layout**: Sidebar navigation with active state indicators
- **Interactive Tables**: Hover effects, alternating row colors, status badges
- **Smooth Transitions**: Animated buttons, cards, and form elements
- **Color-Coded Status**: Visual indicators for leave request statuses
- **Professional Styling**: Consistent design language throughout

## 📊 Database Schema

### Collections

- **employees**: Stores employee information
- **departments**: Stores department data
- **leave_requests**: Stores leave request records

### Sample Documents

**Employee:**
```json
{
  "_id": ObjectId("..."),
  "name": "John Doe",
  "email": "john.doe@company.com",
  "position": "Software Engineer",
  "departmentId": "dept123",
  "hireDate": ISODate("2023-01-15")
}
```

**Department:**
```json
{
  "_id": ObjectId("..."),
  "name": "Engineering",
  "description": "Software development team"
}
```

**Leave Request:**
```json
{
  "_id": ObjectId("..."),
  "employeeId": "emp123",
  "startDate": ISODate("2024-01-10"),
  "endDate": ISODate("2024-01-15"),
  "reason": "Vacation",
  "status": "PENDING"
}
```

## 🐛 Troubleshooting

### MongoDB Connection Issues

If you encounter connection errors:

1. Verify MongoDB is running:
   ```bash
   sudo systemctl status mongod
   ```

2. Check MongoDB port (default: 27017):
   ```bash
   netstat -an | grep 27017
   ```

3. Update connection string in `MongoService.java` if needed

### JavaFX Runtime Issues

If JavaFX doesn't load:

1. Ensure JavaFX dependencies are in `pom.xml`
2. Check Java version compatibility (Java 11+)
3. Verify JavaFX modules are available

### Build Errors

1. Clean and rebuild:
   ```bash
   mvn clean install
   ```

2. Check Maven version:
   ```bash
   mvn -version
   ```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👤 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- MongoDB for robust database solution
- All contributors and testers

---

**Made with ❤️ using JavaFX and MongoDB**
