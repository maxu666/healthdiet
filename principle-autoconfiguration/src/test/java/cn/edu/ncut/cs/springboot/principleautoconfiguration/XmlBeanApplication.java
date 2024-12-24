package cn.edu.ncut.cs.springboot.principleautoconfiguration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlBeanApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("context bean cat exists: " + context.containsBean("xml_bean_cat"));
        System.out.println("context bean user01 exists: " + context.containsBean("xml_bean_user"));
    }
}
