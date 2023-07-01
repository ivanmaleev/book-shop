package com.example.bookshop.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class BookstoreUserDetailsService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = bookstoreUserRepository.findBookstoreUserByEmail(username)
                .map(BookstoreUserDetails::new)
                .orElse(null);
        if (Objects.isNull(userDetails)) {
            userDetails = bookstoreUserRepository.findBookstoreUserByPhone(username)
                    .map(PhoneNumberUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email/phone %s not found", username)));
        }
        return userDetails;
    }
}
