package com.example.bookshop.dto.request;

import lombok.Data;

@Data
public class CommentRatingRequest {
    private Long commentId;
    private Integer value;
}
