package cn.edu.ncut.cs.springboot.employeemanagesystem1.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ncut
 * @since 2024-11-28
 */
@Getter
@Setter
@TableName("employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String number;

    private String name;

    private String bumen;

    private String zhiwei;

    private String birth;

    private String sex;

    private Long salary;

    private String permission;

    private String password;



}
