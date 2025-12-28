package cn.edu.ncut.cs.springboot;

public class Calc {

    // 加法运算
    public double add(double a, double b) {
        return a + b;
    }

    // 减法运算
    public double subtract(double a, double b) {
        return a - b;
    }

    // 乘法运算
    public double multi(double a, double b) {
        return a * b;
    }

    // 除法运算
    public double divide(double a, double b) {
        if (b == 0) {
            System.out.println("错误：除数不能为零！");
            return Double.NaN;  // 返回 NaN 表示除法错误
        }
        return a / b;
    }
}

