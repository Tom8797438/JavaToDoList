package re.ToDoList.ToDoList.service;

import org.springframework.stereotype.Service;

import re.ToDoList.ToDoList.dto.TaskCreateRequest;
import re.ToDoList.ToDoList.dto.TaskPatchRequest;
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

    public Task createTask(TaskCreateRequest req) {
        Task t = new Task();
        t.setDescription(req.description().trim());
        t.setDone(req.done());
        t.setPriority(req.priority());
        t.setDueDate(req.dueDate());
        return repo.save(t);
    }

    public Optional<Task> updateFull(Long id, TaskCreateRequest req) {
        return repo.findById(id).map(t -> {
            t.setDescription(req.description().trim());
            t.setDone(req.done());
            t.setPriority(req.priority());
            t.setDueDate(req.dueDate());  
            return repo.save(t);
        });
        }

    public Optional<Task> updatePartial(Long id, TaskPatchRequest req) {
        return repo.findById(id).map(t -> {
        if (req.description() != null && !req.description().isBlank()) {
            t.setDescription(req.description().trim());
        }
        if (req.done() != null) {
            t.setDone(req.done());
        }
        if (req.priority() != null) {
            t.setPriority(req.priority());
        }
        if (req.dueDate() != null) {
            t.setDueDate(req.dueDate());
        }
        return repo.save(t);
        });
    }

    public void deleteTask(Long id) {
        repo.deleteById(id);
    }
}
