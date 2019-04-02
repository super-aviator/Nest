package com.xqk.nest.websocket.handlers;

import com.xqk.nest.websocket.util.MessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

//拦截id
public class SignChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    public static final ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private Pattern SignInPattern = Pattern.compile("\\d+");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame s) throws Exception {
        String id = s.text();
        Channel channel = ctx.channel();
        if (SignInPattern.matcher(id).matches()) {//判断是否是登陆消息
            if (channels.containsKey(id)) {//查看是否已登陆
                channels.get(id).close();//关闭以前登陆保存的channel
                channels.put(id, channel);//放入新的channel
                return;
            }

            channels.put(id, channel);
            System.out.println(id + "---已登陆");
            MessageUtil.sendOfflineMsgToUser(ctx,id);//发送离线消息到对应的ctx
        }
    }

    //过滤消息，如果发送的是ID，则处理，否则交给下一个ChannelHandler处理
    @Override
    public boolean acceptInboundMessage(Object msg) {
        String id = ((TextWebSocketFrame) msg).text();
        if (SignInPattern.matcher(id).matches())
            return true;
        return false;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        removeIDAndChannel(ctx);
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        removeIDAndChannel(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        removeIDAndChannel(ctx);
        super.channelInactive(ctx);
    }

    //从哈希表中移除指定的channel。
    private void removeIDAndChannel(ChannelHandlerContext ctx) {
        for (String id : channels.keySet()) {
            Channel channel = channels.get(id);
            if (ctx.channel() == channel) {
                channels.remove(id);
                System.out.println(id + "---已登出");
            }
        }
    }

}