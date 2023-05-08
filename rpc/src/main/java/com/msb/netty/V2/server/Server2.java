package com.msb.netty.V2.server;

import com.msb.netty.pre.IUserService;
import com.msb.netty.pre.User;
import com.msb.netty.pre.UserServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 */
public class Server2 {
    private static boolean running = true;
    public static void main(String[] args) throws  Exception{
        ServerSocket serverSocket = new ServerSocket(8888);
        while (running){
            Socket socket = serverSocket.accept();
            process(socket);
            socket.close();
        }
        serverSocket.close();
    }
    private static void  process(Socket socket) throws Exception{
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        DataInputStream dataInputStream = new DataInputStream(in);
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        int id = dataInputStream.readInt();
        IUserService service =  new UserServiceImpl();
        User user = service.findUserByID(id);
        dataOutputStream.writeInt(user.getId());
        dataOutputStream.writeUTF(user.getName());
        dataOutputStream.flush();
    }
}
