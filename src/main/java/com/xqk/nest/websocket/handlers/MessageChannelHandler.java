package com.xqk.nest.websocket.handlers;

import com.xqk.nest.websocket.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.regex.Pattern;

public class MessageChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private Pattern SignInPattern = Pattern.compile("\\d+");
    private MessageUtil messageUtil = new MessageUtil();

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String messageStr = msg.text();
        messageUtil.sendMsg(SignChannelHandler.CHANNELS, messageStr);
    }

    @Override
    public boolean acceptInboundMessage(Object msg) {
        String id = ((TextWebSocketFrame) msg).text();
        return !SignInPattern.matcher(id).matches();//判断是否是登陆发送的ID消息，如果是，则不处理
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("发生异常");
    }
}