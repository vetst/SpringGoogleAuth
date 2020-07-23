package web.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import web.model.Role;
import web.model.User;
import web.util.Util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Component
public class GoogleOauthRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Set<String> roleNames = Util.extractRoleNames(SecurityContextHolder
                .getContext().getAuthentication().getAuthorities());
        response.setStatus(HttpServletResponse.SC_OK);
        if (roleNames.contains("ADMIN") || roleNames.contains("USER")) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("/panel");
        } else {
            response.sendRedirect("/login");
        }
    }
}
