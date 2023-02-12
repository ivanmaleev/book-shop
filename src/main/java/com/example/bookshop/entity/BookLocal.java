package com.example.bookshop.entity;

import com.example.bookshop.entity.redis.BookRedis;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book", schema = "book_shop")
public class BookLocal extends Book implements Serializable {
    private static final long serialVersionUID = -328587793115176643L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(name = "pub_date")
    private Date pubDate;
    @JsonIgnore
    @ManyToOne
    private Author author = new Author();

    @ManyToOne
    private Genre genre;

    @JsonGetter("authors")
    public String authorsFullName() {
        return author.toString();
    }

    @Column(name = "is_bestseller")
    private Integer isBestseller = 0;
    @Column(name = "slug")
    private String slug;
    @Column(name = "title")
    private String title;
    @Column(name = "image")
    private String image;
    @Transient
    private String status;  // PAID, CART, KEPT

    @Column(name = "description")
    private String description;

    @Column(name = "price_old")
    @JsonProperty("priceOld")
    private Integer priceOld = 0;

    @Column(name = "price")
    @JsonProperty("price")
    private Integer price = 0;

//    public BookLocal(BookRedis bookRedis) {
//        this.id = bookRedis.getId();
//        this.pubDate = bookRedis.getPubDate();
//        this.author = bookRedis.getAuthor();
//        this.isBestseller = bookRedis.getIsBestseller();
//        this.slug = bookRedis.getSlug();
//        this.title = bookRedis.getTitle();
//        this.image = bookRedis.getImage();
//        this.status = bookRedis.getStatus();
//        this.description = bookRedis.getDescription();
//        this.priceOld = bookRedis.getPriceOld();
//        this.price = bookRedis.getPrice();
//    }

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
