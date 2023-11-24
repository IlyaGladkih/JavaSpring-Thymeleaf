package ru.test.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.test.util.ConnectService;

public class TestConnect {

    @Test
    public void testConnection(){
        Assertions.assertNull(ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties"));
    }
}
