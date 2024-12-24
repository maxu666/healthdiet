package cn.edu.ncut.cs.springboot.employeemanagesystem1.mapper;

import cn.edu.ncut.cs.springboot.employeemanagesystem1.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    // 可以在这里定义自定义查询方法
}
