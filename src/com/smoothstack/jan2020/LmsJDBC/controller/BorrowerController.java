package com.smoothstack.jan2020.LmsJDBC.controller;

import com.smoothstack.jan2020.LmsJDBC.DataAccess.Condition.Condition;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.Condition.Where;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DAOFactory;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.entity.FieldInfoMap;
import com.smoothstack.jan2020.LmsJDBC.model.Borrower;
import com.smoothstack.jan2020.LmsJDBC.mvc.Controller;
import com.smoothstack.jan2020.LmsJDBC.mvc.Mapping;
import com.smoothstack.jan2020.LmsJDBC.mvc.Model;
import com.smoothstack.jan2020.LmsJDBC.mvc.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;

public class BorrowerController implements Controller {

    @Mapping("borrower")
    public String enterCardNumber(Model model, RequestParam requestParam) {
        int cardNumber = (int) requestParam.getOrDefault("cardNumber", 0);

        if (cardNumber > 0) {
            try {

                @SuppressWarnings("unchecked")
                DataAccess<Borrower> borrowerDAO = (DataAccess<Borrower>) DAOFactory.get(Borrower.class);

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

            } catch (NoSuchFieldException | SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
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
        requestParam.clear();

        switch (choice) {
            case 3: return "redirect:home";
        }

        model.put("borrower", borrower);
        model.put("callback", "proceedBorrower");
        return "borrower_menu";
    }
}
