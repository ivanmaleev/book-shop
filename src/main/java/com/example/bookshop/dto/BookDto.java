package com.example.bookshop.dto;

import com.example.bookshop.entity.Author;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO книги
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto implements Serializable {
    private String id;
    private LocalDate pubDate;
    private Author author = new Author();
    private Integer isBestseller = 0;
    private String slug;
    private String title;
    private String image;
    private String status;
    private String description;
    private Integer priceOld = 0;
    private Integer price = 0;
    private Integer rating = 0;

    @JsonProperty("discount")
    public Integer getDiscount() {
        return priceOld.equals(0) ? 0 : 100 - (price * 100 / priceOld);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", priceOld=" + priceOld +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
