package com.example.bookshop.security;

import com.example.bookshop.security.entity.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {


    Optional<BookstoreUser> findBookstoreUserByEmail(String email);

    Optional<BookstoreUser> findBookstoreUserByPhone(String phone);
}
