package com.example.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("book")
public class BookRedis {
    @Id
    private String id;
    private Date pubDate;
    private Author author;
    private Integer isBestseller = 0;
    private String slug;
    private String title;
    private String image;
    private String status;
    private String description;
    private Integer priceOld = 0;
    private Integer price = 0;

    public BookRedis(Book book) {
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
}
