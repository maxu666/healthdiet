package cn.edu.ncut.cs.springboot.employeemanagesystem1.service.impl;

import cn.edu.ncut.cs.springboot.employeemanagesystem1.entity.Employee;
import cn.edu.ncut.cs.springboot.employeemanagesystem1.mapper.EmployeeMapper;
import cn.edu.ncut.cs.springboot.employeemanagesystem1.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ncut
 * @since 2024-11-28
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
