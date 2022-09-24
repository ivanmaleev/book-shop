package com.example.bookshop.security;

import com.example.bookshop.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "book_shop")
public class BookstoreUser extends AbstractEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;

    public boolean isAnonymousUser() {
        return super.getId() == null;
    }
}
