package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Department;
import services.DepartmentService;

public class DepartmentFormController {
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button saveButton;

    private Department department;
    private DepartmentController departmentController;
    private DepartmentService departmentService;

    @FXML
    public void initialize() {
        departmentService = new DepartmentService();
    }

    public void setDepartment(Department department) {
        this.department = department;
        if (department != null) {
            nameField.setText(department.getName());
            descriptionArea.setText(department.getDescription());
        }
    }

    public void setDepartmentController(DepartmentController controller) {
        this.departmentController = controller;
    }

    @FXML
    private void handleSave() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Please enter department name");
            return;
        }

        if (department == null) {
            department = new Department();
        }

        department.setName(nameField.getText().trim());
        department.setDescription(descriptionArea.getText().trim());

        departmentService.save(department);
        departmentController.refreshTable();
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

