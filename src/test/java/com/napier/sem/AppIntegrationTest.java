package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);
    }

    @Test
    void testGetEmployeeByIdValid()
    {
        Employee emp = app.getEmployee(255530);
        assertNotNull(emp);
        assertEquals(255530, emp.emp_no);
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
        assertNotNull(emp.dept);
    }

    @Test
    void testGetEmployeeByIdInvalid()
    {
        Employee emp = app.getEmployee(-1);
        assertNull(emp);
    }

    @Test
    void testGetEmployeeByNameValid()
    {
        Employee emp = app.getEmployee("Ronghao", "Garigliano");
        assertNotNull(emp);
        assertEquals(255530, emp.emp_no);
    }

    @Test
    void testGetEmployeeByNameInvalid()
    {
        Employee emp = app.getEmployee("Nonexistent", "Person");
        assertNull(emp);
    }

    @Test
    void testGetDepartmentValid()
    {
        Department dept = app.getDepartment("Development");
        assertNotNull(dept);
        assertEquals("Development", dept.dept_name);
    }

    @Test
    void testGetDepartmentInvalid()
    {
        Department dept = app.getDepartment("NonexistentDept");
        assertNull(dept);
    }

    @Test
    void testGetSalariesByDepartmentValid()
    {
        Department dept = app.getDepartment("Development");
        ArrayList<Employee> employees = app.getSalariesByDepartment(dept);
        assertNotNull(employees);
        assertTrue(employees.size() > 0);
        for (Employee emp : employees)
        {
            assertEquals(dept.dept_no, emp.dept.dept_no);
        }
    }

    @Test
    void testGetSalariesByDepartmentNullDept()
    {
        ArrayList<Employee> employees = app.getSalariesByDepartment(null);
        assertNull(employees);
    }

    @Test
    void testPrintSalariesEmptyList()
    {
        ArrayList<Employee> emptyList = new ArrayList<>();
        app.printSalaries(emptyList); // Should not throw exception
    }

    @Test
    void testPrintSalariesNullList()
    {
        app.printSalaries(null); // Should print "No employees" without exception
    }

    @Test
    void testDisplayEmployeeValid()
    {
        Employee emp = app.getEmployee(255530);
        app.displayEmployee(emp); // Should display info without exception
    }

    @Test
    void testDisplayEmployeeNull()
    {
        app.displayEmployee(null); // Should print "No employee data to display"
    }
}
