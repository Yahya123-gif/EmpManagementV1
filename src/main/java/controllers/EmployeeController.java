package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Employee;
import services.EmployeeService;
import services.DepartmentService;

import java.time.format.DateTimeFormatter;

public class EmployeeController {
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> departmentColumn;
    @FXML
    private TableColumn<Employee, String> hireDateColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button employeesBtn;
    @FXML
    private Button departmentsBtn;
    @FXML
    private Button leavesBtn;

    private EmployeeService employeeService;
    private DepartmentService departmentService;
    private ObservableList<Employee> employeeList;

    @FXML
    public void initialize() {
        employeeService = new EmployeeService();
        departmentService = new DepartmentService();
        employeeList = FXCollections.observableArrayList();
        setupTable();
        loadEmployees();
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        departmentColumn.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            if (emp.getDepartmentId() != null) {
                var dept = departmentService.findById(emp.getDepartmentId());
                return javafx.beans.binding.Bindings.createStringBinding(
                    () -> dept != null ? dept.getName() : "N/A"
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        hireDateColumn.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            if (emp.getHireDate() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                    () -> emp.getHireDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });
        employeeTable.setItems(employeeList);
        
        // Improve table appearance
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeeTable.setStyle("-fx-selection-bar: transparent;");
    }

    private void loadEmployees() {
        employeeList.clear();
        employeeList.addAll(employeeService.findAll());
    }

    @FXML
    private void handleAdd() {
        showEmployeeForm(null);
    }

    @FXML
    private void handleEdit() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showEmployeeForm(selected);
        } else {
            showAlert("Please select an employee to edit");
        }
    }

    @FXML
    private void handleDelete() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Employee");
            alert.setContentText("Are you sure you want to delete " + selected.getName() + "?");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                employeeService.delete(selected.getId());
                loadEmployees();
            }
        } else {
            showAlert("Please select an employee to delete");
        }
    }

    @FXML
    private void handleDashboard() {
        setActiveButton(dashboardBtn);
        loadScene("/views/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void showEmployees() {
        setActiveButton(employeesBtn);
    }

    @FXML
    private void handleDepartments() {
        setActiveButton(departmentsBtn);
        loadScene("/views/department.fxml", "Department Management");
    }

    @FXML
    private void handleLeaves() {
        setActiveButton(leavesBtn);
        loadScene("/views/leave.fxml", "Leave Management");
    }

    private void setActiveButton(Button button) {
        dashboardBtn.getStyleClass().remove("sidebar-button-active");
        employeesBtn.getStyleClass().remove("sidebar-button-active");
        departmentsBtn.getStyleClass().remove("sidebar-button-active");
        leavesBtn.getStyleClass().remove("sidebar-button-active");
        
        button.getStyleClass().add("sidebar-button-active");
    }

    private void showEmployeeForm(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/employee-form.fxml"));
            Parent root = loader.load();
            EmployeeFormController controller = loader.getController();
            controller.setEmployee(employee);
            controller.setEmployeeController(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(employee == null ? "Add Employee" : "Edit Employee");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addButton.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadEmployees();
    }

    private void loadScene(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

