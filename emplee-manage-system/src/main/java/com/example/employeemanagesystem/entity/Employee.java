package com.example.employeemanagesystem.entity;

import java.util.Date;

public class Employee {
    /**
     * 物理主键
     */
    private Integer id;
    /**
     * 员工编号，业务主键
     */
    private Integer number;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 部门
     */
    private String department;
    /**
     * 生日
     */
    private transient Date birthday;
    /**
     * 性别
     */
    private String sex;

    /**
     * 薪水
     */
    private Integer salary;

    /**
     * 职位
     */
    private String position;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
