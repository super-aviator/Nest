package com.xqk.nest.websocket.model;


import com.xqk.nest.websocket.util.RedisUtil;

public class Test {
    public static void main(String[] args) {
        Message<ChatMessage> message=new Message<>();
        message.setEmit("chat");
        message.setData(new ChatMessage("熊乾坤","http://sf","10001","friend","Hello在吗？",true,"10001"));
//        System.out.println(JSON.toJSONString(model));
        RedisUtil mop=new RedisUtil();
        mop.pushMsg("1",message);
        mop.pushMsg("2",message);
        mop.pushMsg("3",message);
        mop.pushMsg("4",message);
    }
}
