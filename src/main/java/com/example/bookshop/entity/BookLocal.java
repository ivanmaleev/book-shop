package com.example.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Сущность книги из БД
 */
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

    @Column(name = "pub_date", columnDefinition = "TIMESTAMP")
    private LocalDate pubDate;
    @JsonIgnore
    @ManyToOne
    private Author author = new Author();

    @ManyToOne
    private Genre genre;

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
}
