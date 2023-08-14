package com.java.flex.javaflexdemo.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kscs
 */
public class ThreadExecutorUtil {

    private final static String THREAD_NAME = "thread-execute-Http-task-%d";

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat(THREAD_NAME).build();

    private static final RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.DiscardPolicy();

    public static ThreadPoolExecutor threadExecutor() {

        // 获取处理器数量
        int cpuNum = Runtime.getRuntime().availableProcessors();
        // 根据cpu数量,计算出合理的线程并发数
        int threadNum = cpuNum * 2 + 1;
        int maxThreadNum = 100;

        // 默认是双核的cpu 每个核心走一个线程 一个等待线程
        return new ThreadPoolExecutor(threadNum - 1, // 核心线程数
            maxThreadNum, // 最大线程数
            Integer.MAX_VALUE, // 闲置线程存活时间
            TimeUnit.MILLISECONDS, // 时间单位
            new LinkedBlockingDeque<>(Integer.MAX_VALUE), // 线程队列
            THREAD_FACTORY, // 线程工厂
            new ThreadPoolExecutor.AbortPolicy() { // 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                    super.rejectedExecution(r, e);
                }
            });
    }

}
