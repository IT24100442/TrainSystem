package org.example.trainsystem.auth;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth


                        // 🔹 Public pages first
                        .requestMatchers("/", "/login", "/register-passenger", "/registration","/register", "/css/**", "/js/**").permitAll()

                        // 🔹 Role-restricted pages next
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/opmanager/**").hasRole("OPMANAGER")
                        .requestMatchers("/driver/**").hasRole("DRIVER")
                        .requestMatchers("/it/**").hasRole("ITOFFICER")
                        .requestMatchers("/passenger/**").hasRole("PASSENGER")

                        // 🔹 Any other request requires login
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // ❗ disable CSRF for now (you can enable later)

                .formLogin(form -> form
                        .loginPage("/login") // your Thymeleaf login.html
                        .successHandler(successHandler)
                        .permitAll()
                )

                .logout(logout -> logout
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

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