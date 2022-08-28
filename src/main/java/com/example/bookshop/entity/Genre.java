package com.example.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "genre", schema = "book_shop")
public class Genre extends AbstractEntity {

    private static final long serialVersionUID = -6530011050346355790L;

    @Column(name = "name")
    private String name;

    @Column(name = "ord")
    private int order;
}
