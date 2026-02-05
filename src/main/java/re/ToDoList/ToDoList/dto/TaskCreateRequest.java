// règle de validation
package re.ToDoList.ToDoList.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record TaskCreateRequest(
  
  // Required description for task creation.
  @NotBlank(message = "description is required") String description,

  // Initial done status.
  boolean done,

  // Optional priority, must be >= 0 when provided.
  @Min(value = 1, message = "priority must be between 1 and 3")
  @Max(value = 3, message = "priority must be between 1 and 3")
  Integer priority,

  // Optional due date serialized as yyyy-MM-dd.
  @JsonFormat(pattern="yyyy-MM-dd") 
  LocalDate dueDate,
  OffsetDateTime createdAt,
  OffsetDateTime updatedAt
) {}
