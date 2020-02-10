package com.smoothstack.jan2020.LmsJDBC.controller;

import com.smoothstack.jan2020.LmsJDBC.mvc.Controller;
import com.smoothstack.jan2020.LmsJDBC.mvc.Mapping;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;

public class HomeController implements Controller {

    @Mapping("home")
    public String home(Model model, RequestParam requestParam) {

        int choice = (int) requestParam.getOrDefault("choice", 0);
        requestParam.clear();

        switch(choice) {
            case 1: return "redirect:librarian";
            case 3: return "redirect:borrower";
            case 4: return "quit";
        }

        model.put("callback", "home");

        return "display_main_menu";
    }


}
