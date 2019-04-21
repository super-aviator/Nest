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
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class WebSocketServer {
    @Autowired
    private SignChannelHandler signChannelHandler;

    @Autowired
    private MessageChannelHandler messageChannelHandler;

    public void start() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(8081))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new HttpServerCodec(),//消息编解码器
                                    new HttpObjectAggregator(655360),//最大消息长度，并聚合消息
                                    new WebSocketServerProtocolHandler("/chat"),//websocket路径
                                   signChannelHandler,
                                    messageChannelHandler
//                                    new BinaryFrameHandler()
                            );
                        }
                    });
            ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully().sync();
            work.shutdownGracefully().sync();
        }
    }
}
