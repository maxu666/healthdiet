package org.mrchi;

import org.mrchi.AOPBeanConfig;
import org.mrchi.Calculator;
import org.testng.annotations.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AOPTest {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(AOPBeanConfig.class);
        Calculator c = app.getBean(Calculator.class);
        System.out.println("第一次运行:");
        c.div(40, 13);
        System.out.println("第二次运行:");
        c.div(40, 0);
        app.close();
    }
}