package re.ToDoList.ToDoList.controller;
import re.ToDoList.ToDoList.model.Task;
import re.ToDoList.ToDoList.service.TaskService;
import re.ToDoList.ToDoList.dto.TaskCreateRequest;
import re.ToDoList.ToDoList.dto.TaskPatchRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // Lister toutes les tâches
    @GetMapping
    public List<Task> getAll() {
        return service.getAllTasks();
    }

    // Récupérer une tâche par id
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getOne(@PathVariable Long id) {
        return ResponseEntity.of(service.getTask(id));
  }

    @PostMapping
    public Task create(@Valid @RequestBody TaskCreateRequest body) {
        return service.createTask(body);
    }

    // Modifier une tâche entièrement
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody TaskCreateRequest  body) 
    {
        return service.updateFull(id, body)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
    }

    // Modifier une tache partiellement
    @PatchMapping("/{id}")
    public Task partialUpdate(@PathVariable Long id, @RequestBody TaskPatchRequest body) {
        return service.updatePartial(id, body )
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
    }

    // Supprimer une tâche
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
