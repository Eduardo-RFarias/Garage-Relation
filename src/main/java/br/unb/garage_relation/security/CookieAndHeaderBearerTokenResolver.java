package br.unb.garage_relation.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import static br.unb.garage_relation.Constants.ACCESS_TOKEN_COOKIE_NAME;
import static br.unb.garage_relation.Constants.AUTHORIZATION_HEADER;

public class CookieAndHeaderBearerTokenResolver implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        String authorizationToken = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationToken != null) {
            if (authorizationToken.startsWith("Bearer ")) {
                return authorizationToken.substring("Bearer ".length());
            }
        }

        var cookies = request.getCookies();

        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals(ACCESS_TOKEN_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
