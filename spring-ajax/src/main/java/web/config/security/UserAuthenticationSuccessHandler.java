package web.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import web.model.User;
import web.util.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Set<String> roleNames = Util.extractRoleNames(SecurityContextHolder
                .getContext().getAuthentication().getAuthorities());
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        if (roleNames.contains("ADMIN") || roleNames.contains("USER")) {
            User user = (User) authentication.getPrincipal();
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("user", user);
            httpServletResponse.sendRedirect("/panel");
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }
}