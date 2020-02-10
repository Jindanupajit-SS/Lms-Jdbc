package com.smoothstack.jan2020.LmsJDBC.model;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.persistence.Column;
import com.smoothstack.jan2020.LmsJDBC.persistence.Id;
import com.smoothstack.jan2020.LmsJDBC.persistence.TableName;

@TableName("tbl_genre")
public class Genre implements Entity<Genre> {

    @Id
    @Column(name = "genre_id")
    private Integer id;

    @Column(name = "genre_name")
    private String name;
}
