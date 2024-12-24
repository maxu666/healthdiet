package project3;

import java.util.Scanner;

public class Rectangle {
    double width = 0;
    double height = 0;

    // 计算面积的方法
    double getArea() {
        return width * height;
    }

    // 计算周长的方法
    double getPerimeter() {
        return (width + height) * 2;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 提示用户输入矩形的宽度和高度
        System.out.print("请输入矩形的宽度：");
        double width = scanner.nextDouble();

        System.out.print("请输入矩形的高度：");
        double height = scanner.nextDouble();

        area r = new area();
        r.width = width;
        r.height = height;
        // 输出面积和周长
        System.out.println("矩形的面积为：" + r.getArea());
        System.out.println("矩形的周长为：" + r.getPerimeter());
    }
}
