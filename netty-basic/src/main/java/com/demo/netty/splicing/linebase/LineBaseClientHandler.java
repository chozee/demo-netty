package com.demo.netty.splicing.linebase;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类说明：使用回车换行符解决 粘包/半包问题
 */
public class LineBaseClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private AtomicInteger counter = new AtomicInteger(0);

    /*** 客户端读取到网络数据后的处理*/
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("client Accept["+msg.toString(CharsetUtil.UTF_8)
                +"] and the counter is:"+counter.incrementAndGet());
    }

    /*** 客户端被通知channel活跃后，做事*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf msg = null;
        String request = "ABC,DEF,GHI,JKL,MNO"
            + System.getProperty("line.separator");
        for(int i=0;i<100;i++){
            //Thread.sleep(50);
            System.out.println(System.currentTimeMillis()+":即将发送数据："
                    +request);
            msg = Unpooled.buffer(request.length());
            msg.writeBytes(request.getBytes());
            ctx.writeAndFlush(msg);
        }
    }

    /*** 发生异常后的处理*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
