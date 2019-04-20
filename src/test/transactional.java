package com.xqk.nest.websocket.test;

import com.xqk.nest.util.RedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class transactional {
    public static void main(String[] args) throws InterruptedException {
        Jedis jedis= RedisPool.getJedis();
        jedis.publish("10001","hello");

    }
}

class Suber extends JedisPubSub{
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("get a dto:"+message);
    }
}