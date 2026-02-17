package re.ToDoList.ToDoList.service.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppAuthProvider extends DaoAuthenticationProvider {

    public AppAuthProvider(UserService userService, PasswordEncoder passwordEncoder) {
        // super(userService) évite l'appel à la méthode dépréciée setUserDetailsService(...).
        super(userService);
        setPasswordEncoder(passwordEncoder);
    }
}
