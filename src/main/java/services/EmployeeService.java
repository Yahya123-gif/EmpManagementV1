package services;

import model.Employee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeService {
    private MongoCollection<Document> collection;

    public EmployeeService() {
        MongoDatabase db = MongoService.getInstance().getDatabase();
        this.collection = db.getCollection("employees");
    }

    public void save(Employee employee) {
        Document doc = new Document();
        if (employee.getId() != null && !employee.getId().isEmpty()) {
            doc.append("_id", new ObjectId(employee.getId()));
        }
        doc.append("name", employee.getName())
           .append("email", employee.getEmail())
           .append("position", employee.getPosition())
           .append("departmentId", employee.getDepartmentId())
           .append("hireDate", convertToDate(employee.getHireDate()));

        if (employee.getId() == null || employee.getId().isEmpty()) {
            collection.insertOne(doc);
            employee.setId(doc.getObjectId("_id").toString());
        } else {
            collection.replaceOne(new Document("_id", new ObjectId(employee.getId())), doc);
        }
    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        for (Document doc : collection.find()) {
            employees.add(mapToEmployee(doc));
        }
        return employees;
    }

    public Employee findById(String id) {
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        return doc != null ? mapToEmployee(doc) : null;
    }

    public void delete(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }

    private Employee mapToEmployee(Document doc) {
        Employee emp = new Employee();
        emp.setId(doc.getObjectId("_id").toString());
        emp.setName(doc.getString("name"));
        emp.setEmail(doc.getString("email"));
        emp.setPosition(doc.getString("position"));
        emp.setDepartmentId(doc.getString("departmentId"));
        Date hireDate = doc.getDate("hireDate");
        if (hireDate != null) {
            emp.setHireDate(convertToLocalDate(hireDate));
        }
        return emp;
    }

    private Date convertToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

