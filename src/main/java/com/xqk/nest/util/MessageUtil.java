package com.xqk.nest.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xqk.nest.dto.NotifyDTO;
import com.xqk.nest.service.Impl.MessageServiceImpl;
import com.xqk.nest.service.Impl.UserServiceImpl;
import com.xqk.nest.dto.NotifyReturnDTO;
import com.xqk.nest.websocket.dto.ChatMessageDTO;
import com.xqk.nest.websocket.dto.HistoryChatMessageDTO;
import com.xqk.nest.websocket.dto.MessageDTO;
import com.xqk.nest.websocket.dto.StatusMessageDTO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
@Component
public class MessageUtil {
    private RedisUtil ru = new RedisUtil();//离线消息获取工具类
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();//开一个线程去来存储消息。
    private MessageServiceImpl messageDAO = new MessageServiceImpl();//消息存储DAO类
    private UserServiceImpl userOptDAO = new UserServiceImpl();

    /**
     * 消息分发
     * ctx.write是从当前Handler发送消息，而channel.write是从整个pipeline发送消息
     * 发送给前端的消息都是Message对象，而不是ChatMessage对象，前端根据消息类型去处理，例如添加好友、接收消息等
     * 内部接口(private)参数需要很详细，外部接口(public)参数需要很整洁，即类库能做的事情尽量不交给用户做
     */
    public void sendMsg(Map<String, Channel> channels, String messageStr) {
        MessageDTO messageDTO = JSONObject.parseObject(messageStr, MessageDTO.class);//转换为Message对象
        sendMsg(channels,messageDTO);
    }

    public void sendMsg(Map<String, Channel> channels, MessageDTO<?> messageDTO) {
        switch (messageDTO.getEmit()) {
            case "chat"://如果是聊天类型的消息
                ChatMessageDTO chatMessageDTO = JSONObject.parseObject(JSON.toJSONString(messageDTO.getData()), ChatMessageDTO.class);
                chatMessageDTO.setMine(false);
                storeMsg(chatMessageDTO, chatMessageDTO.getId(), chatMessageDTO.getFromid());//存储群聊天消息
                if ("friend".equals(chatMessageDTO.getType())) //如果是好友消息
                    sendChatMsgToFriend(channels, chatMessageDTO);
                else if ("group".equals(chatMessageDTO.getType())) //如果是群发消息
                    sendMsgToMember(channels, chatMessageDTO);
                break;
            case "notify"://如果是提示类型的消息
                NotifyDTO msg = JSONObject.parseObject(JSON.toJSONString(messageDTO.getData()), NotifyDTO.class);
                storeNotifyMsg(channels, msg);//提示类消息不需要主动推送，只需要向用户发送提示消息的数目
                break;
            case "changeStatus"://如果是用户状态修改
                StatusMessageDTO statusMessageDTO = JSONObject.parseObject(JSON.toJSONString(messageDTO.getData()), StatusMessageDTO.class);//转换聊天消息对象
                sendStatusToFriend(channels, statusMessageDTO);
                break;
        }
    }

