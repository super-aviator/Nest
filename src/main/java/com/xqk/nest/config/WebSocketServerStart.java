package com.xqk.nest.config;

import com.xqk.nest.websocket.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServerStart implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("------------------------WebSocket线程启动成功------------------------");
        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
            new Thread(() -> {
                try {
                    webSocketServer.start();
                } catch (Exception e) {
                    System.err.println("------------------------WebSocket线程启动失败------------------------");
                    e.printStackTrace();
                }
            }).start();//开启WebSocket线程
        }
}
