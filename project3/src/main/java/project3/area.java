package project3;

import java.util.Scanner;

public class area {
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

        // 提示用户输入土地的数量
        System.out.print("请输入土地的数量：");
        int n = scanner.nextInt();  // 获取土地的数量

        // 循环处理每一块土地
        for (int i = 0; i < n; i++) {
            // 提示用户输入每块土地的宽度和高度
            System.out.println("请输入第 " + (i + 1) + " 块土地的相关信息：");

            System.out.print("请输入土地的宽度：");
            double width = scanner.nextDouble();

            System.out.print("请输入土地的高度：");
            double height = scanner.nextDouble();

            // 创建Rectangle对象并设置属性
            Rectangle r = new Rectangle();
            r.width = width;
            r.height = height;

            // 输出每块土地的面积和周长
            System.out.println("第 " + (i + 1) + " 块土地的面积为：" + r.getArea());
            System.out.println("第 " + (i + 1) + " 块土地的周长为：" + r.getPerimeter());
            System.out.println();
        }

        scanner.close();  // 关闭扫描器
    }
}
