package com.example.bookshop.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String jwtSecret;
    private long accessTokenExpiredMinutes;
    private long refreshTokenExpiredDays;

}
