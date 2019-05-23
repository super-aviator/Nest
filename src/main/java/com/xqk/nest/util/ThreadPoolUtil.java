package com.xqk.nest.util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolUtil implements DisposableBean {
    private static final ExecutorService es=Executors.newCachedThreadPool();

    private ThreadPoolUtil(){}

    public void execute(Runnable task){
        es.execute(task);
    }

    @Override
    public void destroy(){
        es.shutdownNow();
        System.out.println("------------------------线程池被销毁------------------------");
    }
}
