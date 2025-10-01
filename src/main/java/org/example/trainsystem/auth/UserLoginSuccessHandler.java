package org.example.trainsystem.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectURL = request.getContextPath(); // default to root if no match

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            switch (role) {
                case "ROLE_ADMIN":
                    redirectURL = "/admin/dashboard";
                    break;
                case "ROLE_PASSENGER":
                    redirectURL = "/passenger/dashboard";
                    break;
                case "ROLE_DRIVER":
                    redirectURL = "/driver/dashboard";
                    break;
                case "ROLE_OPMANAGER":
                    redirectURL = "/opmanager/dashboard";
                    break;
                case "ROLE_ITOFFICER":
                    redirectURL = "/it/dashboard";
                    break;
                case "ROLE_TICKETOFFICER":
                    redirectURL = "/Ticket_Officer/dashboard";
                    break;

                default:
                    redirectURL = "/";
                    break;
            }
        }

        response.sendRedirect(redirectURL);
    }
}
