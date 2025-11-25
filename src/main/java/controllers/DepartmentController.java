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
import model.Department;
import services.DepartmentService;

public class DepartmentController {
    @FXML
    private TableView<Department> departmentTable;
    @FXML
    private TableColumn<Department, String> nameColumn;
    @FXML
    private TableColumn<Department, String> descriptionColumn;
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

    private DepartmentService departmentService;
    private ObservableList<Department> departmentList;

    @FXML
    public void initialize() {
        departmentService = new DepartmentService();
        departmentList = FXCollections.observableArrayList();
        setupTable();
        loadDepartments();
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        departmentTable.setItems(departmentList);
        
        // Improve table appearance
        departmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        departmentTable.setStyle("-fx-selection-bar: transparent;");
    }

    private void loadDepartments() {
        departmentList.clear();
        departmentList.addAll(departmentService.findAll());
    }

    @FXML
    private void handleAdd() {
        showDepartmentForm(null);
    }

    @FXML
    private void handleEdit() {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showDepartmentForm(selected);
        } else {
            showAlert("Please select a department to edit");
        }
    }

    @FXML
    private void handleDelete() {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Department");
            alert.setContentText("Are you sure you want to delete " + selected.getName() + "?");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                departmentService.delete(selected.getId());
                loadDepartments();
            }
        } else {
            showAlert("Please select a department to delete");
        }
    }

    @FXML
    private void handleDashboard() {
        setActiveButton(dashboardBtn);
        loadScene("/views/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void handleEmployees() {
        setActiveButton(employeesBtn);
        loadScene("/views/employee.fxml", "Employee Management");
    }

    @FXML
    private void showDepartments() {
        setActiveButton(departmentsBtn);
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

    private void showDepartmentForm(Department department) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/department-form.fxml"));
            Parent root = loader.load();
            DepartmentFormController controller = loader.getController();
            controller.setDepartment(department);
            controller.setDepartmentController(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(department == null ? "Add Department" : "Edit Department");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addButton.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadDepartments();
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

