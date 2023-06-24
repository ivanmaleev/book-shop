package com.example.bookshop.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CommentRatingRequest {
    @NotNull(message = "Comment id must not be null")
    private Long commentId;
    @Min(value = -1)
    @Max(value = 1)
    private Integer value;
}
