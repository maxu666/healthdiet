package com.example.employeemanagesystem.service;


import com.example.employeemanagesystem.entity.Employee;

import java.util.List;


public interface EmployeeService {
    List<Employee> list();

    Employee getById(Integer id);

    Employee updateEmployee(Employee employee);

    Employee saveEmployee(Employee employee);

    Employee removeEmployee(Integer id);
}
