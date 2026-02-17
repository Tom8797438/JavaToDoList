package re.ToDoList.ToDoList.service.security;

import re.ToDoList.ToDoList.model.Role;
import re.ToDoList.ToDoList.model.RoleEnum;
import re.ToDoList.ToDoList.model.User;
import re.ToDoList.ToDoList.repository.UserRepository;
import re.ToDoList.ToDoList.service.RoleService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Chargement de l'utilisateur depuis la base pour la phase d'authentification.
        return userRepository.findUserWithName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));
    }

    @Transactional
    public User createUser(String username, String rawPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Le username est obligatoire");
        }

        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }

        if (userRepository.findUserWithName(username).isPresent()) {
            throw new IllegalArgumentException("Username déjà utilisé: " + username);
        }

        Role defaultRole = roleService.findByName(RoleEnum.USER)
                .orElseThrow(() -> new IllegalStateException("Le rôle USER n'existe pas en base"));

        User user = new User();
        user.setUsername(username);
        // On stocke toujours un hash BCrypt, jamais le mot de passe brut.
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(defaultRole);

        return userRepository.save(user);
    }
}
