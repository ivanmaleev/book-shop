package com.example.bookshop.security;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContactConfirmationPayload {
    @NotNull(message = "Contact must not be empty")
    private String contact;
    @NotNull(message = "Code must not be empty")
    private String code;
}
