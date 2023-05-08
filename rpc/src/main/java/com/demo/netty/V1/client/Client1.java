package com.demo.netty.V1.client;

import com.demo.netty.pre.User;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * 解决远程通讯问题
 */
public class Client1 {
    //远程调用类
    public static User findUserByIDRemote(Integer id) throws Exception{
        //远程通讯
        Socket socket = new Socket("127.0.0.1",8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(id);
        socket.getOutputStream().write(out.toByteArray());
        socket.getOutputStream().flush();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String name = dis.readUTF();
        User user = new User(id,name);
        dos.close();
        socket.close();
        return user;
    }
}
