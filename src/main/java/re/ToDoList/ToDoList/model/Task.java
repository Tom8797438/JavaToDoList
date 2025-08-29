package re.ToDoList.ToDoList.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private boolean done;
    //rajout (les 2 lignes doivent être "nullables")
    private Integer priority;
    private Date dueDate;


    // Constructeur par défaut requis par JPA
    public Task() {}
    // Constructeur
    public Task(Long id, String description) {
        this.id = id;
        this.description = description;
        this.done = false;
        this.priority = null;
        this.dueDate = null; 
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    // rajout
    public Integer getPriority() {return priority; }
    public void setPriority (Integer priority) { this.priority = priority; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate;}
}
