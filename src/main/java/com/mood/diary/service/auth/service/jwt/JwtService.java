package com.mood.diary.service.auth.service.jwt;

import com.mood.diary.service.user.model.AuthUser;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(AuthUser authUser);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    boolean isTokenValid(String token, UserDetails userDetails);
}
