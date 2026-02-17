package re.ToDoList.ToDoList.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Gestion centralisée d'un accès non authentifié (HTTP 401).
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.info("Unauthorized access to {}: {}", request.getRequestURI(), authException.getMessage());

        // Réponse API claire pour un utilisateur non connecté / session invalide.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication is required");
    }
}
