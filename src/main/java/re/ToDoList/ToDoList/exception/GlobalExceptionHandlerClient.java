package re.ToDoList.ToDoList.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice // @RestControllerAdvice : composant Spring global, intercepte les exceptions venant des controllers et renvoie du JSON (pas une page HTML).
@Order(1) // + prioritaire que GlobalExceptionHandlerServer.java
public class GlobalExceptionHandlerClient {
    
    public class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(long id) {
            super("Task not found: " + id);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));

        return error(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(),
                Map.of("fieldErrors", fieldErrors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Map<String, Object> details = new LinkedHashMap<>();
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            String fieldPath = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(f -> f != null && !f.isBlank())
                    .collect(Collectors.joining("."));

            details.put("field", fieldPath.isBlank() ? "unknown" : fieldPath);
            details.put("rejectedValue", ife.getValue());
            details.put("expectedType",
                    ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "unknown");
            details.put("reason", "Invalid value type");
        } else {
            details.put("reason", "Malformed JSON or type mismatch");
        }

        return error(HttpStatus.BAD_REQUEST, "Request body is invalid", request.getRequestURI(), details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> violations = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            violations.put(v.getPropertyPath().toString(), v.getMessage());
        }
        return error(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(),
                Map.of("violations", violations));
    }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                        HttpServletRequest request) {
            String expectedType = java.util.Optional.ofNullable(ex.getRequiredType())
                .map(Class::getSimpleName)
            .orElse("unknown");
            Map<String, Object> details = new LinkedHashMap<>();
            details.put("field", ex.getName());
            details.put("rejectedValue", ex.getValue());
            details.put("expectedType", expectedType);

            return error(HttpStatus.BAD_REQUEST, "Invalid parameter type", request.getRequestURI(), details);
        }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Missing required parameter", request.getRequestURI(),
                Map.of("parameter", ex.getParameterName()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "Data integrity violation", request.getRequestURI(),
                Map.of("reason", "Constraint violation"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", request.getRequestURI(),
                Map.of("method", ex.getMethod()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                HttpServletRequest request) {
        org.springframework.http.MediaType contentType = ex.getContentType(); // peut être null

        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Content-Type", request.getRequestURI(),
                Map.of("contentType", contentType != null ? contentType.toString() : "unknown"));
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message, String path, Map<String, Object> details) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details == null ? Map.of() : details
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(TaskNotFoundException ex, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), Map.of());
    }

}
