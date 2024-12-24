package com.example.employeemanagesystem.util;

import com.example.employeemanagesystem.entity.Employee;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DaoHelper {
    private static List<Employee> employeeList = new ArrayList();
    static {
        try(InputStream in = DaoHelper.class.getClassLoader().getResourceAsStream("employee.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String line;
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            while (true){
                line = reader.readLine();
                if(null == line){
                    break;
                }
                if("".equals(line) || line.startsWith("#")){
                    continue;
                }
                String[] arr = line.split(",");
                Employee e = new Employee();
                //id,number,name,department,position,birthday,sex,salary
                e.setId(Integer.valueOf(arr[0]));
                e.setNumber(Integer.valueOf(arr[1]));
                e.setName(arr[2]);
                e.setDepartment(arr[3]);
                e.setPosition(arr[4]);
                e.setBirthday(f.parse(arr[5]));
                e.setSex(arr[6]);
                e.setSalary(Integer.valueOf(arr[7]));
                employeeList.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Employee> getEmployeeList() {
        return employeeList;
    }
    public static Employee getEmployeeById(Integer id){
        return employeeList.stream().collect(Collectors.toMap(Employee::getId, e->e)).get(id);
    }
    public static Employee deleteEmployeeById(Integer id){
        Iterator<Employee> iterator = employeeList.iterator();
        while (iterator.hasNext()){
            Employee next = iterator.next();
            if(next.getId().equals(id)){
                iterator.remove();
                return next;
            }
        }
        return null;
    }
    public static Employee updateEmployee(Employee employee){
        Employee oldEmployee = employeeList.stream().collect(
                Collectors.toMap(Employee::getId, e -> e)).get(employee.getId());
        if(employee.getName() != null){
            oldEmployee.setName(employee.getName());
        }
        if(employee.getNumber() != null){
            oldEmployee.setNumber(employee.getNumber());
        }
        if(employee.getDepartment() != null){
            oldEmployee.setDepartment(employee.getDepartment());
        }
        if(employee.getPosition() != null){
            oldEmployee.setPosition(employee.getPosition());
        }
        if(employee.getSex() != null){
            oldEmployee.setSex(employee.getSex());
        }
        if(employee.getSalary() != null){
            oldEmployee.setSalary(employee.getSalary());
        }
        if(employee.getBirthday() != null){
            oldEmployee.setBirthday(employee.getBirthday());
        }
        return oldEmployee;
    }

    public static Employee insertEmployee(Employee employee){
        employeeList.add(employee);
        return employee;
    }
}