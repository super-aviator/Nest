package com.xqk.nest.websocket.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xqk.nest.dao.MessageDAO;
import com.xqk.nest.dao.UserDAO;
import com.xqk.nest.model.NotifyMsg;
import com.xqk.nest.model.NotifyMsgResult;
import com.xqk.nest.websocket.model.ChatMessage;
import com.xqk.nest.websocket.model.HistoryChatMessage;
import com.xqk.nest.websocket.model.Message;
import com.xqk.nest.websocket.model.StatusMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class MessageUtil {
    private RedisUtil ru = new RedisUtil();//离线消息获取工具类
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();//开一个线程去来存储消息。
    private MessageDAO messageDAO = new MessageDAO();//消息存储DAO类
    private UserDAO userOptDAO = new UserDAO();

    /**
     * 消息分发
     * ctx.write是从当前Handler发送消息，而channel.write是从整个pipeline发送消息
     * 发送给前端的消息都是Message对象，而不是ChatMessage对象，前端根据消息类型去处理，例如添加好友、接收消息等
     * 内部接口(private)参数需要很详细，外部接口(public)参数需要很整洁，即类库能做的事情尽量不交给用户做
     */
    public void sendMsg(Map<String, Channel> channels, String messageStr) {
        Message message = JSONObject.parseObject(messageStr, Message.class);//转换为Message对象

        if ("chat".equals(message.getEmit())) {//如果是聊天类型的消息
            ChatMessage chatMessage = JSONObject.parseObject(JSON.toJSONString(message.getData()), ChatMessage.class);
            chatMessage.setMine(false);
            storeMsg(chatMessage, chatMessage.getId(), chatMessage.getFromid());//存储群聊天消息
            if ("friend".equals(chatMessage.getType())) //如果是好友消息
                sendChatMsgToFriend(channels, chatMessage);
            else if ("group".equals(chatMessage.getType())) //如果是群发消息
                sendMsgToMember(channels, chatMessage);
        } else if ("notify".equals(message.getEmit())) {//如果是提示类型的消息
            NotifyMsg msg = JSONObject.parseObject(JSON.toJSONString(message.getData()), NotifyMsg.class);
            storeNotifyMsg(channels, msg);//提示类消息不需要主动推送，只需要向用户发送提示消息的数目
        } else if ("changeStatus".equals(message.getEmit())) {//如果是用户状态修改
            StatusMessage statusMessage = JSONObject.toJavaObject((JSON) message.getData(), StatusMessage.class);//转换聊天消息对象
            sendStatusToFriend(channels, statusMessage);
        }
    }

    /**
     * 将聊天消息推送到用户，ID需要从chatMessage中获取
     *
     * @param channels
     * @param chatMessage
     */
    @SuppressWarnings("unchecked")
    private void sendChatMsgToFriend(Map<String, Channel> channels, ChatMessage chatMessage) {
        String msg = JSON.toJSONString(new Message(chatMessage, "chat"));
        String revId = chatMessage.getId();
        if (channels.containsKey(revId)) //查找id是否在线
            channels.get(revId).writeAndFlush(new TextWebSocketFrame(msg));//在线的话直接发送
        else
            ru.pushChatMsg(revId, msg);//不在线则放入离线消息列表中

    }

    /**
     * 将群消息推送到群成员
     * 这里需要将id和fromId交换一下
     *
     * @param channels
     * @param chatMessage
     */
    @SuppressWarnings("unchecked")
    private void sendMsgToMember(Map<String, Channel> channels, ChatMessage chatMessage) {
        String groupID = chatMessage.getId();
        String sendId = chatMessage.getFromid();
        chatMessage.setFromid(chatMessage.getId());

        String msg = JSON.toJSONString(new Message(chatMessage, "chat"));
        for (String id : ru.getMembers(groupID)) {//获取群成员的id列表
            if (id.equals(sendId)) continue;//如果是消息的发送者，信息不显示
            if (channels.containsKey(id))
                channels.get(id).writeAndFlush(new TextWebSocketFrame(msg));//如果在线，则直接发送chatMessage消息
            else
                ru.pushChatMsg(id, msg);//否则放入用户的离线消息列表中
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
            if (channels.containsKey(id))//确保用户还在线
                ctx.writeAndFlush(new TextWebSocketFrame(ru.popMsg(id)));//将离线消息取出并推送
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
    private void storeMsg(ChatMessage chatMessage, String id, String fromId) {
        EXECUTOR.execute(() -> {
            HistoryChatMessage historyChatMessage = new HistoryChatMessage(chatMessage.getUsername(), chatMessage.getAvatar(), fromId, id, chatMessage.getContent(), chatMessage.getTimestamp(), chatMessage.getType());
            messageDAO.storeMessage(historyChatMessage);//调用dao层去存储消息
        });

    }

    /**
     * 查询用户所有好友，给每一个好友好友发送消息，显示用户状态
     *
     * @param channels      用户channel的Map
     * @param statusMessage 状态信息
     */
    private void sendStatusToFriend(Map<String, Channel> channels, StatusMessage statusMessage) {
        String sendId = statusMessage.getId();
        for (String friendId : ru.getMembers(sendId)) {
            if (friendId.equals(sendId)) continue;//如果是状态的发送者，则不发送
            if (channels.containsKey(sendId)) {//只给在线的用户发送状态信息
                String msg = JSON.toJSONString(new Message<>(statusMessage, "changeStatus"));
                channels.get(sendId).writeAndFlush(new TextWebSocketFrame(msg));//如果在线，则直接发送chatMessage消息
            }
        }
    }

    /**
     * 将提示类的消息存储到离线消息列表中，然后发送用户提示类消息的数目，消息由用户主动获取
     * 提示消息需要一直保存。
     */
    public void storeNotifyMsg(Map<String, Channel> channels, NotifyMsg notifyMsg) {
        String uid = String.valueOf(notifyMsg.getUid());
        String msg = JSON.toJSONString(notifyMsg);
        if (channels.containsKey(uid)) { //查找id是否在线,在线的话提示用户有一个提示消息
            Message<Long> msgNum = new Message<>(Long.parseLong("1"), "notify");//保存的是用户提示消息的数目
            channels.get(uid).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgNum)));//在线的话直接发送消息数目
        }
        ru.pushNotifyMsg(uid, msg);//直接将消息放入离线消息列表中,不管是否在线
    }

    /**
     * 当用户id登陆时，获取提示消息的数量
     */
    public void getNotifyMsgNum(Map<String, Channel> channels, String id) {
        if (channels.containsKey(id)) { //查找id是否在线
            System.out.println(ru.getNotifyMsgNum(id));
            Message<Long> msgNum = new Message<>(ru.getNotifyMsgNum(id), "notify");//保存的是用户提示消息的数目
            channels.get(id).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgNum)));//在线的话直接发送消息数目
        }
    }

    /**
     * 获取用户id所有的提示消息,并通过http发送到msgBox.html
     */
    public String getNotifyMsg(String id) {
        NotifyMsgResult result = new NotifyMsgResult();
        ArrayList<NotifyMsg> list = new ArrayList<>();
        while (ru.hasNotifyMsg(id)) {
            list.add(JSONObject.parseObject(ru.popNotifyMsg(id), NotifyMsg.class));
        }
        result.setData(list);
        return JSON.toJSONString(result);
    }
}