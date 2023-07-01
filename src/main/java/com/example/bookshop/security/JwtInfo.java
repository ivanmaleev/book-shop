package com.example.bookshop.security;

import lombok.Data;

@Data
public class JwtInfo {
    private String username;
    private String accessToken;
    private String refreshToken;

}
