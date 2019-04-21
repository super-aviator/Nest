package com.xqk.nest.websocket.handlers;

import com.xqk.nest.service.UserService;
import com.xqk.nest.util.MessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 拦截登陆时传递的id信息,并且一个id每次只能有一个用户登陆
 */
@Component
@ChannelHandler.Sharable
public class SignChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    public static final ConcurrentHashMap<String, Channel> CHANNELS = new ConcurrentHashMap<>();
    private static final String HIDE ="hide";

    private static final Pattern SignInPattern = Pattern.compile("\\d+");

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private UserService userService;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame s) {
        String id = s.text();
        Channel channel = ctx.channel();
        if (CHANNELS.containsKey(id)) {//查看是否已登陆
            CHANNELS.get(id).close();//关闭以前登陆保存的channel
        }
        CHANNELS.put(id, channel);//放入新的channel
        System.out.println(id + "->已登陆");
        messageUtil.getOfflineMsgToUser(ctx, CHANNELS, id);//发送离线聊天消息到对应的ctx
        messageUtil.getNotifyMsgNum(CHANNELS, id);//获取提示消息数量
    }

    //过滤消息，如果发送的是ID，则处理，否则交给下一个ChannelHandler处理
    @Override
    public boolean acceptInboundMessage(Object msg) {
        String id = ((TextWebSocketFrame) msg).text();
        return SignInPattern.matcher(id).matches();
    }

    /**
     *
     * @param ctx
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        userService.changeUserStatus(removeIDAndChannel(ctx), HIDE);
    }

    /**
     * 连接异常时触发
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        userService.changeUserStatus(removeIDAndChannel(ctx), HIDE);
    }

//    /**
//     * channel长时间不活跃时，断开连接
//     * @param ctx
//     */
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) {
//        userService.changeUserStatus(removeIDAndChannel(ctx), HIDE);
//    }

    /**
     * 从哈希表中移除相应的channle，并返回该channel所对应的id,此id用于修改用户在线状态
     * @param ctx
     * @return
     */
    private long removeIDAndChannel(ChannelHandlerContext ctx) {
        for (String id : CHANNELS.keySet()) {
            Channel channel = CHANNELS.get(id);
            if (ctx.channel() == channel) {
                CHANNELS.remove(id);
                System.out.println(id + "->登出");
                return Long.parseLong(id);
            }
        }
        return -1L;
    }
}