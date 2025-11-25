package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Employee;
import services.EmployeeService;
import services.DepartmentService;

import java.time.LocalDate;

public class EmployeeFormController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField positionField;
    @FXML
    private ComboBox<String> departmentCombo;
    @FXML
    private DatePicker hireDatePicker;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Employee employee;
    private EmployeeController employeeController;
    private EmployeeService employeeService;
    private DepartmentService departmentService;

    @FXML
    public void initialize() {
        employeeService = new EmployeeService();
        departmentService = new DepartmentService();
        hireDatePicker.setValue(LocalDate.now());
        loadDepartments();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (employee != null) {
            nameField.setText(employee.getName());
            emailField.setText(employee.getEmail());
            positionField.setText(employee.getPosition());
            hireDatePicker.setValue(employee.getHireDate());
            if (employee.getDepartmentId() != null) {
                var dept = departmentService.findById(employee.getDepartmentId());
                if (dept != null) {
                    departmentCombo.setValue(dept.getId() + " - " + dept.getName());
                }
            }
        }
    }

    public void setEmployeeController(EmployeeController controller) {
        this.employeeController = controller;
    }

    private void loadDepartments() {
        departmentCombo.getItems().clear();
        departmentService.findAll().forEach(dept -> {
            departmentCombo.getItems().add(dept.getId() + " - " + dept.getName());
        });
    }

    @FXML
    private void handleSave() {
        if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() || 
            positionField.getText().trim().isEmpty() || hireDatePicker.getValue() == null) {
            showAlert("Please fill all fields");
            return;
        }

        if (employee == null) {
            employee = new Employee();
        }

        employee.setName(nameField.getText().trim());
        employee.setEmail(emailField.getText().trim());
        employee.setPosition(positionField.getText().trim());
        employee.setHireDate(hireDatePicker.getValue());
        
        if (departmentCombo.getSelectionModel().getSelectedItem() != null) {
            String selected = departmentCombo.getSelectionModel().getSelectedItem();
            employee.setDepartmentId(selected.split(" - ")[0]);
        }

        employeeService.save(employee);
        employeeController.refreshTable();
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

