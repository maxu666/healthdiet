package Animal;

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
        // 创建一个长度为4的 Animal 类型的数组
        Animal[] animals = new Animal[4];

        // 初始化数组元素，偶数下标为 Cat 对象，奇数下标为 Dog 对象
        for (int i = 0; i < animals.length; i++) {
            if (i % 2 == 0) {  // 偶数下标，创建 Cat 对象
                Cat cat = new Cat();
                cat.name = "小猫" + (i / 2 + 1);  // 为 Cat 对象赋值
                cat.age = 3;
                animals[i] = cat;
            } else {  // 奇数下标，创建 Dog 对象
                Dog dog = new Dog();
                dog.name = "小狗" + (i / 2 + 1);  // 为 Dog 对象赋值
                dog.age = 5;
                animals[i] = dog;
            }
        }

        // 遍历数组，调用每个动物对象的 speak() 方法
        for (Animal animal : animals) {
            animal.speak();  // 会根据对象的实际类型调用相应的 speak() 方法
        }
    }
}
