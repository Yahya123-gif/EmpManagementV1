package services;

import model.Department;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    private MongoCollection<Document> collection;

    public DepartmentService() {
        MongoDatabase db = MongoService.getInstance().getDatabase();
        this.collection = db.getCollection("departments");
    }

    public void save(Department department) {
        Document doc = new Document();
        if (department.getId() != null && !department.getId().isEmpty()) {
            doc.append("_id", new ObjectId(department.getId()));
        }
        doc.append("name", department.getName())
           .append("description", department.getDescription());

        if (department.getId() == null || department.getId().isEmpty()) {
            collection.insertOne(doc);
            department.setId(doc.getObjectId("_id").toString());
        } else {
            collection.replaceOne(new Document("_id", new ObjectId(department.getId())), doc);
        }
    }

    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();
        for (Document doc : collection.find()) {
            departments.add(mapToDepartment(doc));
        }
        return departments;
    }

    public Department findById(String id) {
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        return doc != null ? mapToDepartment(doc) : null;
    }

    public void delete(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }

    private Department mapToDepartment(Document doc) {
        Department dept = new Department();
        dept.setId(doc.getObjectId("_id").toString());
        dept.setName(doc.getString("name"));
        dept.setDescription(doc.getString("description"));
        return dept;
    }
}

