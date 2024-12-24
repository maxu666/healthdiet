package project3;

public class Student {
    private String id;//学号
    private String name;//姓名
    private String gender;//性别
    private int classId;//班号
    private int age;//年龄

    //设置学生id
    public void setId(String id) {
        this.id = id;
    }
    //获取学生id
    public String getId() {
        return id;
    }

    //设置班级id
    public void setClassId(int classId) {
        this.classId = classId;
    }
    //获取班级ID
    public int getClassId() {
        return classId;
    }

    //设置年龄
    public void setAge(int age) {
        this.age = age;
    }
    //获取年龄
    public int getAge() {
        return age;
    }

    //设置姓名
    public void setName(String name){
        this.name = name;
    }
    //获取姓名
    public String getName() {
        return name;
    }

    //设置性别
    public void setGender(String gender) {
        this.gender = gender;
    }
    //获取性别
    public String getGender() {
        return gender;
    }

    //重写toString()
    public String toString() {
        return "学生号："+id+ "  姓名："+name+"  性别："+gender+"  班级号："+classId+" 年龄："+age;
    }

    public static void main(String args[]) {
        Student s = new Student();
        s.setId("22101020103");
        s.setName("马旭");
        s.setGender("男");
        s.setClassId(221);
        s.setAge(20);
        System.out.println(s);
    }
}
