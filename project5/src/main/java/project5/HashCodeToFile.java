package project5;

import java.io.*;

public class HashCodeToFile {
    public static void main(String[] args) {
        // 要计算哈希值的字符串
        String text = "Hello, this is a test string for hashCode!";

        // 1. 获取字符串的哈希值
        int hashValue = text.hashCode();
        System.out.println("原始字符串的哈希值: " + hashValue);

        // 2. 将哈希值保存到文本文档中
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("hashValue.txt"))) {
            writer.write(String.valueOf(hashValue));
            System.out.println("哈希值已成功保存到文件 hashValue.txt！");
        } catch (IOException e) {
            System.out.println("写入文件时出错: " + e.getMessage());
        }

        // 3. 从文本文档读取哈希值并输出
        try (BufferedReader reader = new BufferedReader(new FileReader("hashValue.txt"))) {
            String storedHashValue = reader.readLine();
            System.out.println("从文件中读取的哈希值: " + storedHashValue);
        } catch (IOException e) {
            System.out.println("读取文件时出错: " + e.getMessage());
        }
    }
}

