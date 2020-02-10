package com.smoothstack.jan2020.LmsJDBC.model;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.persistence.*;

@Table("tbl_book")
public class Book implements Entity<Book> {

    @Id
    @Column(name="bookId")
    private Integer id;

    @Column(name="title")
    private String title;

    @OneToOne
    @JoinColumn(name = "pubId", referencedColumnName = "publisherId")
    private Publisher publisher;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
