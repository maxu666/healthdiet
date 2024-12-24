package project3;

// 父类 Animal
class Animal {
    // 公共成员方法 showProfile()，展示动物的基本信息
    public void showProfile() {
        System.out.println("这是一个动物类");
    }

    // 新增成员方法 speak()，表示动物的叫声
    public void speak() {
        System.out.println("动物发出声音");
    }
}

// 子类 Cat，继承自 Animal 类
class Cat extends Animal {
    // 子类特有的属性：name 和 age
    String name;
    int age;

    // 子类特有的成员方法 sleep()，表示猫的行为
    public void sleep() {
        System.out.println(name + " 正在睡觉");
    }

    // 重写父类的 speak() 方法，表示猫的叫声
    @Override
    public void speak() {
        System.out.println(name + " 喵喵叫");
    }
}

// 子类 Dog，继承自 Animal 类
class Dog extends Animal {
    // 子类特有的属性：name 和 age
    String name;
    int age;

    // 子类特有的成员方法 run()，表示狗的行为
    public void run() {
        System.out.println(name + " 在奔跑");
    }

    // 重写父类的 speak() 方法，表示狗的叫声
    @Override
    public void speak() {
        System.out.println(name + " 汪汪叫");
    }
}

public class Main {
    public static void main(String[] args) {
        // 创建一个 Cat 类的对象
        Cat myCat = new Cat();
        myCat.name = "小猫咪";  // 为 Cat 对象赋值
        myCat.age = 3;

        // 调用父类 Animal 中的方法 showProfile()
        myCat.showProfile();
        // 调用子类 Cat 中的方法 speak()，
        myCat.speak();
        // 调用子类 Cat 中的方法 sleep()
        myCat.sleep();

        System.out.println();  // 输出换行

        // 创建一个 Dog 类的对象
        Dog myDog = new Dog();
        myDog.name = "小狗狗";  // 为 Dog 对象赋值
        myDog.age = 5;

        // 调用父类 Animal 中的方法 showProfile()
        myDog.showProfile();
        // 调用子类 Dog 中的方法 speak()
        myDog.speak();
        // 调用子类 Dog 中的方法 run()
        myDog.run();
    }
}
