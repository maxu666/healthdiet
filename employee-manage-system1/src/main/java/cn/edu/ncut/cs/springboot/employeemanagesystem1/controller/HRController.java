package cn.edu.ncut.cs.springboot.employeemanagesystem1.controller;

import cn.edu.ncut.cs.springboot.employeemanagesystem1.entity.Employee;
import cn.edu.ncut.cs.springboot.employeemanagesystem1.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeMapper.selectList(null);
        model.addAttribute("employees", employees);
        return "hr/employee-list";
    }

    @GetMapping("/employee/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeMapper.selectById(id);
        model.addAttribute("employee", employee);
        return "hr/edit-employee";
    }

    @PostMapping("/employee/edit")
    public String updateEmployee(@ModelAttribute Employee employee, @RequestParam String originalPermission) {
        // 保持原有权限不变
        employee.setPermission(originalPermission);
        employeeMapper.updateById(employee);
        return "redirect:/hr/employees";
    }
} 