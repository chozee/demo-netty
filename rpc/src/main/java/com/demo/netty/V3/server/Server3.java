package com.demo.netty.V3.server;


import com.demo.netty.pre.UserServiceImpl;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端：服务更灵活-提供多个类、多个方法的远程接口调用
 */
public class Server3 {
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
        ObjectInputStream ois = new ObjectInputStream(in);

        //我自己定义的协议（className|methodName|parametersTypes|args）
        //TODO 拿到客户端传递过来的class
        String clazzName =ois.readUTF();
        String methodName =ois.readUTF();
        Class[] parameterTypes = (Class[])ois.readObject();
        Object[] args =(Object[])ois.readObject();
        //反射拿到class
        Class clazz =Class.forName(clazzName);
        if(clazz.isInterface()){
            if(clazzName.equals("com.demo.netty.pre.IUserService")){
                clazz = UserServiceImpl.class;
            }
           //这里可以使用反射机制拿到所有接口对应的实现类
        }

        Method method = clazz.getMethod(methodName,parameterTypes);

        Object object = method.invoke(clazz.newInstance(),args);
        //TODO 返回值：使用对象进行返回
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(object);
        oos.flush();
    }
}
