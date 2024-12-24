package project5;

import java.io.*;
import java.util.Date;

public class FileOperations {
    public static void main(String[] args) {
        // 1. 创建文件
        File file = new File("test.txt");

        try {
            // 如果文件不存在，则创建文件
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("文件创建成功！");
            }

            // 2. 向文件写入内容
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Hello, this is a test file.");
                writer.newLine();
                writer.write("This file is created for Java file handling practice.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 3. 读取文件内容
            System.out.println("\n文件内容：");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 4. 查看文件的长度
            System.out.println("\n文件长度: " + file.length() + " 字节");

            // 5. 查看文件的最后修改时间
            long lastModified = file.lastModified();
            Date lastModifiedDate = new Date(lastModified);
            System.out.println("文件最后修改时间: " + lastModifiedDate);

            // 6. 查看文件夹下所有文件
            File folder = new File(".");  // 当前工作目录
            System.out.println("\n当前文件夹中的文件和目录：");
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    System.out.println(f.getName() + (f.isDirectory() ? " [文件夹]" : " [文件]"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
