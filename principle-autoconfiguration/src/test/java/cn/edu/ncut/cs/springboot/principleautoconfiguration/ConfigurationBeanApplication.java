package cn.edu.ncut.cs.springboot.principleautoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ConfigurationBeanApplication {

    public static void main(String[] args) {
        //返回IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(ConfigurationBeanApplication.class, args);
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("MyConfig bean pet exists: " + run.containsBean("myConfigBeanPet"));
        System.out.println("MyConfig bean user exists: " + run.containsBean("myConfigBeanUser"));
    }
}