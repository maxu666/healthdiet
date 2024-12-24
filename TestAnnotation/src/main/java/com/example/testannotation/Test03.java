package com.example.testannotation;

// 导入必要的Java注解相关类
import java.lang.annotation.ElementType;      // 注解可以应用的程序元素类型
import java.lang.annotation.Retention;        // 注解的保留策略
import java.lang.annotation.RetentionPolicy;  // 注解保留策略的枚举
import java.lang.annotation.Target;           // 注解的目标范围

// 在类上使用MyAnnotation2注解，并设置value值
@MyAnnotation2("value")
public class Test03 {
    // 在方法上使用MyAnnotation注解
    // 设置name属性为"hello"，schools属性为包含"NCUT"的字符串数组
    @MyAnnotation(name = "hello", schools = {"NCUT"})
    public void test() {
    }
}

// 定义MyAnnotation注解
@Target({ElementType.TYPE, ElementType.METHOD})  // 该注解只能用于类型和方法上(空间)
@Retention(RetentionPolicy.RUNTIME)             // 注解会在运行时保留(时间)，可以通过反射获取
@interface MyAnnotation{
    // 定义注解的属性
    String name() default "";      // name属性，默认值为空字符串
    int age() default 0;           // age属性，默认值为0
    int id() default -1;           // id属性，默认值为-1
    String[] schools();            // schools属性，必须指定值（没有默认值）
}

// 定义MyAnnotation2注解
@Target({ElementType.TYPE, ElementType.METHOD})  // 该注解只能用于类型和方法上(空间)
@Retention(RetentionPolicy.RUNTIME)             // 注解会在运行时保留，可以通过反射获取(时间)
@interface MyAnnotation2{
    // 定义一个名为value的属性
    // 当注解只有一个属性且名称为value时，在使用注解时可以省略属性名
    String value();
}


/*
补充说明:
@Target 说明注解的使用范围：
    ElementType.TYPE: 可以用于类、接口等类型声明
    ElementType.METHOD: 可以用于方法声明

@Retention 说明注解的生命周期：
    RetentionPolicy.RUNTIME: 注解会保留到运行时，可以通过反射机制读取*/
