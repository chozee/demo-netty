package com.demo.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * An HTTP server that sends back the content of the received HTTP request
 * in a pretty plaintext form.
 */
public final class HttpHelloWorldServer {


    public static void main(String[] args) throws Exception {
        //主从多线程Reactor模式
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer() {
                 @Override
                 protected void initChannel(Channel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new HttpServerCodec()); //netty针对http编解码的处理类
                     p.addLast(new HttpServerExpectContinueHandler());//netty针对http编解码的处理类
                     p.addLast(new HttpHelloWorldServerHandler());//自己的业务处理逻辑
                 }
             });

            Channel ch = b.bind(8080).sync().channel();

            System.err.println("Open your web browser and navigate to " + "http://127.0.0.1:8080");

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
