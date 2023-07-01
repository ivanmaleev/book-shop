package com.example.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность книги из Google Api
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "true")
public class BookGoogleApi extends Book implements Serializable {
    private static final long serialVersionUID = -328587793115176643L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private LocalDate pubDate;
    @JsonIgnore
    private Author author = new Author();

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
}
