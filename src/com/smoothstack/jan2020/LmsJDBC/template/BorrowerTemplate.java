package com.smoothstack.jan2020.LmsJDBC.template;

import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.model.Author;
import com.smoothstack.jan2020.LmsJDBC.model.Book;
import com.smoothstack.jan2020.LmsJDBC.model.Borrower;
import com.smoothstack.jan2020.LmsJDBC.model.Library;
import com.smoothstack.jan2020.LmsJDBC.mvc.*;
import com.smoothstack.jan2020.LmsJDBC.mvc.Template;
import com.smoothstack.jan2020.LmsJDBC.ui.KeyboardScanner;
import com.smoothstack.jan2020.LmsJDBC.ui.SelectOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BorrowerTemplate implements View {

    @Template("borrower_home")
    public String borrower(Model model, RequestParam requestParam) {
        String callback = (String) model.getOrDefault("callback", "borrower");
        String error = (String) model.get("error");
        model.clear();

        if (error != null) System.err.println(error);

        System.out.println("Welcome to borrower home\nPlease enter your card number\n");
        int cardNumber = KeyboardScanner.getInteger("Card Number > ");

        requestParam.put("cardNumber",cardNumber);
        return callback;
    }

    @Template("borrower_menu")
    public String borrowerMenu(Model model, RequestParam requestParam) {
        String callback = (String) model.getOrDefault("callback", "borrower");
        Borrower borrower = (Borrower) model.get("borrower");
        String error = (String) model.get("error");
        model.clear();

        if (error != null) System.err.println(error);

        SelectOption<String, Integer> menu = new SelectOption<>();
        menu.setBanner(String.format("Welcome %s\n\n", borrower.getName()));
        menu.setAbortMessage("Quit to Main Menu");
        Collections.addAll(menu.getItems(),
                "Check out a book",
                "Return a Book");
        menu.setPrompt("\nSelect > ");
        int choice = menu.get();
        Debug.printf("User select '%d'\n", choice);
        requestParam.put("choice", choice);
        requestParam.put("abort", choice>menu.getItems().size());
        requestParam.put("borrower", borrower);
        Debug.printf("Callback -> '%s'\n", callback);
        return callback;
    }

    @Template("borrower_choose_library")
    public String librarySelector(Model model, RequestParam requestParam) {
        String callback = (String) model.getOrDefault("callback", "borrower");
        Borrower borrower = (Borrower) model.get("borrower");
        @SuppressWarnings("unchecked")
        List<Library> libraryList= (List<Library>) model.getOrDefault("libraryList", new ArrayList<>());
        SelectOption<Library, Library> menu = new SelectOption<>();
        menu.setBanner(String.format("Hi %s,\nPick the branch you want to check out from\n", borrower.getName()));
        menu.setAbortMessage("Go back to borrower menu");

        menu.getItems().addAll(libraryList);

        // Menu should display Library name
        menu.setLabelMapper(library -> { return String.format("%s (%s)", library.getName(), library.getAddress()); });

        // Menu should return Library
        menu.setValueMapper((library,option)->library);

        menu.setPrompt("\nLibrary you want to checkout> ");

        Library library = menu.get();

        if (library != null)
            Debug.printf("User select '%s'\n", library.entityDump());
        else
            Debug.println("User abort!");

        requestParam.put("borrower", borrower);
        requestParam.put("library", library);
        requestParam.put("abort", library==null);
        Debug.printf("Callback -> '%s'\n", callback);
        return callback;

    }

    @Template("borrower_choose_book")
    public String bookSelector(Model model, RequestParam requestParam) {
        String callback = (String) model.getOrDefault("callback", "borrower");
        Borrower borrower = (Borrower) model.get("borrower");
        Library library = (Library) model.get("library");

        @SuppressWarnings("unchecked")
        List<Book> bookList= (List<Book>) model.getOrDefault("bookList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        Map<Book, List<Author>> authorMap =(Map<Book, List<Author>>) model.get("authorMap");

        SelectOption<Book, Book> menu = new SelectOption<>();
        menu.setBanner(String.format("Hi %s,\nPick the book you want to check out\n", borrower.getName()));
        menu.setAbortMessage("Go back to borrower menu");

        menu.getItems().addAll(bookList);

        // Menu should display Library name
        menu.setLabelMapper(b -> { return String.format("%s by %s",b.getTitle(),
                authorMap.get(b).stream().map(Author::getName).collect(Collectors.joining(", "))); } );

        // Menu should return Library
        menu.setValueMapper((book,i)->book);

        menu.setPrompt("\nBook you want to checkout> ");

        Book book = menu.get();

        if (book != null)
            Debug.printf("User select '%s'\n", book.entityDump());
        else
            Debug.println("User abort!");

        requestParam.put("borrower", borrower);
        requestParam.put("library", library);
        requestParam.put("book", book);
        requestParam.put("abort", book==null);
        Debug.printf("Callback -> '%s'\n", callback);
        return callback;

    }

    @Template("process_borrow_book")
    public String processBorrowBook(Model model, RequestParam requestParam) {
        String callback = (String) model.getOrDefault("callback", "borrower");
        Borrower borrower = (Borrower) model.get("borrower");
        Library library = (Library) model.get("library");
        Book book  = (Book) model.get("book");

        String error  = (String) model.get("error");
        String info = (String) model.get("info");

        if (error!=null) System.err.println(error);

        if (info!=null) System.out.println(info);

        return callback;
    }
}
