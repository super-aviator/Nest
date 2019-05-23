package com.xqk.nest.websocket.service;

import com.xqk.nest.websocket.handlers.BinaryFrameHandler;
import com.xqk.nest.websocket.handlers.MessageChannelHandler;
import com.xqk.nest.websocket.handlers.SignChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config/application.properties")
public class WebSocketServer {
    @Value("${webSocketAddress}")
    private String webSocketAddress;

    @Value("${webSocketPort}")
    private int webSocketPort;

    @Autowired
    private SignChannelHandler signChannelHandler;

    @Autowired
    private MessageChannelHandler messageChannelHandler;

    private ChannelFuture future;

    public void start() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    //消息编解码器
                                    new HttpServerCodec(),
                                    //最大消息长度，并聚合消息
                                    new HttpObjectAggregator(655360),
                                    //websocket路径
                                    new WebSocketServerProtocolHandler("/chat"),
                                   signChannelHandler,
                                    messageChannelHandler
                                    //new BinaryFrameHandler()
                            );
                        }
                    });
            future = b.bind(webSocketAddress,webSocketPort).sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully().sync();
            work.shutdownGracefully().sync();
        }
    }
}
