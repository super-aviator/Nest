package com.xqk.nest.util;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.NotifyDTO;
import com.xqk.nest.websocket.dto.MessageDTO;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * redis主要做离线消息(message:)的存储以及用户好友(user:)、群成员(group:)的缓存
 * 需要注意数据一致性，但是现在还没有能力解决这些问题
 */
@SuppressWarnings("unused")
@Component
public class RedisUtil {
    private static final String MESSAGE_SEPARATOR = "message:";
    private static final String USER_SEPARATOR = "user:";
    private static final String GROUP_SEPARATOR = "group:";
    private static final String NOTIFY_SEPARATOR = "notify:";
    private static final String HISTORY_NOTIFY_SEPARATOR = "history_notify:";
    private static final long A_WEEK_TIMESTAMP=3600*24*7;
    private static final int MAX_STORE_TIME = 7 * 24 * 60 * 60;//消息最长存储时间为一个星期

    private Jedis jedis = new Jedis();

    /*消息相关
    ----------------------------------------------------------------------------------
     */

    //将消息入离线列表
    public void pushChatMsg(String userId, MessageDTO msg) {
        jedis.lpush(MESSAGE_SEPARATOR + userId, JSON.toJSONString(msg));
        jedis.expire(MESSAGE_SEPARATOR + userId, MAX_STORE_TIME);
    }

    public void pushChatMsg(String userId, String msg) {
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

    /*好友相关
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
    public void addFriend(String userId1, String userId2) {
        if (!isFriend(userId1, userId2)) {//增加好友是双向的
            jedis.sadd(USER_SEPARATOR + userId1, userId2);
            jedis.sadd(USER_SEPARATOR + userId2, userId1);
        }
    }

    /*群相关
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




    /*提示消息相关
    ----------------------------------------------------------------------------------
     */

    /**
     * 存入未读提示消息,基数为当前unix时间戳
     */
    public void storeNotifyMsg(String id, String msg) {
        jedis.zadd(NOTIFY_SEPARATOR + id, System.currentTimeMillis(), msg);
    }

    /**
     * 存入未读提示消息
     */
    public void storeNotifyMsg(String id, NotifyDTO msg) {
        jedis.zadd(NOTIFY_SEPARATOR + id, System.currentTimeMillis(), JSON.toJSONString(msg));
    }

    /**
     * 获得用户未读的提示消息数目
     */
    public Long getNotifyMsgNum(String id) {
        return jedis.zcount(NOTIFY_SEPARATOR + id, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * 取出用户所有未读的消息
     */
    public Set<Tuple> popNotifyMsg(String id) {
        Set<Tuple> result=jedis.zrangeWithScores(NOTIFY_SEPARATOR+id,Long.MIN_VALUE,Long.MAX_VALUE);
        jedis.zremrangeByScore(NOTIFY_SEPARATOR+id,Long.MIN_VALUE,Long.MAX_VALUE);
        return result;
    }

    /**
     * 存入已读的提示消息
     */
    public void pushHistoryNotifyMsg(String id, long timestamp, String msg) {
        jedis.zadd(HISTORY_NOTIFY_SEPARATOR + id, timestamp, msg);
    }

    /**
     * 取出用户一个星期内的已读历史提示消息
     */
    public Set<String> getHistoryNotifyMsg(String id) {
        return jedis.zrange(HISTORY_NOTIFY_SEPARATOR+id,0,System.currentTimeMillis());
    }
}
