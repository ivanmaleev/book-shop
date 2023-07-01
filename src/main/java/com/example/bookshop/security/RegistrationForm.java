package com.example.bookshop.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {

    private String name;
    private String email;
    private String phone;
    private String password;
}
