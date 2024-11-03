package com.omega.software.management.data.auth;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JWTBlacklist {
    private static Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    public void addToBlacklist(String token, Instant expirationTime) {
        blacklist.put(token, expirationTime);
    }

    public boolean isTokenBlacklisted(String token) throws JwtException {
        Instant expirationTime = blacklist.get(token);
        if (expirationTime == null) {
            return false;
        }

        if (Instant.now().isAfter(expirationTime)) {
            blacklist.remove(token);
            return false;
        }

        return true;
    }
    
    public void removeFromBlacklist(String token) {
        blacklist.remove(token);
    }
}
