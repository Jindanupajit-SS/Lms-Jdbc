package com.smoothstack.jan2020.LmsJDBC.DataAccess;

import com.smoothstack.jan2020.LmsJDBC.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DataExtractor {

    private static int[] getCustomIndex(int start, int size, int... customIndex) {
        int[] idx = new int[size];

        for(int i = start,c = 0; c < size; c++, i++) {
            if (c <customIndex.length)
                idx[c] = customIndex[c];
            else
                idx[c] = i;
        }

        return idx;
    }

    public static Book getBook(ResultSet resultSet, int... customIndex) {
        Book book = new Book();
        Publisher publisher = new Publisher();

        int[] index = getCustomIndex(1,3, customIndex);

        try {
            book.setId(resultSet.getInt(index[0]));
            book.setTitle(resultSet.getString(index[1]));
            publisher.setId(resultSet.getInt(index[2]));
            book.setPublisher(publisher);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  book;
    }

    public static Author getAuthor(ResultSet resultSet, int... customIndex) {

        int[] index = getCustomIndex(1,2, customIndex);

        Author author = new Author();
        try {
            author.setId(resultSet.getInt(index[0]));
            author.setName(resultSet.getString(index[1]));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    public static Library getLibrary(ResultSet resultSet, int... customIndex) {

        int[] index = getCustomIndex(1,3, customIndex);

        Library library = new Library();
        try {
            library.setId(resultSet.getInt(index[0]));
            library.setName(resultSet.getString(index[1]));
            library.setAddress(resultSet.getString(index[2]));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return library;
    }

    public static Borrower getBorrower(ResultSet resultSet, int... customIndex) {

        int[] index = getCustomIndex(1,4, customIndex);

        Borrower borrower = new Borrower();
        try {
            borrower.setId(resultSet.getInt(index[0]));
            borrower.setName(resultSet.getString(index[1]));
            borrower.setAddress(resultSet.getString(index[2]));
            borrower.setPhone(resultSet.getString(index[3]));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrower;
    }

    public static int getIntegerAtFirstColumn(ResultSet resultSet, int... customIndex) {
        int[] index = getCustomIndex(1,1, customIndex);
        try {
            return resultSet.getInt(index[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Loans getLoans(ResultSet resultSet, int... customIndex) {
        Loans loans = new Loans();
        Book book = new Book();
        Library library = new Library();
        Borrower borrower = new Borrower();

        int[] index = getCustomIndex(1,5, customIndex);
        Author author = new Author();
        try {
            book.setId(resultSet.getInt(index[0]));


            library.setId(resultSet.getInt(index[1]));


            borrower.setId(resultSet.getInt(index[2]));


            loans.setOut(resultSet.getDate(index[3]).toLocalDate());

            loans.setDue(resultSet.getDate(index[4]).toLocalDate());

            book = getBook(resultSet, 6, 7, 8);
            loans.setBook(book);

            library = getLibrary(resultSet, 9, 10, 11);
            loans.setLibrary(library);

            borrower = getBorrower(resultSet, 12, 13, 14, 15);
            loans.setBorrower(borrower);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loans;
    }
}
