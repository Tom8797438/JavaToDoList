package re.ToDoList.ToDoList.repository;
import re.ToDoList.ToDoList.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    // Requête dérivée pour une recherche de sous-chaîne insensible à la casse sur la description.
    List<Task> findByDescriptionContainingIgnoreCase(String q);
}
