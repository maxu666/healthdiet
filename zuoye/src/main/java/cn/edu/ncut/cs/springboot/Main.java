package cn.edu.ncut.cs.springboot;

import cn.edu.ncut.cs.springboot.Calc;

class Main {
    public static void main(String[] args) {
        // 创建 Calc 类的对象
        Calc calculator = new Calc();

        // 测试加法
        double resultAdd = calculator.add(5.5, 3.5);
        System.out.println("5.5 + 3.2 = " + resultAdd);

        // 测试减法
        double resultSubtract = calculator.subtract(10.0, 4.5);
        System.out.println("10.0 - 4.5 = " + resultSubtract);

        // 测试乘法
        double resultMultiply = calculator.multiply(2.0, 3.5);
        System.out.println("6.0 * 7.5 = " + resultMultiply);

        // 测试除法
        double resultDivide = calculator.divide(20.0, 4.0);
        System.out.println("20.0 / 4.0 = " + resultDivide);

    }
}