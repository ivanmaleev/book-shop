package com.example.bookshop.entity;

import com.example.bookshop.entity.redis.BookRedis;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Базовая сущность книг
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Book implements Serializable {

    private String id;
    private LocalDate pubDate;
    @JsonIgnore
    private Author author = new Author();

    @JsonGetter("authors")
    public String authorsFullName() {
        return author.toString();
    }

    private Integer isBestseller = 0;
    private String slug;
    private String title;
    private String image;
    private String status;  // PAID, CART, KEPT

    private List<BookFile> bookFileList = new ArrayList<>();

    public List<BookFile> getBookFileList() {
        return bookFileList;
    }

    public void setBookFileList(List<BookFile> bookFileList) {
        this.bookFileList = bookFileList;
    }

    private String description;

    @JsonProperty("priceOld")
    private Integer priceOld = 0;

    @JsonProperty("price")
    private Integer price = 0;

    protected Book(BookRedis bookRedis) {
        this.id = bookRedis.getId();
        this.pubDate = bookRedis.getPubDate();
        this.author = bookRedis.getAuthor();
        this.isBestseller = bookRedis.getIsBestseller();
        this.slug = bookRedis.getSlug();
        this.title = bookRedis.getTitle();
        this.image = bookRedis.getImage();
        this.status = bookRedis.getStatus();
        this.description = bookRedis.getDescription();
        this.priceOld = bookRedis.getPriceOld();
        this.price = bookRedis.getPrice();
    }

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
