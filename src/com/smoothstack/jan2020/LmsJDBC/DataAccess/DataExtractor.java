package com.smoothstack.jan2020.LmsJDBC.DataAccess;

import com.smoothstack.jan2020.LmsJDBC.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public abstract class DataExtractor {

    public static Book Book(ResultSet resultSet, int... index) {
        Book book = new Book();
        Publisher publisher = new Publisher();

        try {
            book.setId(resultSet.getInt(1));
            book.setTitle(resultSet.getString(2));
            publisher.setId(resultSet.getInt(3));
            book.setPublisher(publisher);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  book;
    }

    public static Author Author(ResultSet resultSet,int... index) {

        Author author = new Author();
        try {
            author.setId(resultSet.getInt(1));
            author.setName(resultSet.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    public static int getIntegerAtFirstColumn(ResultSet resultSet) {
        try {
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Loans Loans(ResultSet resultSet,int... index) {
        Loans loans = new Loans();
        Book book = new Book();
        Library library = new Library();
        Borrower borrower = new Borrower();


        Author author = new Author();
        try {
            book.setId(resultSet.getInt(1));
            loans.setBook(book);

            library.setId(resultSet.getInt(2));
            loans.setLibrary(library);

            borrower.setId(resultSet.getInt(3));
            loans.setBorrower(borrower);

            loans.setOut(resultSet.getDate(4).toLocalDate());

            loans.setDue(resultSet.getDate(5).toLocalDate());

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loans;
    }
}
