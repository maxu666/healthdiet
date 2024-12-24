package cn.edu.ncut.cs.springboot.employeemanagesystem1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.edu.ncut.cs.springboot.employeemanagesystem1.mapper")  // 扫描 Mapper 接口
public class EmployeeManageSystem1Application {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManageSystem1Application.class, args);
	}
}
