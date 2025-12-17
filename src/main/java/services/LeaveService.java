package services;

import model.LeaveRequest;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Service pour gérer les demandes de congé dans la base de données
public class LeaveService {
    private MongoCollection<Document> collection;
    private EmployeeService employeeService;

    public LeaveService() {
        MongoDatabase db = MongoService.getInstance().getDatabase();
        this.collection = db.getCollection("leave_requests");
        this.employeeService = new EmployeeService();
    }

    // Sauvegarde une demande de congé (ajout ou modification)
    public void save(LeaveRequest leaveRequest) {
        Document doc = new Document();
        if (leaveRequest.getId() != null && !leaveRequest.getId().isEmpty()) {
            doc.append("_id", new ObjectId(leaveRequest.getId()));
        }
        doc.append("employeeId", leaveRequest.getEmployeeId())
           .append("startDate", convertToDate(leaveRequest.getStartDate()))
           .append("endDate", convertToDate(leaveRequest.getEndDate()))
           .append("reason", leaveRequest.getReason())
           .append("status", leaveRequest.getStatus() != null ? leaveRequest.getStatus() : "PENDING");

        // Si c'est nouveau, on insère, sinon on met à jour
        if (leaveRequest.getId() == null || leaveRequest.getId().isEmpty()) {
            collection.insertOne(doc);
            leaveRequest.setId(doc.getObjectId("_id").toString());
        } else {
            collection.replaceOne(new Document("_id", new ObjectId(leaveRequest.getId())), doc);
        }
        
        // Récupérer le nom de l'employé pour l'affichage
        if (leaveRequest.getEmployeeId() != null) {
            var employee = employeeService.findById(leaveRequest.getEmployeeId());
            if (employee != null) {
                leaveRequest.setEmployeeName(employee.getName());
            }
        }
    }

    // Récupère toutes les demandes de congé
    public List<LeaveRequest> findAll() {
        List<LeaveRequest> leaves = new ArrayList<>();
        for (Document doc : collection.find()) {
            LeaveRequest leave = mapToLeaveRequest(doc);
            if (leave.getEmployeeId() != null) {
                var employee = employeeService.findById(leave.getEmployeeId());
                if (employee != null) {
                    leave.setEmployeeName(employee.getName());
                }
            }
            leaves.add(leave);
        }
        return leaves;
    }

    // Trouve une demande de congé par son ID
    public LeaveRequest findById(String id) {
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        if (doc != null) {
            LeaveRequest leave = mapToLeaveRequest(doc);
            if (leave.getEmployeeId() != null) {
                var employee = employeeService.findById(leave.getEmployeeId());
                if (employee != null) {
                    leave.setEmployeeName(employee.getName());
                }
            }
            return leave;
        }
        return null;
    }

    // Supprime une demande de congé
    public void delete(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }

    // Met à jour le statut d'une demande (APPROVED, REJECTED, etc.)
    public void updateStatus(String id, String status) {
        collection.updateOne(
            new Document("_id", new ObjectId(id)),
            new Document("$set", new Document("status", status))
        );
    }

    // Convertit un Document MongoDB en objet LeaveRequest
    private LeaveRequest mapToLeaveRequest(Document doc) {
        LeaveRequest leave = new LeaveRequest();
        leave.setId(doc.getObjectId("_id").toString());
        leave.setEmployeeId(doc.getString("employeeId"));
        Date startDate = doc.getDate("startDate");
        if (startDate != null) {
            leave.setStartDate(convertToLocalDate(startDate));
        }
        Date endDate = doc.getDate("endDate");
        if (endDate != null) {
            leave.setEndDate(convertToLocalDate(endDate));
        }
        leave.setReason(doc.getString("reason"));
        leave.setStatus(doc.getString("status"));
        return leave;
    }

    // Convertit LocalDate en Date pour MongoDB
    private Date convertToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Convertit Date en LocalDate
    private LocalDate convertToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

