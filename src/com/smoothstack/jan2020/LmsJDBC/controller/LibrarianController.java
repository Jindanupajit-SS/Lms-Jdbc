package com.smoothstack.jan2020.LmsJDBC.controller;

import com.smoothstack.jan2020.LmsJDBC.DataAccess.DAOFactory;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.model.Library;
import com.smoothstack.jan2020.LmsJDBC.mvc.Controller;
import com.smoothstack.jan2020.LmsJDBC.mvc.Mapping;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;
import com.smoothstack.jan2020.LmsJDBC.services.LibraryService;

import java.sql.SQLException;
import java.util.List;

public class LibrarianController implements Controller {

    @Mapping("librarian")
    public String librarian(Model model, RequestParam requestParam) {
        int choice = (int) requestParam.getOrDefault("choice", 0);
        requestParam.clear();
        switch(choice) {
            case 1: return "redirect:librarySelector";
        }
        if (choice > 0)
            return "redirect:home";

        model.put("callback", "librarian");

        return "library_home";
    }

    @Mapping("librarySelector")
    public String locationChoice(Model model, RequestParam requestParam) {

        Library library = (Library) requestParam.getOrDefault("library", null);
       // int id = (int) requestParam.getOrDefault("id", 0);
        boolean abort = (boolean) requestParam.getOrDefault("abort", false);
        requestParam.clear();

        if (abort)
            return "redirect:home";

        if (library != null) {
            requestParam.put("library", library);
            return "redirect:manageLibrary";
        }

        LibraryService libraryService = new LibraryService();

        model.put("libraryList", libraryService.getLibraryList());

        model.put("callback", "librarySelector");
        return "library_selector";
    }

    @Mapping("manageLibrary")
    public String manageLibrary(Model model, RequestParam requestParam) {
        Library library = (Library) requestParam.getOrDefault("library", null);
        int choice = (int) requestParam.getOrDefault("choice", 0);
        boolean abort = (boolean) requestParam.getOrDefault("abort", false);
        requestParam.clear();

        if (abort)
            return "redirect:librarySelector";

        if (library == null)
            return "redirect:librarySelector";

        if (choice == 1) {
            requestParam.put("library", library);
            return "redirect:libraryInput";
        } else
            Debug.printf("Choose %d\n",choice);


        model.put("library", library);


        model.put("callback", "manageLibrary");
        return "manage_library";
    }

    @Mapping("libraryInput")
    public String libraryInput(Model model, RequestParam requestParam) {
        Library library = (Library) requestParam.getOrDefault("library", null);
        requestParam.clear();

        model.put("library", library);

        model.put("callback", "libraryInputProcess");

        return "library_input";
    }

    @Mapping("libraryInputProcess")
    public String libraryInputProcess(Model model, RequestParam requestParam) {
        Library library = (Library) requestParam.getOrDefault("library", null);
        requestParam.clear();
        try {

            @SuppressWarnings("unchecked")
            DataAccess<Library> libraryDAO = (DataAccess<Library>) DAOFactory.get(Library.class);

            if (library.getId() > 0 )
                libraryDAO.update(library);
            else
                libraryDAO.insert(library);

            libraryDAO.getConnection().commit();

        } catch (NoSuchFieldException | SQLException e) {
            e.printStackTrace();
        }

        // TODO: message
        model.put("info", "Library info updated");
        model.put("callback", "manageLibrary");

        return "redirect:manageLibrary";
    }
}
