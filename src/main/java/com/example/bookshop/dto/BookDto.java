package com.example.bookshop.dto;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * DTO книги
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
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

    public BookDto(Book book) {
        this.id = book.getId();
        this.pubDate = book.getPubDate();
        this.author = book.getAuthor();
        this.isBestseller = book.getIsBestseller();
        this.slug = book.getSlug();
        this.title = book.getTitle();
        this.image = book.getImage();
        this.status = book.getStatus();
        this.description = book.getDescription();
        this.priceOld = book.getPriceOld();
        this.price = book.getPrice();
    }

    @JsonProperty("discount")
    public Integer getDiscount() {
        Integer discount = priceOld.equals(0) ? 0 : 100 - (price * 100 / priceOld);
        return discount;
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
