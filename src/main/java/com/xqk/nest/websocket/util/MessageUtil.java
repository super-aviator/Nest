package com.xqk.nest.websocket.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.Set;

public class MessageUtil {
    private static final RedisUtil RU = new RedisUtil();

    /**
     * ctx是从当前Handler发送消息，而channel是从整个pipeline发送消息
     */
    //将消息推送到用户
    public static void sendMsgToUser(Map<String, Channel> channels, String userId, String message) {
        if (channels.containsKey(userId)) {//查找id是否在线
            Channel channel = channels.get(userId);
            channel.writeAndFlush(new TextWebSocketFrame(message));//在线的话直接发送
        } else {
            RU.push(userId, message);//不在线则放入离线消息列表中
        }
    }

    //将群发消息推送到群成员
    public static void sendMsgToMember(Set<String> idSet, Map<String, Channel> channels, String msg) {
        for (String id : idSet) {
            if (channels.containsKey(id)) {//如果在线，则直接发送消息
                channels.get(id).writeAndFlush(msg);
            } else
                RU.push(id, msg);//否则放入用户的离线消息列表中
        }
    }

    //获取用户离线消息队列的消息
    public static void sendOfflineMsgToUser(ChannelHandlerContext ctx,String id) {
        while (RU.hasMsg(id)) {//查询用户的离线消息
            ctx.writeAndFlush(new TextWebSocketFrame(RU.pop(id)));
        }
    }
}