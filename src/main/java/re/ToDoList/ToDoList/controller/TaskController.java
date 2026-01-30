package re.ToDoList.ToDoList.controller;

import re.ToDoList.ToDoList.model.Task;
import re.ToDoList.ToDoList.service.TaskService;
import re.ToDoList.ToDoList.dto.TaskUpsterRequest;
import re.ToDoList.ToDoList.dto.TaskPatchRequest;
import re.ToDoList.ToDoList.mapper.TaskMapper;

// import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import re.ToDoList.ToDoList.dto.TaskResponse;

// import java.time.LocalDate;
// import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    // Orchestrates HTTP requests for Task resources.
    private final TaskService service;
    private final TaskMapper mapper;

    public TaskController(TaskService service, TaskMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // Lister toutes les tâches
    @GetMapping
    public List<TaskResponse> getAll() {
        return service.getAllTasks().stream().map(mapper::toResponse).toList();
    }

    // Récupérer une tâche par id
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getOne(
        @PathVariable long id
    ) {
        return service.getTask(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    // Récupérer une tâche par description
    @GetMapping("/search-description")
    public List<Task> searchByDescription(@RequestParam String q) {
        return service.searchByDescription(q);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskUpsterRequest req) {
        Task created = service.createTask(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }
        // Build Location header for the created resource.
    //     URI location = ServletUriComponentsBuilder
    //             .fromCurrentRequest().path("/{id}")
    //             .buildAndExpand(createdTask.getId()).toUri();

    //     return ResponseEntity.created(location).body(createdTask);
    // }

    // Modifier une tâche entièrement
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> put(
        @PathVariable long id,
        @Valid @RequestBody TaskUpsterRequest req
    ) {
        return service.updateFullTask(id, req)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Modifier une tache partiellement
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> patch(
        @PathVariable long id, 
        @Valid @RequestBody TaskPatchRequest req
    ) {
        return service.updatePartial(id, req)
                .map(mapper::toResponse)
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
    public Page<TaskResponse> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // Délègue le filtrage et la pagination à la couche de service
        return service.search(q, done, priority, dueAfter, dueBefore, page, size)
                .map(mapper::toResponse);
    }
}
