package com.example.employeemanagesystem.service.impl;

import com.example.employeemanagesystem.util.DaoHelper;
import com.example.employeemanagesystem.entity.Employee;
import com.example.employeemanagesystem.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public List<Employee> list() {
        return DaoHelper.getEmployeeList();
    }

    @Override
    public Employee getById(Integer id) {
        return DaoHelper.getEmployeeById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return DaoHelper.updateEmployee(employee);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return DaoHelper.insertEmployee(employee);
    }

    @Override
    public Employee removeEmployee(Integer id) {
        return DaoHelper.deleteEmployeeById(id);
    }
}
