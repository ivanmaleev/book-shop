package com.example.bookshop.entity;

import com.example.bookshop.entity.redis.BookRedis;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConditionalOnProperty(value="google.books.api.enable", havingValue = "true")
public class BookGoogleApi extends Book implements Serializable {
    private static final long serialVersionUID = -328587793115176643L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer priceOld = 0;

    @JsonProperty("price")
    private Integer price = 0;

    public BookGoogleApi(BookRedis bookRedis) {
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
