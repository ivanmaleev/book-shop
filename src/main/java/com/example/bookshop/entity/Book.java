package com.example.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private Date pubDate;
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
    private Integer priceOld = 1;

    @JsonProperty("price")
    private Double price = 0D;

    @JsonProperty("discount")
    public Integer getDiscount() {
        Integer discount = 100 - (int) (price * 100 / priceOld);
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
