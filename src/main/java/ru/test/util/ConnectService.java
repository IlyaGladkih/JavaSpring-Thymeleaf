package ru.test.util;

import jakarta.servlet.ServletContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class ConnectService {

    Properties properties;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Autowired
    public ConnectService(ServletContext servletContext) {
        InputStream resourceAsStream = servletContext.getResourceAsStream("/WEB-INF/classes/application.properties");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println(System.getProperty("user.dir"));
        properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);

        }

    }

    public Connection getConnection(){
        try {
            return DriverManager.getConnection(
                    properties.getProperty("database"),
                    properties.getProperty("user"),
                    properties.getProperty("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
