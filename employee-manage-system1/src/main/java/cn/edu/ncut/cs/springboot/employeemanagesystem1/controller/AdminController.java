package cn.edu.ncut.cs.springboot.employeemanagesystem1.controller;

import cn.edu.ncut.cs.springboot.employeemanagesystem1.entity.Employee;
import cn.edu.ncut.cs.springboot.employeemanagesystem1.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Employee> employees = employeeMapper.selectList(null);
        model.addAttribute("employees", employees);
        return "admin/dashboard";
    }

    @PostMapping("/employee/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeMapper.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/employee/add")
    public String showAddForm() {
        return "admin/add-employee";
    }

    @PostMapping("/employee/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeMapper.insert(employee);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/employee/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeMapper.selectById(id);
        model.addAttribute("employee", employee);
        return "admin/edit-employee";
    }

    @PostMapping("/employee/edit")
    public String updateEmployee(@ModelAttribute Employee employee) {
        employeeMapper.updateById(employee);
        return "redirect:/admin/dashboard";
    }
} 