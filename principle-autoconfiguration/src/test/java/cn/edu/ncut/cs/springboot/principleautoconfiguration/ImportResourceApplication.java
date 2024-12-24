package cn.edu.ncut.cs.springboot.principleautoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ImportResourceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ImportResourceApplication.class, args);
        System.out.println("context bean cat exists: " + run.containsBean("xml_bean_cat"));
        System.out.println("context bean user01 exists: " + run.containsBean("xml_bean_user"));
    }
}