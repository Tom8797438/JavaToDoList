package re.ToDoList.ToDoList.dto;

import jakarta.validation.constraints.PositiveOrZero;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record TaskPatchRequest(
  String description,
  Boolean done,
  @PositiveOrZero Integer priority,
  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dueDate
) {}
