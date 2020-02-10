package com.smoothstack.jan2020.LmsJDBC.services;

import com.smoothstack.jan2020.LmsJDBC.ConnectionManager.ConnectionBuilder;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataExtractor;
import com.smoothstack.jan2020.LmsJDBC.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookService implements Service {

    public List<Book> getBookListAtLibrary(Library library, int minimumNoOfCopies) {

        try (Connection connection = new ConnectionBuilder().getConnection() ) {
            return DataAccess.executeQuery(connection,
                    "SELECT b.bookId, b.title, b.pubId FROM tbl_book b JOIN tbl_book_copies c on b.bookId = c.bookId " +
                            "JOIN tbl_library_branch l on l.branchId = c.branchId " +
                            "WHERE c.branchId = ? AND c.noOfCopies >= ? "
                    , DataExtractor::Book
                    , library.getId(), minimumNoOfCopies);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Author> getAuthorByBook(Book book) {
        try (Connection connection = new ConnectionBuilder().getConnection() ) {
            return DataAccess.executeQuery(connection,
                    "SELECT a.authorId, a.authorName FROM tbl_book b JOIN tbl_book_authors ba on b.bookId = ba.bookId " +
                            "JOIN tbl_author a on ba.authorId = a.authorId " +
                            "WHERE b.bookId = ? "
                    , DataExtractor::Author
                    , book.getId());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int getNumberOfCopies(Book book, Library library) {
        try (Connection connection = new ConnectionBuilder().getConnection() ) {

                List<Integer> noOfCopies = DataAccess.executeQuery(connection,
                    "SELECT c.noOfCopies FROM tbl_book b JOIN tbl_book_copies c on b.bookId = c.bookId " +
                            "JOIN tbl_library_branch l on l.branchId = c.branchId " +
                            "WHERE c.bookId = ? AND c.branchId >= ? "
                    , DataExtractor::getIntegerAtFirstColumn
                    , book.getId(), library.getId());

                if (noOfCopies.size() == 1) {
                    return noOfCopies.get(0);
                } else if (noOfCopies.size() == 0) {
                    return 0;
                }
                else {
                    throw new SQLException("Single result expected "+noOfCopies.size()+" records return");
                }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean checkoutBookFromLibraryByBorrower(Book book, Library library, Borrower borrower) {
        int noOfCoipes = getNumberOfCopies(book, library);

        if (noOfCoipes < 1)
            return false;

        boolean success = false;
        Connection connection = null;


        try  {
            connection = new ConnectionBuilder().getConnection();
            DataAccess.executeUpdate(connection,
                    "UPDATE tbl_book_copies SET noOfCopies = noOfCopies - 1 " +
                            "WHERE noOfCopies > 0 AND bookId = ? AND branchId = ? "
                    , book.getId() , library.getId()
                    );

            LocalDate dateOut = LocalDate.now();
            LocalDate dueDate = dateOut.plusDays(7);

            Date jDateOut = Date.valueOf(dateOut);
            Date jDueDate = Date.valueOf(dueDate);

            DataAccess.executeUpdate(connection,
                    "INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) " +
                            "VALUES  (?,?,?,?,?)"
                    ,book.getId() ,library.getId(), borrower.getId(), jDateOut, jDueDate);

            connection.commit();

            success = true;

        } catch (SQLException | ClassNotFoundException e) {
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();

        } finally  {
            try {
                if (connection != null)
                        connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return success;
    }

    public List<Loans> getBookListBorrowedByBorrower(Borrower borrower) {
        try (Connection connection = new ConnectionBuilder().getConnection() ) {
            List<Loans> lonasList = DataAccess.executeQuery(connection,
                    "SELECT bl.bookId, bl.branchId, bl.cardNo, bl.dateOut, bl.dueDate " +
                            "" +
                            "FROM tbl_book b JOIN tbl_book_loans bl on b.bookId = bl.bookId " +
                            "JOIN tbl_library_branch l on l.branchId = bl.branchId " +
                            "JOIN tbl_borrower r on r.cardNo = bl.cardNo " +
                            "WHERE dateIn is null AND bl.cardNo = ? "
                    , DataExtractor::Loans
                    , borrower.getId());



            return lonasList;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
