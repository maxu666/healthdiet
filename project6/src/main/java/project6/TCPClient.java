package project6;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8080)) {
            System.out.println("已连接到服务器");

            // 发送数据
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("Hello, Server!");

            // 接收服务器返回数据
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverMessage = input.readLine();
            System.out.println("服务器回复: " + serverMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

