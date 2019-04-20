package com.xqk.nest.websocket.test;

import redis.clients.jedis.Jedis;

public class RedisStart {
    public static void main(String[] args) {

        Jedis jedis =new Jedis();
        jedis.lpush("10001","ehllo");
        System.out.println(jedis.llen("10001"));
        System.out.println(jedis.rpop("10001"));
        System.out.println(jedis.llen("10001"));
    }
}
