package com.example.reflection;

import java.lang.annotation.*;

public class Test12 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        Class c1 = Class.forName("com.example.reflection.Student2");

        //通过反射获得注解
        Annotation[] annotations = c1.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }

        Table table = (Table)c1.getAnnotation(Table.class);
        String value = table.value();
        System.out.println("table:"+value);

        java.lang.reflect.Field name = c1.getDeclaredField("name");
        Column annotation = name.getAnnotation(Column.class);
        System.out.println("\tcolumn name:"+annotation.columnName());
        System.out.println("\tcolumn type:"+annotation.type());
        System.out.println("\tcolumn length:"+annotation.length());
    }
}

@Table("db_student")
class Student2{
    @Column(columnName = "db_id",type="int", length = 10)
    private int id;
    @Column(columnName = "db_age",type="int", length = 10)
    private int age;
    @Column(columnName = "db_name",type="varchar", length = 3 )
    private String name;

    public Student2() {
    }

    @Override
    public String toString() {
        return "Student2{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student2(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }
}


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Table {
    String value();
}

@Target(ElementType.FIELD )
@Retention(RetentionPolicy.RUNTIME)
@interface Column {
    String columnName();

    String type();

    int length();

}

