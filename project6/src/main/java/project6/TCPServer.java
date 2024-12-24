package project6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("服务器已启动，监听端口 8080...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端已连接: " + clientSocket.getInetAddress());

                // 接收数据
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientMessage = input.readLine();
                System.out.println("收到客户端消息: " + clientMessage);

                // 处理并回传数据
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                output.println("服务器已收到消息: " + clientMessage);

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
