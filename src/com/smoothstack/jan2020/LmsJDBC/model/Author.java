package com.smoothstack.jan2020.LmsJDBC.model;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.persistence.Column;
import com.smoothstack.jan2020.LmsJDBC.persistence.Id;
import com.smoothstack.jan2020.LmsJDBC.persistence.TableName;

@TableName("tbl_author")
public class Author implements Entity<Author> {

    @Id
    @Column(name="authorId")
    private Integer id;

    @Column(name="authorName")
    private String name;

    public Author() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}