package com.smoothstack.jan2020.LmsJDBC.controller;

import com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils.Condition;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils.Where;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DAOFactory;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.entity.FieldInfoMap;
import com.smoothstack.jan2020.LmsJDBC.model.Author;
import com.smoothstack.jan2020.LmsJDBC.model.Book;
import com.smoothstack.jan2020.LmsJDBC.model.Borrower;
import com.smoothstack.jan2020.LmsJDBC.model.Library;
import com.smoothstack.jan2020.LmsJDBC.mvc.Controller;
import com.smoothstack.jan2020.LmsJDBC.mvc.Mapping;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;
import com.smoothstack.jan2020.LmsJDBC.services.BookService;
import com.smoothstack.jan2020.LmsJDBC.services.LibraryService;
import com.smoothstack.jan2020.LmsJDBC.template.Template;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BorrowerController implements Controller {

    @Mapping("borrower")
    public String enterCardNumber(Model model, RequestParam requestParam) {
        int cardNumber = (int) requestParam.getOrDefault("cardNumber", 0);

        if (cardNumber > 0) {
            try (DataAccess<Borrower> borrowerDAO = (DataAccess<Borrower>) DAOFactory.get(Borrower.class);) {

               @SuppressWarnings("unchecked")
                FieldInfoMap<Borrower> borrowerFieldInfo = (FieldInfoMap<Borrower>) FieldInfoMap.of(Borrower.class);

                Iterator<Borrower> bi = borrowerDAO.read(new Where(Condition.EQUAL, borrowerFieldInfo.getIdSet().iterator().next(), cardNumber )).iterator();

                if (bi.hasNext()) {
                    Borrower borrower = bi.next();

                    requestParam.put("borrower", borrower);
                    Debug.println(borrower.entityDump());
                    return "redirect:proceedBorrower";
                }

                model.put("error", "Invalid card number");

            } catch (NoSuchFieldException | SQLException | IOException e) {
                e.printStackTrace();
            }

        }

        model.put("callback","borrower");
        return "borrower_home";
    }

    @Mapping("proceedBorrower")
    public String proceedBorrower(Model model, RequestParam requestParam) {
        int choice = (int) requestParam.getOrDefault("choice", 0);
        Borrower borrower = (Borrower) requestParam.get("borrower");

        if (borrower == null)
            return "redirect:borrower";

        requestParam.put("borrower", borrower);

        switch (choice) {
            case 1: return "redirect:borrowerChooseLibrary";
            case 3: return "redirect:home";
        }

        model.put("borrower", borrower);
        model.put("callback", "proceedBorrower");
        return "borrower_menu";
    }

    @Mapping("borrowerChooseLibrary")
    public String chooseLibrary(Model model, RequestParam requestParam) {
        Borrower borrower = (Borrower) requestParam.get("borrower");
        Library library = (Library) requestParam.get("library");
        requestParam.clear();

        if (borrower == null)
            return "redirect:borrower";

        requestParam.put("borrower", borrower);
        requestParam.put("library", library);
        if (library != null)
            return "redirect:borrowerChooseBook";

        model.put("libraryList", new LibraryService().getLibraryList());
        model.put("borrower", borrower);
        model.put("callback", "borrowerChooseLibrary");
        return "borrower_choose_library";
    }

    @Mapping("borrowerChooseBook")
    public String chooseBook(Model model, RequestParam requestParam) {
        Borrower borrower = (Borrower) requestParam.get("borrower");
        Library library = (Library) requestParam.get("library");
        Book book = (Book) requestParam.get("book");
        boolean abort = (boolean) requestParam.getOrDefault("abort", false);



        if (borrower == null)
            return "redirect:borrower";

        if (library== null)
            return "redirect:borrowerChooseLibrary";

        if (book != null)
            return "redirect:processBorrowBook";

        requestParam.clear();
        if (abort)
            return "redirect:proceedBorrower";

        BookService bookService = new BookService();
        List<Book> bookList = bookService.getBookListAtLibrary(library, 1);
        Map<Book, List<Author>> authorMap = new HashMap<>();
        bookList.forEach(
                b -> {
                    List<Author> authorList = bookService.getAuthorByBook(b);
                    authorMap.put(b, authorList);
                }
        );


        model.put("borrower", borrower);
        model.put("library", library);
        model.put("book", new Book());
        model.put("bookList", bookList);
        model.put("authorMap", authorMap);
        model.put("callback", "borrowerChooseBook");
        return "borrower_choose_Book";

    }

    @Mapping("processBorrowBook")
    public String processBorrowBook(Model model, RequestParam requestParam) {
        Borrower borrower = (Borrower) requestParam.get("borrower");
        Library library = (Library) requestParam.get("library");
        Book book = (Book) requestParam.get("book");

        BookService bookService = new BookService();

        // TODO: if borrower was checkout the same book, and not turn in yet ?
        if (bookService.checkoutBookFromLibraryByBorrower(book, library, borrower)) {
            model.put("info", "You checked out a book, enjoy!");
        }
        else {
            model.put("error", "Operation was not success");
        }

        model.put("callback","proceedBorrower");
        return "process_borrow_book";
    }
}
