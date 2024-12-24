package cn.edu.ncut.cs.springboot.principleautoconfiguration.config;

import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Car.class)
public class PropertiesConfig {
    @Autowired
    private Car car;
}
