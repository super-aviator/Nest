package com.xqk.nest.websocket.util;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.websocket.model.Message;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * redis主要做离线消息(message:)的存储以及用户好友(user:)、群成员(group:)的缓存
 * 需要注意数据一致性，但是现在还没有能力解决这些问题
 */
public class RedisUtil {
    private static final String MESSAGE_SEPARATOR = "message:";
    private static final String USER_SEPARATOR = "user:";
    private static final String GROUP_SEPARATOR = "group:";
    private static final int MAX_STORE_TIME = 7 * 24 * 60 * 60;//消息最长存储时间为一个星期

    private Jedis jedis = new Jedis();

    /*
    消息相关
    ----------------------------------------------------------------------------------
     */

    //将消息入离线列表
    public void pushMsg(String userId, Message msg) {
        jedis.lpush(MESSAGE_SEPARATOR + userId, JSON.toJSONString(msg));
        jedis.expire(MESSAGE_SEPARATOR + userId, MAX_STORE_TIME);
    }

    public void pushMsg(String userId, String msg) {
        jedis.lpush(MESSAGE_SEPARATOR + userId, msg);
    }

    //将消息出离线列表
    public String popMsg(String userId) {
        return jedis.rpop(MESSAGE_SEPARATOR + userId);
    }

    //判断是否有离线消息
    public boolean hasMsg(String userId) {
        return jedis.llen(MESSAGE_SEPARATOR + userId) != 0;
    }

    /*
    好友相关
    ----------------------------------------------------------------------------------
     */

    //查找用户userId的所有好友
    public Set<String> getFriends(String userId) {
        return jedis.smembers(USER_SEPARATOR + userId);
    }

    //移除用户userId1中的userId2好友
    public void remove(String userId1, String userId2) {
        if (isFriend(userId1, userId2)) {//移除好友是双向的
            jedis.srem(USER_SEPARATOR + userId1, userId2);
            jedis.srem(USER_SEPARATOR + userId2, userId1);
        }
    }

    //判断userId1和userId2是否是朋友关系
    private boolean isFriend(String userId1, String userId2) {
        return jedis.sismember(USER_SEPARATOR + userId1, userId2)
                && jedis.sismember(USER_SEPARATOR + userId2, userId1);
    }

    //增加好友
    private void addFriend(String userId1, String userId2) {
        if (!isFriend(userId1, userId2)) {//增加好友是双向的
            jedis.sadd(USER_SEPARATOR + userId1, userId2);
            jedis.sadd(USER_SEPARATOR + userId2, userId1);
        }
    }

    /*
    群相关
    ----------------------------------------------------------------------------------
     */

    //查找群的所有成员
    public Set<String> getMembers(String groupId) {
        return jedis.smembers(GROUP_SEPARATOR + groupId);
    }

    //将用户加入群聊
    public void addMemebers(String groupId, String userId) {
        if (jedis.sismember(GROUP_SEPARATOR + groupId, userId))
            jedis.sadd(GROUP_SEPARATOR + groupId, userId);
    }

    //将用户移除群聊
    public long removeMembers(String groupId, String userId) {
        if (jedis.sismember(GROUP_SEPARATOR + groupId, userId))
            return jedis.srem(GROUP_SEPARATOR + groupId, userId);
        return -1;
    }

    //解散群，删除群所有成员
    public long dissolutionGroup(String groupId) {
        return jedis.del(GROUP_SEPARATOR + groupId);
    }
}
