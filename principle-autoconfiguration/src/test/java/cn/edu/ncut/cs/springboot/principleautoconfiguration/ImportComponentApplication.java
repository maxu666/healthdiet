package cn.edu.ncut.cs.springboot.principleautoconfiguration;

import ch.qos.logback.core.helpers.NOPAppender;
import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ImportComponentApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ImportComponentApplication.class, args);
        String[] beanNamesForType = run.getBeanNamesForType(NOPAppender.class);
        for (String s : beanNamesForType) {
            System.out.println(s);
        }

        String[] userBeanNames = run.getBeanNamesForType(User.class);
        for (String userBeanName : userBeanNames) {
            System.out.println(userBeanName);
        }

    }
}
