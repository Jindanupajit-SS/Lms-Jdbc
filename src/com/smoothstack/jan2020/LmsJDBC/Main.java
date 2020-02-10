package com.smoothstack.jan2020.LmsJDBC;

import com.smoothstack.jan2020.LmsJDBC.ConnectionManager.ConnectionBuilder;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DAOFactory;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.controller.BorrowerController;
import com.smoothstack.jan2020.LmsJDBC.controller.HomeController;
import com.smoothstack.jan2020.LmsJDBC.controller.LibrarianController;
import com.smoothstack.jan2020.LmsJDBC.model.Library;
import com.smoothstack.jan2020.LmsJDBC.mvc.MVCEngine;
import com.smoothstack.jan2020.LmsJDBC.template.BorrowerTemplate;
import com.smoothstack.jan2020.LmsJDBC.template.LibraryTemplate;
import com.smoothstack.jan2020.LmsJDBC.template.MainTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        // Init debug
        Debug.println("Debug mode is on\n");

        // Load properties from file
        loadProperties();

        // Register Controller and View
        try {
            MVCEngine.registerController(HomeController.class);
            MVCEngine.registerController(LibrarianController.class);
            MVCEngine.registerController(BorrowerController.class);

            MVCEngine.registerView(MainTemplate.class);
            MVCEngine.registerView(LibraryTemplate.class);
            MVCEngine.registerView(BorrowerTemplate.class);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        //preloadData();

        // Start the engine
        MVCEngine.start();

    }

    private static void loadProperties() {
        File configFile = new File("config.xml");

        if (!configFile.exists()) {

            File directory = new File(configFile.getParent());
            if (!directory.exists() ||  directory.mkdirs()) {
                System.err.printf("Cannot create directory '%s'\n", directory.getName());
                System.exit(1);
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(configFile)) {

                Properties defaultProperties = new Properties();
                defaultProperties.setProperty("driver","com.mysql.cj.jdbc.Driver");
                defaultProperties.setProperty("url", "jdbc:mysql://localhost:3306/library?useSSL=false");
                defaultProperties.setProperty("username", "root");
                defaultProperties.setProperty("password", "root");
                defaultProperties.storeToXML(fileOutputStream, "LMS Application Properties","UTF-8");
                System.out.printf("Config file '%s' was created with default values.\n", configFile.getName());
                System.out.println("Please edit and re-run the program\n");
                System.exit(0);

            } catch (IOException e) {
                System.err.printf("Cannot create config file '%s'\n", configFile.getName());
                System.exit(1);
            }
        }


        try (FileInputStream fileInputStream = new FileInputStream(configFile)) {

            Properties defaultProperties = new Properties();
            defaultProperties.loadFromXML(fileInputStream);
            ConnectionBuilder.setDefaultProperties(defaultProperties);

        } catch (IOException e) {
            System.err.printf("Cannot load config file '%s'\n", configFile.getName());
            System.exit(1);
        }

    }

    private static void preloadData() {

        try (DataAccess<Library> libraryDAO = (DataAccess<Library>) DAOFactory.get(Library.class);) {

            try {
                libraryDAO.purge();


            libraryDAO.insert(new Library("University Library", "Boston"));
            libraryDAO.insert(new Library("State Library", "New York"));
            libraryDAO.insert(new Library("Federal Library", "Washington D.C."));
            libraryDAO.insert(new Library("County Library", "McLean VA"));

            libraryDAO.getConnection().commit();

            } catch (SQLException | NoSuchFieldException e) {
                e.printStackTrace();
                try {
                    libraryDAO.getConnection().rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
