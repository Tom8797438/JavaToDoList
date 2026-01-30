package re.ToDoList.ToDoList.service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import re.ToDoList.ToDoList.dto.TaskUpsterRequest;
import re.ToDoList.ToDoList.dto.TaskPatchRequest;
import re.ToDoList.ToDoList.model.Task;
import re.ToDoList.ToDoList.repository.TaskRepository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import static re.ToDoList.ToDoList.repository.TaskSpecifications.*;

@Service
@Transactional
public class TaskService {
    // Passerelle d’accès aux données pour la persistance des tâches et les requêtes.
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> getAllTasks() {
        // retourne toute les tasks sans filtrage.
        return repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Optional<Task> getTask(long id) {
        // trouver une task par id.
        return repo.findById(id);
    }

    public List<Task> searchByDescription(String q) {
        // Recherche de sous-chaînes insensible à la casse sur la description.
        return repo.findByDescriptionContainingIgnoreCase(q.trim());
    }


    public Task createTask(TaskUpsterRequest req) {
        // Maps / Associe DTO à l’entité avant de sauvegarder.
        Task t = new Task();
        t.setDescription(req.description().trim());
        t.setDone(Boolean.TRUE.equals(req.done()));
        t.setPriority(req.priority());
        t.setDueDate(req.dueDate());

        return repo.save(t);
    }

    public Optional<Task> updateFullTask(long id, TaskUpsterRequest req) {
        // Replaces all updatable fields for an existing task.
        return repo.findById(id).map(t -> {
            t.setDescription(req.description().trim());
            t.setDone(req.done());
            t.setPriority(req.priority());
            t.setDueDate(req.dueDate());  
            return t;
        });
        }

    public Optional<Task> updatePartial(long id, TaskPatchRequest req) {
        // Applies only non-null fields from the PATCH DTO.
        return repo.findById(id).map(t -> {

        if (req.description() != null && !req.description().isBlank()) {
            t.setDescription(req.description().trim());
        }
        if (req.done() ) {
            t.setDone(req.done());
        }
        if (req.priority() != null) {
            t.setPriority(req.priority());
        }
        if (req.dueDate() != null) {
            t.setDueDate(req.dueDate());
        }
        return t;
        });
    }

    public void deleteTask(long id) {
        // Deletes a task by id.
        repo.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Page<Task> search(String q,
                            Boolean done,
                            Integer priority,
                            LocalDate dueAfter,
                            LocalDate dueBefore,
                            int page,
                            int size) {

        // Builds a dynamic JPA Specification based on optional filters.
        Specification<Task> spec = Specification.unrestricted();

        if (q != null && !q.isBlank()) spec = spec.and(descriptionContains(q));
        if (done != null)              spec = spec.and(hasDone(done));
        if (priority != null)          spec = spec.and(hasPriority(priority));
        if (dueAfter != null)          spec = spec.and(dueDateOnOrAfter(dueAfter));
        if (dueBefore != null)         spec = spec.and(dueDateOnOrBefore(dueBefore));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        // Executes a paged, sorted query with the built specification.
        return repo.findAll(spec, pageable);
    }

}
