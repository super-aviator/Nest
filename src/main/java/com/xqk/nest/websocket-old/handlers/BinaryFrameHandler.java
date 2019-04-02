package com.xqk.nest.websocket.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class BinaryFrameHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String url = msg.toString();
        System.out.println("com.com.nest.server receive binary meg: " + url);

        ctx.writeAndFlush(new TextWebSocketFrame("你好，客户端"));
    }
}
