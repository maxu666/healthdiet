package cn.edu.ncut.cs.springboot.principleautoconfiguration.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class GetProperties {
    public static void main(String[] args) {
        Properties properties = new Properties();
        String propertyFile = "src/main/resources/application.properties";//配置文件文件路径
        try {
            properties.load(new FileInputStream(propertyFile));
            Enumeration<?> propertyNames = properties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String strKey = (String) propertyNames.nextElement();
                String strValue = properties.getProperty(strKey);
                System.out.println(strKey + "=" + strValue);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
