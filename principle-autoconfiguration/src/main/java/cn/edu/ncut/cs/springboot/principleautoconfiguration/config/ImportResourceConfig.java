package cn.edu.ncut.cs.springboot.principleautoconfiguration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:beans.xml")
@Configuration
public class ImportResourceConfig {
}