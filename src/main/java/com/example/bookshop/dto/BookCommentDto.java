package com.example.bookshop.dto;

import com.example.bookshop.entity.AbstractEntity;
import com.example.bookshop.security.BookstoreUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
