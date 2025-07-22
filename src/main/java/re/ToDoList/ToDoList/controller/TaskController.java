package re.ToDoList.ToDoList.controller;
import re.ToDoList.ToDoList.model.Task;
import re.ToDoList.ToDoList.service.TaskService;
import java.util.List;
//import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    return service.getTask(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    // @GetMapping("/{id}")
    // public Task getOne(@PathVariable Long id) {
    //     return service.getTask(id).orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
    // }

    // Créer une tâche
    @PostMapping
    public Task create(@RequestParam String description, @RequestParam(defaultValue = "false") boolean done) {
        return service.createTask(description, done);
    }

    // Modifier une tâche
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task updaptedTask) 
    {
        return service.updateTask(id, updaptedTask)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
    }

    // Supprimer une tâche
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
