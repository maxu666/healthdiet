package project6;

import java.net.InetAddress;
import java.util.Scanner;

public class InetAddressExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入主机名: ");
        String hostname = scanner.nextLine();

        try {
            InetAddress address = InetAddress.getByName(hostname);
            System.out.println("主机名: " + address.getHostName());
            System.out.println("IP地址: " + address.getHostAddress());
        } catch (Exception e) {
            System.out.println("无法解析主机名: " + e.getMessage());
        }

        scanner.close();
    }
}
