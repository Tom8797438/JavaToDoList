package re.ToDoList.ToDoList.controller;

import re.ToDoList.ToDoList.model.Task;
import re.ToDoList.ToDoList.service.TaskService;
import re.ToDoList.ToDoList.dto.TaskCreateRequest;
import re.ToDoList.ToDoList.dto.TaskPatchRequest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    // Orchestrates HTTP requests for Task resources.
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
    public ResponseEntity<Task> getOne(
        @PathVariable long id
    ) {
        return service.getTask(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    // Récupérer une tâche par description
    @GetMapping("/search-description")
    public List<Task> searchByDescription(@RequestParam String q) {
        return service.searchByDescription(q);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskCreateRequest req) {
        Task createdTask = service.createTask(req);

        // Build Location header for the created resource.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdTask.getId()).toUri();

        return ResponseEntity.created(location).body(createdTask);
    }

    // Modifier une tâche entièrement
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
        @PathVariable long id,
        @Valid @RequestBody TaskCreateRequest body
    ) {
        return service.updateFullTask(id, body)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }


    // Modifier une tache partiellement
    @PatchMapping("/{id}")
    public ResponseEntity<Task> partialUpdate(
        @PathVariable long id, 
        @Valid @RequestBody TaskPatchRequest body
    ) {
        return service.updatePartial(id, body)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    // Supprimer une tâche
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.deleteTask(id);
    }

    // Rechercher des tâches avec des critères
    @GetMapping("/search")
    public Page<Task> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // Délègue le filtrage et la pagination à la couche de service
        return service.search(q, done, priority, dueAfter, dueBefore, page, size);
    }
}
