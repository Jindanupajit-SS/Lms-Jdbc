package com.smoothstack.jan2020.LmsJDBC.model;

import com.smoothstack.jan2020.LmsJDBC.persistence.Column;
import com.smoothstack.jan2020.LmsJDBC.persistence.JoinColumn;
import com.smoothstack.jan2020.LmsJDBC.persistence.OneToOne;
import com.smoothstack.jan2020.LmsJDBC.persistence.TableName;

@TableName("tbl_book_copies")
public class Copies {

    @OneToOne(Book.class)
    @JoinColumn(name = "bookId", referencedColumnName = "bookId")
    private Book book;

    @OneToOne(Library.class)
    @JoinColumn(name = "branchId", referencedColumnName = "branchId")
    private Library library;

    @Column(name = "noOfCopies")
    private Integer noOfCopies;
}
