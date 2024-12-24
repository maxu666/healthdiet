package project4;

public class Stringdemo {
    public static void main(String[] args) {
        // 创建一个字符串
        String originalString = "Hello";

        // 使用 toUpperCase() 方法将字符串转换为大写
        String upperCaseString = originalString.toUpperCase();
        System.out.println("大写字符串: " + upperCaseString);

        // 使用 toLowerCase() 方法将字符串转换为小写
        String lowerCaseString = originalString.toLowerCase();
        System.out.println("小写字符串: " + lowerCaseString);

        // 使用 concat() 方法连接字符串
        String additionalString = " World";
        String concatenatedString = originalString.concat(additionalString);
        System.out.println("拼接后的字符串: " + concatenatedString);
    }
}
