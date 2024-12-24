package cn.edu.ncut.cs.springboot.employeemanagesystem1.controller;

import cn.edu.ncut.cs.springboot.employeemanagesystem1.entity.Employee;
import cn.edu.ncut.cs.springboot.employeemanagesystem1.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *前端控制器 * </p>
 *
 * @author ncut
 * @since 2024-11-24
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private IEmployeeService addressService;

    @PutMapping("/save")
    public boolean saveOrUpdate(@RequestBody Employee employee){
        return addressService.saveOrUpdate(employee);
    }
    @GetMapping("/list")
    public List<Employee> remove(){
        return addressService.list();
    }
}
