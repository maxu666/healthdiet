package com.example.employeemanagesystem.controller;


import com.example.employeemanagesystem.util.Result;
import com.example.employeemanagesystem.entity.Employee;
import com.example.employeemanagesystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public Result<List<Employee>> list() {
        List<Employee> employeeList = employeeService.list();
        Result<List<Employee>> r = new Result<>();
        return r.success("查询所有员工信息成功!", employeeList);
    }

    @PostMapping("employees")
    public Result<Employee> addEmployee(@RequestBody Employee employee) {
        Result<Employee> r = new Result<>();
        Employee e = employeeService.saveEmployee(employee);
        return r.success("新增员工信息成功！", e);
    }
    @GetMapping("/employees/{id}")
    public Result<Employee> getEmployee(@PathVariable("id") Integer id){
        Result<Employee> r = new Result();
        Employee e = employeeService.getById(id);
        return r.success("通过id获取员工信息成功！",e);
    }

    @PutMapping("/employees/{id}")
    public Result<Employee> updateEmployee(@RequestBody Employee employee){
        Result<Employee> r = new Result();
        Employee newEmployee = employeeService.updateEmployee(employee);
        return r.success("修改员工信息成功！",newEmployee);
    }
    @DeleteMapping("/employees/{id}")
    public Result<Employee> deleteEmployee(@PathVariable("id") Integer id){
        Result<Employee> result = new Result();
        Employee employee = employeeService.removeEmployee(id);
        return result.success("根据id删除员工信息成功！",employee);
    }
}
