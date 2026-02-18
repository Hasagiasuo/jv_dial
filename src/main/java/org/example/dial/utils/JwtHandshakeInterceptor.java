package org.example.dial.utils;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtGenerator jwtGenerator;

    public JwtHandshakeInterceptor(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        String cookies = request.getHeaders().getFirst("cookie");
        if (cookies == null) return false;

        String token = null;
        for (String c : cookies.split(";")) {
            String[] kv = c.trim().split("=");
            if (kv.length == 2 && kv[0].equals("satisfy")) {
                token = kv[1];
            }
        }

        if (token == null || !jwtGenerator.isValidToken(token))
            return false;

        String username = jwtGenerator.parseName(token);
        attributes.put("username", username);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest req,
                               ServerHttpResponse res,
                               WebSocketHandler wsHandler,
                               Exception ex) {}
}
