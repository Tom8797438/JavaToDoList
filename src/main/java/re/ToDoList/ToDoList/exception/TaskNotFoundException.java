package re.ToDoList.ToDoList.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(long id) {
        super("Task not found: " + id);
    }
}
