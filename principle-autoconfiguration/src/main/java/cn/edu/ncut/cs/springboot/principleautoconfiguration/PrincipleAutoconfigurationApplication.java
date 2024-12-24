package cn.edu.ncut.cs.springboot.principleautoconfiguration;

import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.Car;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PrincipleAutoconfigurationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(PrincipleAutoconfigurationApplication.class, args);
        Car car = run.getBean(Car.class);
        System.out.println("car=" + car);
    }

}
