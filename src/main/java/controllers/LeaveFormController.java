package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.LeaveRequest;
import services.LeaveService;
import services.EmployeeService;

import java.time.LocalDate;

public class LeaveFormController {
    @FXML
    private ComboBox<String> employeeCombo;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextArea reasonArea;
    @FXML
    private Button saveButton;

    private LeaveController leaveController;
    private LeaveService leaveService;
    private EmployeeService employeeService;

    @FXML
    public void initialize() {
        leaveService = new LeaveService();
        employeeService = new EmployeeService();
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));
        loadEmployees();
    }

    public void setLeaveController(LeaveController controller) {
        this.leaveController = controller;
    }

    private void loadEmployees() {
        employeeCombo.getItems().clear();
        employeeService.findAll().forEach(emp -> {
            employeeCombo.getItems().add(emp.getId() + " - " + emp.getName());
        });
    }

    @FXML
    private void handleSave() {
        if (employeeCombo.getSelectionModel().getSelectedItem() == null ||
            startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
            reasonArea.getText().trim().isEmpty()) {
            showAlert("Please fill all fields");
            return;
        }

        if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            showAlert("Start date must be before end date");
            return;
        }

        String selected = employeeCombo.getSelectionModel().getSelectedItem();
        String employeeId = selected.split(" - ")[0];

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(employeeId);
        leave.setStartDate(startDatePicker.getValue());
        leave.setEndDate(endDatePicker.getValue());
        leave.setReason(reasonArea.getText().trim());
        leave.setStatus("PENDING");

        leaveService.save(leave);
        leaveController.refreshTable();
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

