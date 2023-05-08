package com.demo.netty.pipeline;

import java.text.DecimalFormat;

/**
 * Pipeline实现类
 */
public class Pipeline {
    //handler上下文，维护链表和负责链表的执行
    class HandlerChainContext {
        HandlerChainContext next;// 持有下一个节点:单链表
        AbstractHandler handler;
        public HandlerChainContext(AbstractHandler handler) {
            this.handler = handler;
        }
        // 将节点持有下去
        void handler(Object arg0) {
            this.handler.doHandler(this, arg0);
        }
        // 继续执行下一个
        void runNext(Object arg0) {
            if (this.next != null) {
                this.next.handler(arg0);
            }
        }
    }
    // 持有上下文(可以获得需要的数据,属性)
    public HandlerChainContext context = new HandlerChainContext(new AbstractHandler() {
        @Override
        void doHandler(HandlerChainContext context, Object arg0) {
            System.out.println("折扣前"+arg0);
            context.runNext(arg0);
        }
    });

    // 添加责任链
    public void addLast(AbstractHandler handler) {
        HandlerChainContext next = context;
        while (next.next != null) {
            next = next.next;
        }
        next.next = new HandlerChainContext(handler);
    }

    // 开始调用
    public void requestProcess(Object arg0) {
        context.handler(arg0);
    }
    //处理器抽象类
    static abstract class AbstractHandler {
        abstract void doHandler(HandlerChainContext context, Object arg0);
    }
    //具体的处理器实现类（购物折扣1）
    static class Handler1 extends AbstractHandler {
        @Override
        void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
            System.out.println("--首次购买打9折！");
            arg0 = new DecimalFormat("0.00").format(Double.valueOf(arg0.toString())*0.9);
            System.out.println("折扣后金额："+arg0);
            // 继续执行下一个
            handlerChainContext.runNext(arg0);
        }
    }
    //具体的处理器实现类（购物折扣2）
    static class Handler2 extends AbstractHandler {
        @Override
        void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
            System.out.println("--满200减20！");
            if(Double.valueOf(arg0.toString()) >= 200){
                arg0 = Double.valueOf(arg0.toString())-20;
                // 继续执行下一个
                System.out.println("折扣后金额："+arg0);
                handlerChainContext.runNext(arg0);

            }else{
                System.out.println("不满足条件，折扣结束："+arg0);
            }
        }
    }
    //具体的处理器实现类（购物折扣3）
    static class Handler3 extends AbstractHandler {
        @Override
        void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
            System.out.println("--第二件减10元！");
            arg0 = Double.valueOf(arg0.toString())-10;
            System.out.println("折扣后金额："+arg0);
            // 继续执行下一个
            handlerChainContext.runNext(arg0);
        }
    }

    public static void main(String[] args) {
        Pipeline p = new Pipeline();
        p.addLast(new Handler1());
        p.addLast(new Handler2());
        p.addLast(new Handler3());

        p.requestProcess("150");
    }

}