package re.ToDoList.ToDoList.model;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {
    // Primary key managed by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Task text provided by the user.
    private String description;
    // Completion flag.
    private boolean done;
    // Optional priority (nullable).
    private Integer priority;
    // Optional due date (nullable).
    private LocalDate dueDate;


    // Default constructor required by JPA.
    public Task() {}
    // Convenience constructor for basic creation.
    public Task(Long id, String description) {
        this.id = id;
        this.description = description;
        this.done = false;
        this.priority = null;
        this.dueDate = null; 
    }

    // Getters and setters.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    // Optional priority accessor.
    public Integer getPriority() {return priority; }
    public void setPriority (Integer priority) { this.priority = priority; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate;}
}
