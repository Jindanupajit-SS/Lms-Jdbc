package com.smoothstack.jan2020.LmsJDBC.template;

import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;
import com.smoothstack.jan2020.LmsJDBC.mvc.Template;
import com.smoothstack.jan2020.LmsJDBC.mvc.View;
import com.smoothstack.jan2020.LmsJDBC.ui.SelectOption;

import java.util.Collections;

public class MainTemplate implements View {

    @Template("display_main_menu")
    public String mainMenu(Model model, RequestParam requestParam) {

        SelectOption<String, Integer> menu = new SelectOption<>();
        menu.setBanner("Welcome to the SmoothStack LMS.\nWhich category of a user are you?\n");
        menu.setAbortMessage(null);
        Collections.addAll(menu.getItems(),
                "Librarian",
                "Administrator",
                "Borrower",
                "Quit" );
        menu.setPrompt("\nSelect > ");
        model.put("callback", "home");
        model.put("menu", menu);

        String callback = (String) model.getOrDefault("callback", "home");

        int choice = menu.get();
        Debug.printf("User select '%d'\n", choice);
        requestParam.put("choice", choice);
        requestParam.put("abort", choice>menu.getItems().size());
        Debug.printf("Callback -> '%s'\n", callback);
        return callback;
    }

    @Template("quit")
    public String quit(Model model, RequestParam requestParam) {
        System.out.println("Good bye.");
        return "exit";
    }
}
