package project4;

public class MathExample {
    public static void main(String[] args) {
        // 求绝对值
        double num1 = -25.5;
        System.out.println("绝对值: " + Math.abs(num1));

        // 求最大值和最小值
        int a = 5, b = 12;
        System.out.println("最大值: " + Math.max(a, b));
        System.out.println("最小值: " + Math.min(a, b));

        // 求平方根
        double num2 = 16.0;
        System.out.println("平方根: " + Math.sqrt(num2));

        // 计算幂 (x^y)
        double base = 2.0, exponent = 3.0;
        System.out.println("幂: " + Math.pow(base, exponent));

        // 取整（向上取整、向下取整）
        double num3 = 3.14;
        System.out.println("向上取整: " + Math.ceil(num3));
        System.out.println("向下取整: " + Math.floor(num3));

        // 四舍五入
        double num4 = 3.6;
        System.out.println("四舍五入: " + Math.round(num4));
    }
}

