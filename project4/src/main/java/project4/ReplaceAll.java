package project4;

public class ReplaceAll {
    public static void main(String[] args) {
        // 创建一个包含非数字字符的字符串
        String inputString = "a1b2c3d4@#5&6!7";

        // 使用 replaceAll() 方法剔除所有非数字字符
        String digitsOnly = inputString.replaceAll("[^0-9]", "");

        // 输出剔除非数字字符后的结果
        System.out.println("剔除非数字字符后的字符串: " + digitsOnly);
    }
}
