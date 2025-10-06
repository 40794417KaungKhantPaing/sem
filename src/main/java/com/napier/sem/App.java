package com.napier.sem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class App {

    private static Connection con = null;

    // Connect to database
    public static void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(delay);
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    public static void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected from database");
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    // ------------------- ORIGINAL METHODS -------------------

    public Employee getEmployee(int ID) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, t.title, s.salary, d.dept_no, d.dept_name, " +
                            "m.emp_no AS manager_no, m.first_name AS manager_first, m.last_name AS manager_last " +
                            "FROM employees e " +
                            "LEFT JOIN titles t ON e.emp_no = t.emp_no " +
                            "LEFT JOIN salaries s ON e.emp_no = s.emp_no " +
                            "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                            "LEFT JOIN dept_manager dm ON de.dept_no = dm.dept_no " +
                            "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.emp_no = " + ID + " " +
                            "ORDER BY s.to_date DESC LIMIT 1;";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");

                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");

                Employee mgr = null;
                String managerFirst = rset.getString("manager_first");
                String managerLast = rset.getString("manager_last");
                if (managerFirst != null && managerLast != null) {
                    mgr = new Employee();
                    mgr.emp_no = rset.getInt("manager_no");
                    mgr.first_name = managerFirst;
                    mgr.last_name = managerLast;
                }
                dept.manager = mgr;
                emp.manager = mgr;
                emp.dept = dept;

                return emp;
            } else {
                System.out.println("Employee not found");
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public Employee getEmployee(String firstName, String lastName) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, t.title, s.salary, d.dept_no, d.dept_name, " +
                            "m.emp_no AS manager_no, m.first_name AS manager_first, m.last_name AS manager_last " +
                            "FROM employees e " +
                            "LEFT JOIN titles t ON e.emp_no = t.emp_no " +
                            "LEFT JOIN salaries s ON e.emp_no = s.emp_no " +
                            "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                            "LEFT JOIN dept_manager dm ON de.dept_no = dm.dept_no " +
                            "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.first_name = '" + firstName + "' AND e.last_name = '" + lastName + "' " +
                            "ORDER BY s.to_date DESC LIMIT 1;";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");

                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");

                Employee mgr = null;
                String managerFirst = rset.getString("manager_first");
                String managerLast = rset.getString("manager_last");
                if (managerFirst != null && managerLast != null) {
                    mgr = new Employee();
                    mgr.emp_no = rset.getInt("manager_no");
                    mgr.first_name = managerFirst;
                    mgr.last_name = managerLast;
                }
                dept.manager = mgr;
                emp.manager = mgr;
                emp.dept = dept;

                return emp;
            } else {
                System.out.println("Employee not found: " + firstName + " " + lastName);
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public Department getDepartment(String deptName) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT d.dept_no, d.dept_name, m.emp_no AS manager_no, m.first_name AS manager_first, m.last_name AS manager_last " +
                            "FROM departments d " +
                            "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                            "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE d.dept_name = '" + deptName + "';";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");

                Employee mgr = null;
                String managerFirst = rset.getString("manager_first");
                String managerLast = rset.getString("manager_last");
                if (managerFirst != null && managerLast != null) {
                    mgr = new Employee();
                    mgr.emp_no = rset.getInt("manager_no");
                    mgr.first_name = managerFirst;
                    mgr.last_name = managerLast;
                }
                dept.manager = mgr;
                return dept;
            } else {
                System.out.println("Department not found: " + deptName);
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department");
            return null;
        }
    }

    public ArrayList<Employee> getSalariesByRole(String title) {
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary, t.title, d.dept_no, d.dept_name, " +
                            "m.emp_no AS manager_no, m.first_name AS manager_first, m.last_name AS manager_last " +
                            "FROM employees e " +
                            "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                            "JOIN titles t ON e.emp_no = t.emp_no AND t.to_date = '9999-01-01' " +
                            "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                            "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no AND dm.to_date = '9999-01-01' " +
                            "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE t.title = '" + title + "' " +
                            "ORDER BY e.emp_no ASC;";

            ResultSet rset = stmt.executeQuery(strSelect);

            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");
                emp.title = rset.getString("title");

                Department dept = null;
                String dept_no = rset.getString("dept_no");
                String dept_name = rset.getString("dept_name");
                if (dept_no != null && dept_name != null) {
                    dept = new Department();
                    dept.dept_no = dept_no;
                    dept.dept_name = dept_name;

                    String managerFirst = rset.getString("manager_first");
                    String managerLast = rset.getString("manager_last");
                    if (managerFirst != null && managerLast != null) {
                        Employee mgr = new Employee();
                        mgr.emp_no = rset.getInt("manager_no");
                        mgr.first_name = managerFirst;
                        mgr.last_name = managerLast;
                        dept.manager = mgr;
                    }
                }
                emp.dept = dept;
                emp.manager = (dept != null) ? dept.manager : null;

                employees.add(emp);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salaries by role");
        }
        return employees;
    }

    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        if (dept == null) {
            return null;
        }

        ArrayList<Employee> employees = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary, t.title " +
                            "FROM employees e " +
                            "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "JOIN departments d ON de.dept_no = d.dept_no AND d.dept_no = '" + dept.dept_no + "' " +
                            "LEFT JOIN titles t ON e.emp_no = t.emp_no AND t.to_date = '9999-01-01' " +
                            "ORDER BY e.emp_no ASC;";

            ResultSet rset = stmt.executeQuery(strSelect);

            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");
                emp.title = rset.getString("title");

                emp.dept = dept;
                emp.manager = dept.manager;

                employees.add(emp);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salaries by department");
        }
        return employees;
    }

    public void printSalaries(ArrayList<Employee> employees) {
        if (employees == null) {
            System.out.println("No employees");
            return;
        }
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        for (Employee emp : employees) {
            if (emp == null) continue;
            System.out.println(String.format("%-10s %-15s %-20s %-8s",
                    emp.emp_no, emp.first_name, emp.last_name, emp.salary));
        }
    }

    public void displayEmployee(Employee emp) {
        if (emp == null) {
            System.out.println("No employee data to display");
            return;
        }
        System.out.printf(
                "Emp No: %d\nName: %s %s\nTitle: %s\nSalary: %d\nDepartment: %s\nManager: %s\n",
                emp.emp_no,
                emp.first_name,
                emp.last_name,
                emp.title,
                emp.salary,
                (emp.dept != null ? emp.dept.dept_name : "N/A"),
                (emp.manager != null ? emp.manager.first_name + " " + emp.manager.last_name : "N/A")
        );
    }

    public void addEmployee(Employee emp) {
        try {
            Statement stmt = con.createStatement();
            String strUpdate =
                    "INSERT INTO employees (emp_no, first_name, last_name, birth_date, gender, hire_date) " +
                            "VALUES (" + emp.emp_no + ", '" + emp.first_name + "', '" + emp.last_name + "', " +
                            "'9999-01-01', 'M', '9999-01-01')";
            stmt.execute(strUpdate);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to add employee");
        }
    }

    public void outputEmployees(ArrayList<Employee> employees, String filename) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees to output");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("| Emp No | First Name | Last Name | Title | Salary | Department | Manager |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");

        for (Employee emp : employees) {
            if (emp == null) continue;
            sb.append("| " + emp.emp_no + " | " +
                    emp.first_name + " | " +
                    emp.last_name + " | " +
                    (emp.title != null ? emp.title : "N/A") + " | " +
                    emp.salary + " | " +
                    (emp.dept != null ? emp.dept.toString() : "N/A") + " | " +
                    (emp.manager != null ? emp.manager.emp_no : "N/A") + " |\r\n");
        }

        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
            System.out.println("Output written to ./reports/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------- NEW URL ENTRY POINT METHODS -------------------

    @RequestMapping("employee")
    public Employee getEmployeeEndpoint(@RequestParam(value = "id") int ID) {
        return getEmployee(ID);
    }

    @RequestMapping("employeeByName")
    public Employee getEmployeeByNameEndpoint(@RequestParam(value = "first") String firstName,
                                              @RequestParam(value = "last") String lastName) {
        return getEmployee(firstName, lastName);
    }

    @RequestMapping("department")
    public Department getDepartmentEndpoint(@RequestParam(value = "name") String deptName) {
        return getDepartment(deptName);
    }

    @RequestMapping("salariesByRole")
    public ArrayList<Employee> getSalariesByRoleEndpoint(@RequestParam(value = "title") String title) {
        return getSalariesByRole(title);
    }

    @RequestMapping("salariesByDepartment")
    public ArrayList<Employee> getSalariesByDepartmentEndpoint(@RequestParam(value = "name") String deptName) {
        Department dept = getDepartment(deptName);
        return getSalariesByDepartment(dept);
    }

    // ------------------- MAIN -------------------

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        App a = new App();
        if (args.length < 1) {
            a.connect("localhost:33060", 30000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        Department dept = a.getDepartment("Development");
        ArrayList<Employee> employees = a.getSalariesByDepartment(dept);
        a.printSalaries(employees);

        ArrayList<Employee> employees1 = a.getSalariesByRole("Manager");
        a.outputEmployees(employees1, "ManagerSalaries.md");

        a.disconnect();
        System.exit(0);
    }
}
