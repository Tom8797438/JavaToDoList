package re.ToDoList.ToDoList.mapper;

import org.springframework.stereotype.Component;
import re.ToDoList.ToDoList.dto.TaskResponse;
import re.ToDoList.ToDoList.model.Task;

import java.time.ZoneId;

@Component
public class TaskMapper {

    private static final ZoneId REUNION = ZoneId.of("Indian/Reunion");

    public TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getDescription(),
                t.isDone(),
                t.getPriority(),
                t.getDueDate(),
                t.getCreatedAt() == null ? null : t.getCreatedAt().atZone(REUNION).toOffsetDateTime(),
                t.getUpdatedAt() == null ? null : t.getUpdatedAt().atZone(REUNION).toOffsetDateTime()
        );
    }
}
