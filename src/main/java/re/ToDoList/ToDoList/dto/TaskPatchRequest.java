package re.ToDoList.ToDoList.dto;
import java.time.LocalDate;
public record TaskPatchRequest(
    // Optional fields; null means "no change".
    String description,
    Boolean done,      
    Integer priority,  
    LocalDate dueDate  
) {}
