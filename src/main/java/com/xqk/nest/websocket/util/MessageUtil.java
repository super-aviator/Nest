package com.xqk.nest.websocket.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xqk.nest.dao.MessageDAO;
import com.xqk.nest.model.HistoryMsgItem;
import com.xqk.nest.websocket.model.ChatMessage;
import com.xqk.nest.websocket.model.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MessageUtil {
    private RedisUtil ru = new RedisUtil();
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();//开一个线程去来存储消息。
    private MessageDAO DAO = new MessageDAO();

    /**
     * ctx.write是从当前Handler发送消息，而channel.write是从整个pipeline发送消息
     */
    //给好友或者群推送消息
    public void sendMsg(Map<String, Channel> channels, Message<ChatMessage> message) {
        ChatMessage msg =message.getData();
        String fromId = msg.getFromid();//接受者id/群id
        String sendId =msg.getId();//发送者id
        msg.setMine(false);

        if ("chat".equals(message.getEmit())) {//如果是聊天类的消息
            if ("friend".equals(msg.getType())) {//如果是好友消息
                sendMsgToUser(fromId, channels, message);
                //storeMsg(message);//存储好友聊天消息
            } else if ("group".equals(msg.getType())) {//如果是群发消息
                sendMsgToMember(sendId, fromId, channels, message);//formId是群ID,sendId是发送者ID
                //storeMsg(message);//存储群聊天消息
            }
        }
    }

    //将消息推送到用户
    private void sendMsgToUser(String revId, Map<String, Channel> channels, Message message) {
        String msg = JSON.toJSONString(message);
        if (channels.containsKey(revId)) {//查找id是否在线
            channels.get(revId).writeAndFlush(new TextWebSocketFrame(msg));//在线的话直接发送
        } else {
            ru.pushMsg(revId, message);//不在线则放入离线消息列表中
        }
    }

    //将群发消息推送到群成员
    private void sendMsgToMember(String sendId, String groupID, Map<String, Channel> channels, Message<ChatMessage> message) {
        ChatMessage cmsg =message.getData();
        cmsg.setId(groupID);//将接受方id存入
        String msg = JSON.toJSONString(message);//将消息序列化

        for (String id : ru.getMembers(groupID)) {//获取群成员的id列表
            if (id.equals(sendId)) continue;//发送者，信息不显示
            System.out.println("send group message to" + id);
            if (channels.containsKey(id)) {
                sendMsgToUser(id, channels, message);//如果在线，则直接发送消息
            } else
                ru.pushMsg(id, msg);//否则放入用户的离线消息列表中
        }
    }

    //获取用户离线消息队列的消息
    public void sendOfflineMsgToUser(ChannelHandlerContext ctx, String id) {
        while (ru.hasMsg(id)) {//查询用户的离线消息
            ctx.writeAndFlush(new TextWebSocketFrame(ru.popMsg(id)));//将离线消息取出并推送
        }
    }

    private void storeMsg(Message<ChatMessage> message) {//好友消息和群消息放在不同的表中进行存储
        EXECUTOR.execute(() -> {
            ChatMessage msg =message.getData();
            HistoryMsgItem item = new HistoryMsgItem(msg.getUsername(), Long.parseLong(msg.getFromid()), Long.parseLong(msg.getId()), msg.getAvatar(), msg.getTimestamp(), msg.getContent(), msg.getType(), 0, 10);
            DAO.storeMessage(item);//调用dao层去存储消息
        });
    }
}