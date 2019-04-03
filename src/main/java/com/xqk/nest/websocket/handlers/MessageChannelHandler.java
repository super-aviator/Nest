package com.xqk.nest.websocket.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xqk.nest.websocket.model.ChatMessage;
import com.xqk.nest.websocket.model.Message;
import com.xqk.nest.websocket.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.regex.Pattern;

public class MessageChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private Pattern SignInPattern = Pattern.compile("\\d+");
    private MessageUtil messageUtil=new MessageUtil();

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String content = msg.text();
        Message<ChatMessage> message = JSON.parseObject(content, new TypeReference<Message<ChatMessage>>() {
        });

        String id = message.getData().getId();
        messageUtil.sendMsg(SignChannelHandler.channels, id,message);
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