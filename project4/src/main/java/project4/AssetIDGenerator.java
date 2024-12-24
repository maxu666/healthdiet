package project4;

import java.util.Scanner;

public class AssetIDGenerator {
    public static void main(String[] args) {
        // 创建 Scanner 对象来获取用户输入
        Scanner scanner = new Scanner(System.in);

        // 获取年份
        System.out.print("请输入年份：");
        int year = scanner.nextInt();

        // 获取产品类型
        System.out.print("请输入产品类型（1：台式计算机，2：笔记本电脑，3：其他设备）：");
        int productType = scanner.nextInt();

        // 获取随机数（假设随机数已经由用户输入，并且是一个三位数）
        System.out.print("请输入随机数（三位数，范围：100-999）：");
        int randomNumber = scanner.nextInt();

        // 生成固定资产编号
        String assetID = generateAssetID(year, productType, randomNumber);

        // 输出生成的固定资产编号
        System.out.println("生成的固定资产编号是：" + assetID);

        // 关闭 scanner
        scanner.close();
    }

    // 生成固定资产编号的方法
    public static String generateAssetID(int year, int productType, int randomNumber) {
        // 将年份转换为字符串
        String yearStr = String.valueOf(year);

        // 将产品类型转换为字符串
        String productTypeStr = String.valueOf(productType);

        // 将随机数转换为三位数字（确保随机数是三位数，不足三位时前面补零）
        String randomStr = String.format("%03d", randomNumber);

        // 拼接成固定资产编号
        return yearStr + productTypeStr + randomStr;
    }
}

