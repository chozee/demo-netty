package com.demo.netty;


/**
 * 类说明：nio通信服务端
 */
public class NioServer {
    private static NioServerHandle nioServerHandle;

    public static void start(){
        if(nioServerHandle !=null) nioServerHandle.stop();
        nioServerHandle = new NioServerHandle(10001);
        new Thread(nioServerHandle,"Server").start();
    }
    public static void main(String[] args){
        start();
    }

}
