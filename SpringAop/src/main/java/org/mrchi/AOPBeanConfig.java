package org.mrchi;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy//用于启动Spring的注解Aspect功能
public class AOPBeanConfig {
    @Bean//将Calculator对象放入容器中
    public Calculator calculator() {
        return new Calculator();
    }
    @Bean//将LogAspects对象放入容器中
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
