package org.example.trainsystem.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();

        // Store basic info in session (avoid calling services here to prevent circular dependencies)
        HttpSession session = request.getSession();
        session.setAttribute("username", username);

        // Determine redirect URL based on user role
        String redirectUrl = determineTargetUrl(authentication);

        response.sendRedirect(redirectUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();

            switch (role) {
                case "ROLE_ADMIN":
                    return "/admin/dashboard";
                case "ROLE_OPMANAGER":
                    return "/opmanager/dashboard";
                case "ROLE_DRIVER":
                    return "/driver/dashboard";
                case "ROLE_ITOFFICER":
                    return "/it/dashboard";
                case "ROLE_PASSENGER":
                    return "/passenger/dashboard";
                default:
                    return "/login?error";
            }
        }

        // Default fallback
        return "/login?error";
    }
}