package com.example.bookshop.dto;

import com.example.bookshop.security.entity.BookstoreUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO комментария на книгу
 */
@Data
@NoArgsConstructor
public class BookCommentDto {

    private Long id;
    private String text;
    private BookstoreUser user;
    private LocalDateTime date;
    private Long likes;
    private Long dislikes;

    public BookCommentDto(Long id, String text, BookstoreUser user, LocalDateTime date, Long likes, Long dislikes) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.date = date;
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
