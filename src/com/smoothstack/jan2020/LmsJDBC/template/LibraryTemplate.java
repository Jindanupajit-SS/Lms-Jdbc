package com.smoothstack.jan2020.LmsJDBC.template;

import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.model.Library;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;
import com.smoothstack.jan2020.LmsJDBC.mvc.Template;
import com.smoothstack.jan2020.LmsJDBC.mvc.View;
import com.smoothstack.jan2020.LmsJDBC.ui.KeyboardScanner;
import com.smoothstack.jan2020.LmsJDBC.ui.SelectOption;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LibraryTemplate implements View {

    @Template("library_home")
    public String libraryHome(Model model, RequestParam requestParam) {
        String callback = (String) model.getOrDefault("callback", "library");
        SelectOption<String, Integer> menu = new SelectOption<>();
        menu.setBanner("Welcome Librarian\n\n");
        menu.setAbortMessage("Quit to Main Menu");
        Collections.addAll(menu.getItems(),
                "Enter Branch you manage");
        menu.setPrompt("\nSelect > ");
        int choice = menu.get();
        Debug.printf("User select '%d'\n", choice);
        requestParam.put("choice", choice);
        requestParam.put("abort", choice>menu.getItems().size());
        Debug.printf("Callback -> '%s'\n", callback);
        return callback;
    }

    @Template("library_selector")
    public String librarySelector(Model model, RequestParam requestParam) {
        String callback = (String) model.getOrDefault("callback", "library");
        @SuppressWarnings("unchecked")
        List<Library> libraryList= (List<Library>) model.getOrDefault("libraryList", new ArrayList<String>());
        SelectOption<Library, Library> menu = new SelectOption<>();
        menu.setBanner("::: Librarian Menu :::\n\nPlease select library you manage\n");
        menu.setAbortMessage("Go back to main menu");

        menu.getItems().addAll(libraryList);

        // Menu should display Library name
        menu.setLabelMapper(library -> { return String.format("%s (%s)", library.getName(), library.getAddress()); });

        // Menu should return Library
        menu.setValueMapper((library,option)->library);

        menu.setPrompt("\nLibrary > ");

        Library library = menu.get();

        if (library != null)
            Debug.printf("User select '%s'\n", library.entityDump());
        else
            Debug.println("User abort!");

        requestParam.put("library", library);
        requestParam.put("abort", library==null);
        Debug.printf("Callback -> '%s'\n", callback);
        return callback;

    }

    @Template("manage_library")
    public String manageLibrary(Model model, RequestParam requestParam) {
        String info = (String) model.get("info");
        String callback = (String) model.getOrDefault("callback", "library");
        Library library = (Library) model.getOrDefault("library", new Library());
        SelectOption<String, Integer> menu = new SelectOption<>();
        if (info != null) {
            System.out.println(info);
        }

        menu.setAbortMessage("Go back to library selection");

        menu.setBanner(String.format("Manage %s (%s)\n",library.getName(), library.getAddress()));
        menu.getItems().add("Update the details of the library");

        int choice = menu.get();
        Debug.printf("User select '%d'\n", choice);
        requestParam.put("library", library);
        requestParam.put("choice", choice);
        requestParam.put("abort", choice>menu.getItems().size());
        Debug.printf("Callback -> '%s'\n", callback);
        return callback;

    }

    @Template("library_input")
    public String libraryInput(Model model, RequestParam requestParam) {
        Library library = (Library) model.getOrDefault("library", new Library("",""));
        String callback = (String) model.getOrDefault("callback", "home");
        model.clear();

        Objects.requireNonNull(library.getName());
        library.setName(KeyboardScanner.getStringOrDefault("Name > ", library.getName()));
        library.setAddress(KeyboardScanner.getStringOrDefault("Address > ", library.getAddress()));

        requestParam.put("library", library);
        return callback;
    }
}
