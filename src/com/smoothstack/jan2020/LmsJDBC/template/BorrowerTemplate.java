package com.smoothstack.jan2020.LmsJDBC.template;

import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.model.Borrower;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;
import com.smoothstack.jan2020.LmsJDBC.mvc.Template;
import com.smoothstack.jan2020.LmsJDBC.mvc.View;
import com.smoothstack.jan2020.LmsJDBC.ui.KeyboardScanner;
import com.smoothstack.jan2020.LmsJDBC.ui.SelectOption;

import java.util.Collections;

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
}
