package com.demo.netty.udp.unicast;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * 类说明：应答Handler
 */
public class AnswerHandler extends
        SimpleChannelInboundHandler<DatagramPacket> {

    /*应答的具体内容从常量字符串数组中取得，由nextQuote方法随机获取*/
    private static final String[] DICTIONARY = {
            "只要功夫深，铁棒磨成针。",
            "旧时王谢堂前燕,飞入寻常百姓家。",
            "洛阳亲友如相问，一片冰心在玉壶。",
            "一寸光阴一寸金，寸金难买寸光阴。",
            "老骥伏枥，志在千里，烈士暮年，壮心不已" };
    private static Random r = new Random();
    private String nextQuote(){
        return DICTIONARY[r.nextInt(DICTIONARY.length-1)];
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                DatagramPacket packet)
            throws Exception {

        //获得请求
        String req = packet.content().toString(CharsetUtil.UTF_8);
        if(UdpQuestionSide.QUESTION.equals(req)){
            String answer = UdpAnswerSide.ANSWER+nextQuote();
            System.out.println("接收到请求："+req);
            /**
             * 重新 new 一个DatagramPacket对象，我们通过packet.sender()来获取发送者的消息。重新发送出去！
             */
            ctx.writeAndFlush(
                    new DatagramPacket(
                            Unpooled.copiedBuffer(
                                    answer,
                                    CharsetUtil.UTF_8),
                            packet.sender()));
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
