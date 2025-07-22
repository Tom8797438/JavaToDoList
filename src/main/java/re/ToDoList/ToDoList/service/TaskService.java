package re.ToDoList.ToDoList.service;

import org.springframework.stereotype.Service;
import re.ToDoList.ToDoList.model.Task;
import re.ToDoList.ToDoList.repository.TaskRepository;

//import static org.mockito.Mockito.description;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    public Optional<Task> getTask(Long id) {
        return repo.findById(id);
    }

    public Task createTask(String description, boolean done) {
        Task task = new Task();
        task.setDescription(description);
        task.setDone(done);
        return repo.save(task);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {
        Optional<Task> existing = repo.findById(id);
        if (existing.isPresent()) {
            Task task = existing.get();
            task.setDescription(updatedTask.getDescription());
            task.setDone(updatedTask.isDone());
            repo.save(task);
            return Optional.of(task);
        } else {
            return Optional.empty();
        }
    }

    public void deleteTask(Long id) {
        repo.deleteById(id);
    }
}
