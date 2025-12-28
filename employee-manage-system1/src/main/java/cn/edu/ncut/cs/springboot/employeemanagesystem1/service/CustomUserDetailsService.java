package cn.edu.ncut.cs.springboot.employeemanagesystem1.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.ncut.cs.springboot.employeemanagesystem1.entity.Employee;
import cn.edu.ncut.cs.springboot.employeemanagesystem1.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;

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
                .password("{noop}" + employee.getPassword())  // 使用 {noop} 前缀表示明文密码
                .authorities(Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + employee.getPermission())
                ))
                .build();
    }
}
