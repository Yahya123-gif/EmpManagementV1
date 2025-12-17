package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Employee;
import services.EmployeeService;
import services.DepartmentService;

import java.time.LocalDate;

// Contrôleur pour le formulaire d'ajout/modification d'employé
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

    // Initialisation du formulaire au chargement
    @FXML
    public void initialize() {
        employeeService = new EmployeeService();
        departmentService = new DepartmentService();
        hireDatePicker.setValue(LocalDate.now()); // Date par défaut = aujourd'hui
        loadDepartments();
    }

    // Remplit le formulaire avec les données d'un employé existant (pour modification)
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

    // Charge la liste des départements dans le ComboBox
    private void loadDepartments() {
        departmentCombo.getItems().clear();
        departmentService.findAll().forEach(dept -> {
            departmentCombo.getItems().add(dept.getId() + " - " + dept.getName());
        });
    }

    // Sauvegarde les données du formulaire
    @FXML
    private void handleSave() {
        // Vérification que tous les champs sont remplis
        if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() || 
            positionField.getText().trim().isEmpty() || hireDatePicker.getValue() == null) {
            showAlert("Please fill all fields");
            return;
        }

        // Créer un nouvel employé si on est en mode ajout
        if (employee == null) {
            employee = new Employee();
        }

        // Récupérer les valeurs des champs
        employee.setName(nameField.getText().trim());
        employee.setEmail(emailField.getText().trim());
        employee.setPosition(positionField.getText().trim());
        employee.setHireDate(hireDatePicker.getValue());
        
        // Récupérer l'ID du département sélectionné
        if (departmentCombo.getSelectionModel().getSelectedItem() != null) {
            String selected = departmentCombo.getSelectionModel().getSelectedItem();
            employee.setDepartmentId(selected.split(" - ")[0]);
        }

        employeeService.save(employee);
        employeeController.refreshTable();
        closeWindow();
    }

    // Annule et ferme la fenêtre
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    // Affiche un message d'alerte
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

