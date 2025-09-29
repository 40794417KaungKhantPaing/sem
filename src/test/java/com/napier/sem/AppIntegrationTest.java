package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    void init()
    {
        app = new App();

        String jdbcUrl = "jdbc:mysql://127.0.0.1:33060/employees?useSSL=false";
        app.connect(jdbcUrl, 30000);


        // Connect to the database with 30s timeout
        app.connect(jdbcUrl, 30000);
    }

    @Test
    void testGetEmployee()
    {
        Employee emp = app.getEmployee(255530);
        assertEquals(255530, emp.emp_no);
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
    }
}
