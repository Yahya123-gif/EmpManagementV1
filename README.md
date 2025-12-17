# Employee Management System


### Employee Management
- Add new employees
- Edit employee information
- Delete employees
- View all employees in a modern table
- Search and filter capabilities

### Department Management
-  Create departments
-  Update department details
-  Remove departments
-  List all departments

### Leave Request Management
-  Create leave requests
-  Approve leave requests
-   Reject leave requests
-   Delete leave requests
-   View all leave requests with status indicators
-   Link leave requests to employees

##  Technologies

- **Java 11+** - Programming language
- **JavaFX 17** - UI framework
- **MongoDB 4.0+** - NoSQL database
- **Maven** - Build tool and dependency management
- **FXML** - UI markup language
- **CSS** - Styling


##  Prerequisites

Before running this application, ensure you have:

- **Java Development Kit (JDK) 11 or higher**
  

- **Apache Maven 3.6+**


- **MongoDB 4.0+** installed and running
 
##  Installation

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






