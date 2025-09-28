package org.example.trainsystem.auth;

import org.example.trainsystem.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserLoginSuccessHandler successHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public pages first
                        .requestMatchers("/", "/login", "/register-passenger", "/registration", "/register", "/css/**", "/js/**", "/images/**").permitAll()

                        // Role-restricted pages next
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/opmanager/**").hasRole("OPMANAGER")
                        .requestMatchers("/driver/**").hasRole("DRIVER")
                        .requestMatchers("/it/**").hasRole("ITOFFICER")
                        .requestMatchers("/passenger/**").hasRole("PASSENGER") //hasRole("PASSENGER") change

                        // Any other request requires login
                        .anyRequest().authenticated()  // change to .authenticated() if you want to restrict all other URLs
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF for now

                .formLogin(form -> form
                        .loginPage("/login") // Your Thymeleaf login.html
                        .loginProcessingUrl("/login") // Spring Security will handle POST to /login
                        .usernameParameter("username") // Match your form field names
                        .passwordParameter("password") // Match your form field names
                        .successHandler(successHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // Set custom user details service
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}