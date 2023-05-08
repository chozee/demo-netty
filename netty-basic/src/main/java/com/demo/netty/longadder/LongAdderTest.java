package com.demo.netty.longadder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest {

    public static void main(String[] args) {
        testAtomicLongVSLongAdder(10, 10000);
        System.out.println("==================");
        testAtomicLongVSLongAdder(10, 200000);
        System.out.println("==================");
        testAtomicLongVSLongAdder(100, 200000);
    }
    //AtomicLong与LongAdder多线程并发模拟及耗时统计
    static void testAtomicLongVSLongAdder(final int threadCount, final int times) {
        try {
            long start = System.currentTimeMillis();
            testLongAdder(threadCount, times);
            long end = System.currentTimeMillis() - start;
           // System.out.println("条件>>>>>>线程数:" + threadCount + ", 单线程操作" + times);
            System.out.println("LongAdder--count" + (threadCount * times) + ",time:" + end);

            long start2 = System.currentTimeMillis();
            testAtomicLong(threadCount, times);
            long end2 = System.currentTimeMillis() - start2;
            System.out.println("Atomic--count" + (threadCount * times) + ",time:" + end2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //使用AtomicLong模拟i++多线程并发：threadCount线程数、times每个线程运行多少次
    static void testAtomicLong(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);//发令枪：确保多线程同时运行
        AtomicLong atomicLong = new AtomicLong();
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < times; j++) {
                        atomicLong.incrementAndGet(); //++操作
                    }
                    countDownLatch.countDown();
                }
            }, "my-thread" + i).start();
        }
        countDownLatch.await();
    }
    //使用LongAdder模拟i++多线程并发：threadCount线程数、times每个线程运行多少次
    static void testLongAdder(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < times; j++) {
                        longAdder.add(1);//是原子操作，多线程安全  //++操作
                    }
                    countDownLatch.countDown();
                }
            }, "my-thread" + i).start();
        }

        countDownLatch.await();
    }
}