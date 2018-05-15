package org.nuaa.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于TCP协议的Socket通信，实现文件传输-服务器端
 */
public class Server {

    public static void main(String[] args) {
    try {
        // 创建一个服务器端的ServerSocket，绑定并监听9999端口
        ServerSocket serverSocket = new ServerSocket(9999);
        int count = 0;// 记录客户端的数量
        System.out.println("服务器1启动，等待客户端的连接。。。");
        Socket socket1 = null;
        Thread serverHandleThread =  null;
        while (true) {
        // 循环监听等待客户端的连接
        socket1 = serverSocket.accept();
        // 每当一个客户端连接到服务器，服务器启动一个单独的线程处理与此客户端的通信
        ++count;
        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
        switch(dis.readInt()) {
        case 0:
//        		dis.close();
            serverHandleThread = new Thread(new Uploading(socket1));
            break;
        case 1:
//        		dis.close();
            serverHandleThread = new Thread(new Download(socket1));
            break;
        default:
        		break;
        }
        serverHandleThread.setPriority(4);// 设置线程的优先级[1,10],1为最低，默认是5
        serverHandleThread.start();
        System.out.println("上线的客户端有" + count + "个！");
        InetAddress inetAddress = socket1.getInetAddress();
        System.out.println("当前客户端的IP地址是："+ inetAddress.getHostAddress());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}