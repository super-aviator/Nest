package com.xqk.nest.websocket.message;


import com.xqk.nest.websocket.util.RedisUtil;

public class Test {
    public static void main(String[] args) {
        Message<ChatMessage> message=new Message<>();
        message.setEmit("chat");
        message.setData(new ChatMessage("熊乾坤","http://sf","10001","friend","在吗？",true,"10001"));
//        System.out.println(JSON.toJSONString(message));
        RedisUtil mop=new RedisUtil();
        mop.push("10001",message);
        mop.push("10001",message);
        System.out.println(mop.pop("10001"));
    }
}
