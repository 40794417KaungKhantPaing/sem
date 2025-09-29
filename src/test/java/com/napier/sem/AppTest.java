package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }
    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        app.printSalaries(employess);
    }
    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        employess.add(null);
        app.printSalaries(employess);
    }

    @Test
    void printSalaries()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }
    // ------------------- displayEmployee Tests -------------------
    @Test
    void displayEmployeeNull()
    {
        app.displayEmployee(null); // should handle gracefully
    }

    @Test
    void displayEmployeeMinimal()
    {
        Employee emp = new Employee();
        emp.emp_no = 2;
        emp.first_name = "Alice";
        emp.last_name = "Smith";
        app.displayEmployee(emp);
    }

    @Test
    void displayEmployeeWithManagerAndDepartment()
    {
        // Department
        Department dept = new Department();
        dept.dept_no = "d001";
        dept.dept_name = "Engineering";

        // Manager
        Employee mgr = new Employee();
        mgr.emp_no = 3;
        mgr.first_name = "Bob";
        mgr.last_name = "Manager";
        dept.manager = mgr;

        // Employee with dept and manager
        Employee emp = new Employee();
        emp.emp_no = 4;
        emp.first_name = "Charlie";
        emp.last_name = "Developer";
        emp.title = "Engineer";
        emp.salary = 60000;
        emp.dept = dept;
        emp.manager = mgr;

        app.displayEmployee(emp);
    }

    @Test
    void displayEmployeeWithoutManager()
    {
        Department dept = new Department();
        dept.dept_no = "d002";
        dept.dept_name = "HR";

        Employee emp = new Employee();
        emp.emp_no = 5;
        emp.first_name = "Daisy";
        emp.last_name = "Williams";
        emp.title = "HR Specialist";
        emp.salary = 45000;
        emp.dept = dept;
        emp.manager = null; // no manager

        app.displayEmployee(emp);
    }
}