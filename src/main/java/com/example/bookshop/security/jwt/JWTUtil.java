package com.example.bookshop.security.jwt;

import com.example.bookshop.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTUtil {

    @Autowired
    private JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getJwtSecret().getBytes());
    }

    private String createAccessToken(Claims claims, String username, Instant validity) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public String createAccessToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", resolveRoles(userDetails.getAuthorities()));
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccessTokenExpiredMinutes(), ChronoUnit.MINUTES);
        return createAccessToken(claims, userDetails.getUsername(), validity);
    }

    public String createRefreshToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        Instant validity = Instant.now()
                .plus(jwtProperties.getRefreshTokenExpiredDays(), ChronoUnit.DAYS);
        return createAccessToken(claims, userDetails.getUsername(), validity);
    }

    private List<String> resolveRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }
}
