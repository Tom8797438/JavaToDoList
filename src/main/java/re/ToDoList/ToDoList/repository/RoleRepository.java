package re.ToDoList.ToDoList.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import re.ToDoList.ToDoList.model.RoleEnum;
import re.ToDoList.ToDoList.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
