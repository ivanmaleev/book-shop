package com.example.bookshop.entity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Сущность книги из Google Api
 */
@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "true")
public class BookGoogleApi extends Book {
}
