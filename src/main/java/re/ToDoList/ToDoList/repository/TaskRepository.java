package re.ToDoList.ToDoList.repository;

import re.ToDoList.ToDoList.model.Task;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public List<Task> findAll() {
        return tasks;
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
            tasks.add(task);
        } else {
            // mise à jour
            tasks.removeIf(t -> t.getId().equals(task.getId()));
            tasks.add(task);
        }
        return task;
    }

    public void deleteById(Long id) {
        tasks.removeIf(t -> t.getId().equals(id));
    }
}
