package cn.edu.ncut.cs.springboot.principleautoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestConditionalBeanApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TestConditionalBeanApplication.class, args);
        System.out.println("容器中含有tom22的组件:" + run.containsBean("tome22"));
        System.out.println("容器中含有lisi组件:" + run.containsBean("lisi"));
    }
}
