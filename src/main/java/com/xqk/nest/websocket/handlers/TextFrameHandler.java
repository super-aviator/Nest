package com.xqk.nest.websocket.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class TextFrameHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String content = ((TextWebSocketFrame)msg).text();
        System.out.println("server receive : " + content);
        ctx.writeAndFlush(new TextWebSocketFrame(""));
    }
}