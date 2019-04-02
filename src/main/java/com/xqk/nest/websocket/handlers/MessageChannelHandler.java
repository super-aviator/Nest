package com.xqk.nest.websocket.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xqk.nest.websocket.message.ChatMessage;
import com.xqk.nest.websocket.message.Message;
import com.xqk.nest.websocket.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.regex.Pattern;

public class MessageChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Pattern SignInPattern = Pattern.compile("\\d+");

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String content = msg.text();
        Message<ChatMessage> message = JSON.parseObject(content, new TypeReference<Message<ChatMessage>>() {
        });

        System.out.println(JSON.toJSONString(message.getData()));
        String id = message.getData().getId();
        MessageUtil.sendMsgToUser(SignChannelHandler.channels, id,JSON.toJSONString(message.getData()));
    }

    @Override
    public boolean acceptInboundMessage(Object msg) {
        String id = ((TextWebSocketFrame) msg).text();
        return !SignInPattern.matcher(id).matches();//判断是否是登陆发送的ID，如果是，则不处理
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("发生异常");
    }
}