package com.smoothstack.jan2020.LmsJDBC.controller;

import com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils.Condition;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils.Where;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DAOFactory;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.entity.FieldInfoMap;
import com.smoothstack.jan2020.LmsJDBC.model.Borrower;
import com.smoothstack.jan2020.LmsJDBC.model.Library;
import com.smoothstack.jan2020.LmsJDBC.mvc.Controller;
import com.smoothstack.jan2020.LmsJDBC.mvc.Mapping;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;
import com.smoothstack.jan2020.LmsJDBC.services.LibraryService;
import com.smoothstack.jan2020.LmsJDBC.template.Template;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;

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

        if (library != null)
            return "redirect:borrowerChooseBook";

        model.put("libraryList", new LibraryService().getLibraryList());
        model.put("borrower", borrower);
        model.put("callback", "borrowerChooseLibrary");
        return "borrower_choose_library";
    }
}
