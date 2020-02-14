package com.smoothstack.jan2020.LmsJDBC.model;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.persistence.Column;
import com.smoothstack.jan2020.LmsJDBC.persistence.JoinColumn;
import com.smoothstack.jan2020.LmsJDBC.persistence.OneToOne;
import com.smoothstack.jan2020.LmsJDBC.persistence.TableName;

import java.time.LocalDate;

@TableName("tbl_book_loans")
public class Loans implements Entity<Loans> {

    @OneToOne(Book.class)
    @JoinColumn(name = "bookId", referencedColumnName = "bookId")
    private Book book;

    @OneToOne(Library.class)
    @JoinColumn(name = "branchId", referencedColumnName = "branchId")
    private Library library;

    @OneToOne(Borrower.class)
    @JoinColumn(name = "cardNo", referencedColumnName = "cardNo")
    private Borrower borrower;

    @Column(name = "dateOut")
    private LocalDate out;

    @Column(name = "dueDate")
    private LocalDate due;

    @Column(name = "dateIn")
    private LocalDate in;

    public Loans() {
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public LocalDate getOut() {
        return out;
    }

    public void setOut(LocalDate out) {
        this.out = out;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public LocalDate getIn() {
        return in;
    }

    public void setIn(LocalDate in) {
        this.in = in;
    }
}
