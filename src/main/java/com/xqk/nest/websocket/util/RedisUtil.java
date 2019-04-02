package com.xqk.nest.websocket.util;

import com.alibaba.fastjson.JSON;
import com.xqk.websocket.message.Message;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class RedisUtil {
    private Jedis jedis=new Jedis();

    //将消息入离线列表
    public void push(String id, Message msg){
        jedis.lpush(id, JSON.toJSONString(msg));
    }

    public void push(String id,String msg){
        jedis.lpush(id,msg);
    }

    //将消息出离线列表
    public String pop(String id){
        return jedis.rpop(id);
    }

    //判断是否有离线消息
    public boolean hasMsg(String id){
        return jedis.llen(id)!=0;
    }

    //查找群的所有成员成员
    public Set<String> findMembers(String id) {
        return jedis.smembers(id);
    }

    //将用户加入群聊
    public void addMemebers(String groupId, String userId) {
        if (jedis.sismember(groupId, userId))
            jedis.sadd(groupId, userId);
    }

    //将用户移除群聊
    public void removeMembers(String groupId, String userId) {
        if(jedis.sismember(groupId,userId))
            jedis.srem(groupId,userId);
    }

    //解散群，删除群所有成员
    public void dissolutionGroup(String id){
        jedis.del(id);
    }
}
