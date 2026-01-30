package re.ToDoList.ToDoList.dto;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public record TaskResponse(
    long id,
    String description,
    boolean done,
    int priority,
    LocalDate dueDate,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    private static final ZoneId REUNION = ZoneId.of("Indian/Reunion");

    public static OffsetDateTime toReunion(Instant i) {
         return i == null ? null : i.atZone(REUNION).toOffsetDateTime();
    }
} 
