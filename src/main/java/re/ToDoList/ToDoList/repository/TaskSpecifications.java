package re.ToDoList.ToDoList.repository;

import org.springframework.data.jpa.domain.Specification;
import re.ToDoList.ToDoList.model.Task;

import java.time.LocalDate;

public final class TaskSpecifications {
  // Utility class: static builders for JPA Specifications.
  private TaskSpecifications() {}

  public static Specification<Task> descriptionContains(String q) {
    // Matches description containing the query (case-insensitive).
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("description")), "%" + q.toLowerCase() + "%");
  }

  public static Specification<Task> hasDone(boolean done) {
    // Filters by done status.
    return (root, query, cb) -> cb.equal(root.get("done"), done);
  }

  public static Specification<Task> hasPriority(int p) {
    // Filters by priority value.
    return (root, query, cb) -> cb.equal(root.get("priority"), p);
  }

  public static Specification<Task> dueDateOnOrAfter(LocalDate d) {
    // Filters tasks with dueDate >= given date.
    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dueDate"), d);
  }

  public static Specification<Task> dueDateOnOrBefore(LocalDate d) {
    // Filters tasks with dueDate <= given date.
    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dueDate"), d);
  }
}
