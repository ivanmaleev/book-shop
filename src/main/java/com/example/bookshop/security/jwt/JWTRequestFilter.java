package com.example.bookshop.security.jwt;

import com.example.bookshop.security.BookstoreUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.example.bookshop.security.SecurityConstants.ACCESS_TOKEN;
import static com.example.bookshop.security.SecurityConstants.REFRESH_TOKEN;

@Component
@AllArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (Objects.nonNull(cookies)) {
            Arrays.stream(cookies)
                    .filter(cookie -> Objects.equals(cookie.getName(), ACCESS_TOKEN))
                    .findAny()
                    .ifPresent(cookie -> {
                        String token = cookie.getValue();
                        if (Objects.nonNull(token)) {
                            String username = null;
                            try {
                                username = jwtUtil.extractUsername(token);
                                if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                                    UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(username);
                                    setAuthentication(httpServletRequest, userDetails);
                                }
                            } catch (ExpiredJwtException e) {
                                String refreshToken = getRefreshToken(cookies);
                                if (StringUtils.isNotBlank(refreshToken)) {
                                    username = jwtUtil.extractUsername(refreshToken);
                                    UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(username);
                                    httpServletResponse.addCookie(new Cookie(ACCESS_TOKEN, jwtUtil.createAccessToken(userDetails)));
                                    setAuthentication(httpServletRequest, userDetails);
                                }
                            }
                        }
                    });
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getRefreshToken(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), REFRESH_TOKEN))
                .map(Cookie::getValue)
                .findAny()
                .orElse(null);
    }

    private void setAuthentication(HttpServletRequest httpServletRequest, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
