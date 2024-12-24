package project5;

import java.io.*;

public class ImageCopy {
    public static void main(String[] args) {
        // 原图片文件路径（可以更改为任意路径）
        String sourceImagePath = "D:\\Pycharm\\hehua.jpg";  // 请替换为你本地图片的路径
        // 目标文件路径
        String targetImagePath = "a.jpg";

        // 1. 读取图片文件并将其存储为字节数组
        try (FileInputStream fis = new FileInputStream(sourceImagePath)) {
            // 获取文件的字节长度
            long fileLength = new File(sourceImagePath).length();
            byte[] imageBytes = new byte[(int) fileLength];

            // 读取文件到字节数组
            int bytesRead = fis.read(imageBytes);
            if (bytesRead == -1) {
                System.out.println("文件读取失败！");
                return;
            }
            System.out.println("图片文件成功读取到内存！");

            // 2. 将字节数组写入新文件 a.jpg
            try (FileOutputStream fos = new FileOutputStream(targetImagePath)) {
                fos.write(imageBytes);
                System.out.println("字节数据成功写入到新文件 a.jpg！");
            } catch (IOException e) {
                System.out.println("写入新文件时出错: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("读取图片文件时出错: " + e.getMessage());
        }
    }
}

