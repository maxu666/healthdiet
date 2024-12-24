package project4;

public class CharAt {
    public static void main(String[] args) {
        // 创建一个字符串
        String str = "Hello,World";

        // 使用 charAt(0) 获取第一个字符
        char firstChar = str.charAt(0);
        System.out.println("第一个字符: " + firstChar);

        // 使用 charAt(str.length() - 1) 获取最后一个字符
        char lastChar = str.charAt(str.length() - 1);
        System.out.println("最后一个字符: " + lastChar);
    }
}

