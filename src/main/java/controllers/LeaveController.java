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
import model.LeaveRequest;
import services.LeaveService;

import java.time.format.DateTimeFormatter;

public class LeaveController {
    @FXML
    private TableView<LeaveRequest> leaveTable;
    @FXML
    private TableColumn<LeaveRequest, String> employeeColumn;
    @FXML
    private TableColumn<LeaveRequest, String> startDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> endDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> reasonColumn;
    @FXML
    private TableColumn<LeaveRequest, String> statusColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button refuseButton;
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

    private LeaveService leaveService;
    private ObservableList<LeaveRequest> leaveList;

    @FXML
    public void initialize() {
        leaveService = new LeaveService();
        leaveList = FXCollections.observableArrayList();
        setupTable();
        loadLeaves();
    }

    private void setupTable() {
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        startDateColumn.setCellValueFactory(cellData -> {
            LeaveRequest leave = cellData.getValue();
            if (leave.getStartDate() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                    () -> leave.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });
        endDateColumn.setCellValueFactory(cellData -> {
            LeaveRequest leave = cellData.getValue();
            if (leave.getEndDate() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                    () -> leave.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        leaveTable.setItems(leaveList);
        
        // Improve table appearance
        leaveTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        leaveTable.setStyle("-fx-selection-bar: transparent;");
        
        // Add status cell factory for better visual representation
        statusColumn.setCellFactory(column -> new javafx.scene.control.TableCell<LeaveRequest, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "APPROVED":
                            setStyle("-fx-text-fill: #11998e; -fx-font-weight: bold; -fx-background-color: rgba(17,153,142,0.1); -fx-background-radius: 8px; -fx-padding: 4px 12px;");
                            break;
                        case "REJECTED":
                            setStyle("-fx-text-fill: #eb3349; -fx-font-weight: bold; -fx-background-color: rgba(235,51,73,0.1); -fx-background-radius: 8px; -fx-padding: 4px 12px;");
                            break;
                        case "PENDING":
                            setStyle("-fx-text-fill: #f5576c; -fx-font-weight: bold; -fx-background-color: rgba(245,87,108,0.1); -fx-background-radius: 8px; -fx-padding: 4px 12px;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    private void loadLeaves() {
        leaveList.clear();
        leaveList.addAll(leaveService.findAll());
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/leave-form.fxml"));
            Parent root = loader.load();
            LeaveFormController controller = loader.getController();
            controller.setLeaveController(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Leave Request");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addButton.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAccept() {
        LeaveRequest selected = leaveTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            leaveService.updateStatus(selected.getId(), "APPROVED");
            loadLeaves();
        } else {
            showAlert("Please select a leave request to accept");
        }
    }

    @FXML
    private void handleRefuse() {
        LeaveRequest selected = leaveTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            leaveService.updateStatus(selected.getId(), "REJECTED");
            loadLeaves();
        } else {
            showAlert("Please select a leave request to refuse");
        }
    }

    @FXML
    private void handleDelete() {
        LeaveRequest selected = leaveTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Leave Request");
            alert.setContentText("Are you sure you want to delete this leave request?");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                leaveService.delete(selected.getId());
                loadLeaves();
            }
        } else {
            showAlert("Please select a leave request to delete");
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
    private void handleDepartments() {
        setActiveButton(departmentsBtn);
        loadScene("/views/department.fxml", "Department Management");
    }

    @FXML
    private void showLeaves() {
        setActiveButton(leavesBtn);
    }

    private void setActiveButton(Button button) {
        dashboardBtn.getStyleClass().remove("sidebar-button-active");
        employeesBtn.getStyleClass().remove("sidebar-button-active");
        departmentsBtn.getStyleClass().remove("sidebar-button-active");
        leavesBtn.getStyleClass().remove("sidebar-button-active");
        
        button.getStyleClass().add("sidebar-button-active");
    }

    public void refreshTable() {
        loadLeaves();
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

