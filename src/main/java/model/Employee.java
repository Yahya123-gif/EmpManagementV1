package model;

import java.time.LocalDate;

// Classe pour représenter un employé dans le système
public class Employee {
    private String id;
    private String name;
    private String email;
    private String position;
    private String departmentId;
    private LocalDate hireDate;

    // Constructeur par défaut
    public Employee() {
    }

    // Constructeur avec tous les paramètres
    public Employee(String id, String name, String email, String position, String departmentId, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.position = position;
        this.departmentId = departmentId;
        this.hireDate = hireDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}

