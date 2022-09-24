package com.example.bookshop.dto;

import com.example.bookshop.entity.AbstractEntity;
import com.example.bookshop.security.BookstoreUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRatingDto{
    private String bookId;
    private int rating;
    private long ratingCounter;
}
