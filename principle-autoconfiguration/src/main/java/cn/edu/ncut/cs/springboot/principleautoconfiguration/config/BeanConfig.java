package cn.edu.ncut.cs.springboot.principleautoconfiguration.config;

import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.Pet;
import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @ConditionalOnBean(name = "tom23")
    @Bean("lisi")//当容器中有tom23这个bean的时候，才会创建lisi这个bean
    public User conditionalUser() {
        return new User("lisi", 20);
    }

    @Bean("tom22")
    public Pet conditionalPet() {
        return new Pet("conditionalPet");
    }
}