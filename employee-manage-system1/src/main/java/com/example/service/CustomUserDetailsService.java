package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number", username);
        
        Employee employee = employeeMapper.selectOne(queryWrapper);
        
        if (employee == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return User.builder()
                .username(employee.getNumber())
                .password(passwordEncoder.encode(employee.getNumber()))
                .authorities(Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + employee.getPermission())
                ))
                .build();
    }
} 