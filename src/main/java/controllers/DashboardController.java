package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.EmployeeService;
import services.DepartmentService;
import services.LeaveService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML
    private Label totalEmployeesLabel;
    @FXML
    private Label totalDepartmentsLabel;
    @FXML
    private Label pendingLeavesLabel;
    @FXML
    private Label approvedLeavesLabel;
    @FXML
    private Label refreshLabel;
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
    private LeaveService leaveService;

    @FXML
    public void initialize() {
        employeeService = new EmployeeService();
        departmentService = new DepartmentService();
        leaveService = new LeaveService();
        setActiveButton(dashboardBtn);
        loadStats();
    }

    private void loadStats() {
        int totalEmployees = employeeService.findAll().size();
        int totalDepartments = departmentService.findAll().size();
        long pendingLeaves = leaveService.findAll().stream()
            .filter(l -> "PENDING".equals(l.getStatus()))
            .count();
        long approvedLeaves = leaveService.findAll().stream()
            .filter(l -> "APPROVED".equals(l.getStatus()))
            .count();

        totalEmployeesLabel.setText(String.valueOf(totalEmployees));
        totalDepartmentsLabel.setText(String.valueOf(totalDepartments));
        pendingLeavesLabel.setText(String.valueOf(pendingLeaves));
        approvedLeavesLabel.setText(String.valueOf(approvedLeaves));
        
        refreshLabel.setText("Last updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @FXML
    private void showDashboard() {
        // Already on dashboard
        setActiveButton(dashboardBtn);
    }

    @FXML
    private void openEmployees() {
        setActiveButton(employeesBtn);
        loadScene("/views/employee.fxml", "Employee Management");
    }

    @FXML
    private void openDepartments() {
        setActiveButton(departmentsBtn);
        loadScene("/views/department.fxml", "Department Management");
    }

    @FXML
    private void openLeaves() {
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

    private void loadScene(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            Stage stage = (Stage) totalEmployeesLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
