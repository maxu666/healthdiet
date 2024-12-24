package project5;

import java.io.*;

public class StringToFile {
    public static void main(String[] args) {
        // 要保存的字符串
        String content = "这是一个保存到文件中的字符串！\nhallo world！";

        // 1. 将字符串保存到文本文档
        try (FileWriter writer = new FileWriter("output.txt")) {
            // 将字符串写入文件
            writer.write(content);
            System.out.println("字符串已成功写入文件！");
        } catch (IOException e) {
            System.err.println("写入文件时出错: " + e.getMessage());
        }

        // 2. 从文本文档读取内容并输出
        try (FileReader reader = new FileReader("output.txt")) {
            int ch;
            System.out.println("读取文件内容：");
            // 逐字符读取文件内容
            while ((ch = reader.read()) != -1) {
                System.out.print((char) ch);  // 输出读取的字符
            }
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
        }
    }
}

