package InterfaceA;

// 定义接口 InterfaceA
interface InterfaceA {
    // 计算 1 到 n 的和
    int method(int n);
}

// 定义类 ClassA 实现接口 InterfaceA
class ClassA implements InterfaceA {

    // 实现接口中的 method 方法
    @Override
    public int method(int n) {
        int sum = 0;
        // 使用循环计算从 1 到 n 的整数和
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }
}

public class Main {
    public static void main(String[] args) {
        // 创建 ClassA 类的对象
        ClassA classA = new ClassA();

        // 计算 1 到 10 的和
        int result = classA.method(10);

        // 输出结果
        System.out.println("1 到 10 的和是: " + result);
    }
}
