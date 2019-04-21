package com.xqk.nest.websocket.dto;


import com.xqk.nest.util.RedisUtil;

public class Test {
    public static void main(String[] args) {
        MessageDTO<ChatMessageDTO> messageDTO =new MessageDTO<>();
        messageDTO.setEmit("chat");
        messageDTO.setData(new ChatMessageDTO("熊乾坤","http://sf","10001","friend","Hello在吗？",true,"10001"));
//        System.out.println(JSON.toJSONString(dto));
        RedisUtil mop=new RedisUtil();
        mop.pushChatMsg("1", messageDTO);
        mop.pushChatMsg("2", messageDTO);
        mop.pushChatMsg("3", messageDTO);
        mop.pushChatMsg("4", messageDTO);
    }
}
