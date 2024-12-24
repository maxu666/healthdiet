package cn.edu.ncut.cs.springboot.principleautoconfiguration.config;

import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.Pet;
import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
    @Bean
    public User myConfigBeanUser() {
        return new User("zhangsan", 18);
    }

    @Bean
    public Pet myConfigBeanPet() {
        return new Pet("tomcat");
    }
}
