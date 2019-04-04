package com.xqk.nest.websocket.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xqk.nest.dao.MessageDAO;
import com.xqk.nest.dao.UserDAO;
import com.xqk.nest.websocket.model.ChatMessage;
import com.xqk.nest.websocket.model.HistoryChatMessage;
import com.xqk.nest.websocket.model.Message;
import com.xqk.nest.websocket.model.StatusMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class MessageUtil {
    private RedisUtil ru = new RedisUtil();//离线消息获取工具类
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();//开一个线程去来存储消息。
    private MessageDAO messageDAO = new MessageDAO();//消息存储DAO类
    private UserDAO userOptDAO = new UserDAO();

    /**
     * 消息分发
     * ctx.write是从当前Handler发送消息，而channel.write是从整个pipeline发送消息
     * 发送给前端的消息都是Message对象，而不是ChatMessage对象，前端根据消息类型去处理，例如添加好友、接收消息等
     * 内部接口(private)参数需要很详细，外部接口(public)参数需要很整洁，即类库能做的事情尽量不交给用户做
     */
    public void sendMsg(Map<String, Channel> channels, String messageStr) {
        Message message = JSON.parseObject(messageStr, new TypeReference<Message>() {
        });//转换为Message对象

        if ("chat".equals(message.getEmit())) {//如果是聊天类型的消息
            ChatMessage chatMessage = JSONObject.toJavaObject((JSON) message.getData(), ChatMessage.class);//转换聊天消息对象
            chatMessage.setMine(false);
            if ("friend".equals(chatMessage.getType())) //如果是好友消息
                sendChatMsgToFriend(channels, message, chatMessage);
            else if ("group".equals(chatMessage.getType())) //如果是群发消息
                sendMsgToMember(channels, message, chatMessage);//formId是群ID,sendId是发送者ID
//            storeMsg(chatMessage);//存储群聊天消息
        } else if ("notify".equals(message.getEmit())) {//如果是提示类型的消息
            //
            System.out.println();
        } else if ("changeStatus".equals(message.getEmit())) {//如果是用户状态修改
            StatusMessage statusMessage = JSONObject.toJavaObject((JSON) message.getData(), StatusMessage.class);//转换聊天消息对象
            sendStatusToFriend(channels, message, statusMessage);
        }
    }

    /**
     * 将聊天消息推送到用户，ID需要从chatMessage中获取
     *
     * @param channels
     * @param message
     * @param chatMessage
     */
    private void sendChatMsgToFriend(Map<String, Channel> channels, Message message, ChatMessage chatMessage) {
        String msg = JSON.toJSONString(message);
        String revId = chatMessage.getId();
        if (channels.containsKey(revId)) //查找id是否在线
            channels.get(revId).writeAndFlush(new TextWebSocketFrame(msg));//在线的话直接发送
        else
            ru.pushMsg(revId, msg);//不在线则放入离线消息列表中

    }

    /**
     * 将群消息推送到群成员
     *
     * @param channels
     * @param message
     * @param chatMessage
     */
    private void sendMsgToMember(Map<String, Channel> channels, Message message, ChatMessage chatMessage) {
        String groupID = chatMessage.getId();
        String sendId = chatMessage.getFromid();

        for (String id : ru.getMembers(groupID)) {//获取群成员的id列表
            if (id.equals(sendId)) continue;//发送者，信息不显示
            if (channels.containsKey(id)) {
                String messageStr = JSON.toJSONString(message);
                channels.get(id).writeAndFlush(new TextWebSocketFrame(messageStr));//如果在线，则直接发送chatMessage消息
            } else
                ru.pushMsg(id, message);//否则放入用户的离线消息列表中
        }
    }

    /**
     * 获取用户离线消息队列中的消息
     *
     * @param ctx
     * @param channels
     * @param id
     */
    public void sendOfflineMsgToUser(ChannelHandlerContext ctx, Map<String, Channel> channels, String id) {
        while (ru.hasMsg(id)) {//查询用户的离线消息
            if (channels.containsKey(id))//确保推送离线消息时不掉线
                ctx.writeAndFlush(new TextWebSocketFrame(ru.popMsg(id)));//将离线消息取出并推送----------------------
            else
                return;//如果推送过程中掉线，则直接退出推送，
        }
    }

    /**
     * 存储聊天消息
     * 好友消息和群消息放在不同的表中进行存储,只会存储聊天类消息（ChatMessage）
     *
     * @param chatMessage 存放消息的对象
     */
    private void storeMsg(ChatMessage chatMessage) {
        EXECUTOR.execute(() -> {
            HistoryChatMessage historyChatMessage=new HistoryChatMessage(chatMessage.getUsername(),chatMessage.getAvatar(),chatMessage.getId(),chatMessage.getContent(),chatMessage.getTimestamp());
            messageDAO.storeMessage(historyChatMessage);//调用dao层去存储消息
        });
    }

    /**
     * 查询用户所有好友，给每一个好友好友发送消息提示用户状态
     *
     * @param channels      用户channel的Map
     * @param message       要推送的状态消息
     * @param statusMessage 状态信息
     */
    private void sendStatusToFriend(Map<String, Channel> channels, Message message, StatusMessage statusMessage) {
        String sendId = statusMessage.getId();
        for (String frendId : ru.getMembers(sendId)) {
            if (frendId.equals(sendId)) continue;//是发送者，信息不发送
            if (channels.containsKey(sendId)) {//只给在线的用户发送状态信息
                String messageStr = JSON.toJSONString(message);
                channels.get(sendId).writeAndFlush(new TextWebSocketFrame(messageStr));//如果在线，则直接发送chatMessage消息
            }
        }
    }
}