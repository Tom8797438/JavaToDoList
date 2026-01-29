// règle de validation
package re.ToDoList.ToDoList.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TaskUpsterRequest(
  
  // Required description for task creation.
  @NotBlank(message = "description is required") String description,

  // Initial done status.
  @NotNull(message = "done is required") boolean done,

  // Optional priority, must be >= 0 when provided.
  @PositiveOrZero(message = "must be >= 0 ") 
  @Min(value = 1, message = "priority must be between 1 and 3")
  @Max(value = 3, message = "priority must be between 1 and 3")
  Integer priority,

  // Optional due date serialized as yyyy-MM-dd.
  @JsonFormat(pattern="yyyy-MM-dd") 
  LocalDate dueDate
) {}
