package re.ToDoList.ToDoList.config;

import re.ToDoList.ToDoList.service.security.AppAuthProvider;
import re.ToDoList.ToDoList.service.security.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        // Pour une API simple, on désactive CSRF pour éviter les rejets sur POST/PUT/DELETE.
        http.csrf(csrf -> csrf.disable());

        // Mode web: gestion du 403 uniquement, la redirection login est gérée par formLogin.
        http.exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler));

        // Enregistre le provider personnalisé dans la chaîne de sécurité.
        http.authenticationProvider(authenticationProvider);

        // Page de login Spring Security par défaut.
        //http.formLogin(Customizer.withDefaults());

        // Page de login Spring Security par défaut ver swagger pour l'exercice.
        http.formLogin(form -> form.defaultSuccessUrl("/swagger-ui/index.html", true));

        // Logout standard.
        http.logout(Customizer.withDefaults());

        // Logout personnalisé pour une API REST, avec invalidation de session et suppression des cookies.
        http.logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

        // Toutes les routes applicatives nécessitent une authentification.
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login", "/logout", "/.well-known/**", "/error").permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        // Construction explicite du provider pour éviter les cycles de beans.
        return new AppAuthProvider(userService, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt est l'encodeur recommandé pour les mots de passe.
        return new BCryptPasswordEncoder();
    }
}
