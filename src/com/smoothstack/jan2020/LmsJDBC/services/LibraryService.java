package com.smoothstack.jan2020.LmsJDBC.services;


import com.smoothstack.jan2020.LmsJDBC.DataAccess.DAOFactory;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.model.Library;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibraryService implements Service {

    public List<Library> getLibraryList() {

        try (DataAccess<Library> libraryDAO = (DataAccess<Library>) DAOFactory.get(Library.class)) {

            List<Library> libraryList = libraryDAO.read();

            return libraryList;

        } catch (NoSuchFieldException | SQLException | IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