    /**
     * 将聊天消息推送到用户，ID需要从chatMessage中获取
     *
     * @param channels
     * @param chatMessageDTO
     */
    @SuppressWarnings("unchecked")
    private void sendChatMsgToFriend(Map<String, Channel> channels, ChatMessageDTO chatMessageDTO) {
        String msg = JSON.toJSONString(new MessageDTO(chatMessageDTO, "chat"));
        String revId = chatMessageDTO.getId();
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
     * @param chatMessageDTO
     */
    @SuppressWarnings("unchecked")
    private void sendMsgToMember(Map<String, Channel> channels, ChatMessageDTO chatMessageDTO) {
        String groupID = chatMessageDTO.getId();
        String sendId = chatMessageDTO.getFromid();
        chatMessageDTO.setFromid(chatMessageDTO.getId());

        String msg = JSON.toJSONString(new MessageDTO(chatMessageDTO, "chat"));
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
    public void getOfflineMsgToUser(ChannelHandlerContext ctx, Map<String, Channel> channels, String id) {
        while (ru.hasMsg(id)) {//查询用户的离线消息
            if (channels.containsKey(id))//确保用户还在线
                ctx.writeAndFlush(new TextWebSocketFrame(ru.popMsg(id)));//将离线消息取出并推送
            else
                return;//如果推送过程中掉线，则直接退出推送，
        }
    }

    /**
     * 存储聊天消息
     * 好友消息和群消息放在不同的表中进行存储,只会存储聊天类消息（ChatMessageDTO）
     *
     * @param chatMessageDTO 存放消息的对象
     */
    private void storeMsg(ChatMessageDTO chatMessageDTO, String id, String fromId) {
        EXECUTOR.execute(() -> {
            HistoryChatMessageDTO historyChatMessageDTO = new HistoryChatMessageDTO(chatMessageDTO.getUsername(), chatMessageDTO.getAvatar(), fromId, id, chatMessageDTO.getContent(), chatMessageDTO.getTimestamp(), chatMessageDTO.getType());
            messageDAO.storeMessage(historyChatMessageDTO);//调用dao层去存储消息
        });

    }

    /**
     * 查询用户所有好友，给每一个好友好友发送消息，显示用户状态
     *
     * @param channels      用户channel的Map
     * @param statusMessageDTO 状态信息
     */
    private void sendStatusToFriend(Map<String, Channel> channels, StatusMessageDTO statusMessageDTO) {
        String sendId = String.valueOf(statusMessageDTO.getId());
        for (String friendId : ru.getFriends(String.valueOf(sendId))) {
            if (friendId.equals(sendId)) continue;//如果是状态的发送者，则不发送
            if (channels.containsKey(friendId)) {//只给在线的用户发送状态信息
                String msg = JSON.toJSONString(new MessageDTO<>(statusMessageDTO, "changeStatus"));
                channels.get(friendId).writeAndFlush(new TextWebSocketFrame(msg));//如果在线，则直接发送chatMessage消息
            }
        }
    }

    /**
     * 将提示类的消息存储到离线消息列表中，然后发送用户提示类消息的数目，消息由用户主动获取
     * 提示消息需要一直保存。
     * @param notifyDTO 提示消息DTO
     */
    public void storeNotifyMsg(Map<String, Channel> channels, NotifyDTO notifyDTO) {
        String uid = String.valueOf(notifyDTO.getUid());
        String msg = JSON.toJSONString(notifyDTO);
        ru.storeNotifyMsg(uid, msg);//直接将消息放入离线消息列表中,不管用户是否在线
        if (channels.containsKey(uid)) { //查找id是否在线,在线的话提示用户有一个提示消息
            MessageDTO<Long> msgNum = new MessageDTO<>(ru.getNotifyMsgNum(uid), "notify");//保存的是用户提示消息的数目
            channels.get(uid).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgNum)));//在线的话直接发送消息数目
        }
    }

    /**
     * 当用户id登陆时，获取提示消息的数量
     */
    public void getNotifyMsgNum(Map<String, Channel> channels, String id) {
        if (channels.containsKey(id)) { //查找id是否在线
            MessageDTO<Long> msgNum = new MessageDTO<>(ru.getNotifyMsgNum(id), "notify");//保存的是用户提示消息的数目
            channels.get(id).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgNum)));//在线的话直接发送消息数目
        }
    }

    /**
     * 获取用户id所有的提示消息,并通过http发送到msgbox.html，
     * 如果消息的from不为0，则表示此消息是用户加好友时第一次发送的消息，而不是提示类消息，在历史集合中不进行保存。
     */
    public String getNotifyMsg(String id) {
        NotifyReturnDTO result = new NotifyReturnDTO();
        ArrayList<NotifyDTO> list = new ArrayList<>();

        for (String msg : ru.getHistoryNotifyMsg(id)) {//取已读消息
            list.add(JSONObject.parseObject(msg, NotifyDTO.class));
        }

        for (Tuple tuple : ru.popNotifyMsg(id)) {//取未读消息
            list.add(JSONObject.parseObject(tuple.getElement(), NotifyDTO.class));
            NotifyDTO model = JSONObject.parseObject(tuple.getElement(), NotifyDTO.class);
            if (model.getFrom() == 0) {//判断是否是提示类型的消息
                String notifyModelStr = JSON.toJSONString(model);
                ru.pushHistoryNotifyMsg(id, (long) tuple.getScore(), notifyModelStr);//将消息存入已读有序集合中
            }

        }
        Collections.reverse(list);

        result.setData(list);
        return JSON.toJSONString(result);
    }
}