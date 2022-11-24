package br.unb.garage_relation.security;

import br.unb.garage_relation.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenHandler jwtTokenHandler;
    private static final Logger LOGGER = Logger.getLogger(JwtTokenFilter.class.getName());

    public JwtTokenFilter(JwtTokenHandler jwtTokenHandler) {
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var token = jwtTokenHandler.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (token.isPresent() && jwtTokenHandler.validateToken(token.get())) {
                var authentication = jwtTokenHandler.getAuthentication(token.get());

                var newContext = SecurityContextHolder.createEmptyContext();
                newContext.setAuthentication(authentication);

                SecurityContextHolder.setContext(newContext);
            }
        } catch (JwtAuthenticationException e) {
            LOGGER.info(e.getMessage());
            var newContext = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(newContext);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
