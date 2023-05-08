package com.demo.netty.V1.server;


import com.demo.netty.pre.IUserService;
import com.demo.netty.pre.User;
import com.demo.netty.pre.UserServiceImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 */
public class Server1 {
    private static boolean running = true;
    public static void main(String[] args) throws  Exception{
        ServerSocket serverSocket = new ServerSocket(8888);//监听 8888
        while (running){
            Socket socket = serverSocket.accept();//有网络请求过来了（客户端-服务端）
            process(socket);
            socket.close();
        }
        serverSocket.close();
    }
    //网络通讯的代码  业务代码 耦合在一起。
    private static void  process(Socket socket) throws Exception{
        InputStream in = socket.getInputStream();//这个就是客户端送过来的ID
        OutputStream out = socket.getOutputStream();

        DataInputStream dataInputStream = new DataInputStream(in);
        DataOutputStream dataOutputStream = new DataOutputStream(out);

        int id = dataInputStream.readInt();
        IUserService service =  new UserServiceImpl();//服务器的本地
        User user = service.findUserByID(id);
        dataOutputStream.writeUTF(user.getName());
        //改动 加入新的属性处理
        dataOutputStream.flush();
    }
}
