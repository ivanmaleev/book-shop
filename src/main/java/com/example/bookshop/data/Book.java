package com.example.bookshop.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pub_date")
    private Date pubDate;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonIgnore
    private Author author;

    @JsonGetter("authors")
    public String authorsFullName() {
        return author.toString();
    }

    @Column(name = "is_bestseller")
    private Integer isBesteller;


    private String slug;

    private String title;

    private String image;

    @OneToMany(mappedBy = "book")
    private List<BookFile> bookFileList = new ArrayList<>();

    public List<BookFile> getBookFileList() {
        return bookFileList;
    }

    public void setBookFileList(List<BookFile> bookFileList) {
        this.bookFileList = bookFileList;
    }

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "priceOld")
    @JsonProperty("priceOld")
    private Integer priceOld;

    @Column(name = "price")
    @JsonProperty("price")
    private Double price;

    @JsonProperty("discount")
    public Integer getDiscount() {
        Integer discount = 100 - (int) (price * 100 / priceOld);
        return discount;
    }


    public Integer getIsBesteller() {
        return isBesteller;
    }

    public void setIsBesteller(Integer isBesteller) {
        this.isBesteller = isBesteller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Integer priceOld) {
        this.priceOld = priceOld;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", priceOld=" + priceOld +
                ", price=" + price +
                '}';
    }
}
