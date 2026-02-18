package org.example.dial.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {
    private final JwtGenerator jwtGenerator;

    public JwtCookieFilter(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
) throws ServletException, IOException {

    try {
        String token = this.readJwtFromReq(request);

        if (token != null && this.jwtGenerator.isValidToken(token)) {

            String userId = this.jwtGenerator.parseId(token);
            if (userId == null) {
                throw new Exception("user id by jwt token is null");
            }
            String role = this.jwtGenerator.parseRole(token);
            if (role == null) {
                throw new Exception("user role by jwt token is null");
            }

            List<GrantedAuthority> auths =
                    List.of(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, auths);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
        System.out.println("JWT expired");
    } catch (Exception e) {
        System.out.println("JWT invalid");
    }

    filterChain.doFilter(request, response);
}

    private String readJwtFromReq(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("satisfy")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}