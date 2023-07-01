package com.example.bookshop.security;

import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.security.jwt.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    public void registerNewUser(RegistrationForm registrationForm) {

        if (Objects.isNull(bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()))) {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            bookstoreUserRepository.save(user);
        }
    }

    public JwtInfo jwtLogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setUsername(userDetails.getUsername());
        jwtInfo.setAccessToken(jwtUtil.createAccessToken(userDetails));
        jwtInfo.setRefreshToken(jwtUtil.createRefreshToken(userDetails));
        return jwtInfo;
    }

    public Object getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal) || Objects.isNull(principal)) {
            return new BookstoreUser();
        }
        BookstoreUserDetails userDetails = (BookstoreUserDetails) principal;
        return userDetails.getBookstoreUser();
    }


}
