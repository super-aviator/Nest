package com.xqk.nest.websocket.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool pool;
    private static String IP="127.0.0.1";
    private static int PORT=6379;
    private static int MAX_ACTIVE = 1024;
    private static int MAX_IDLE = 200;
    private static int TIMEOUT = 10000;
    private static boolean TEST_ON_BORROW = true;

    static{
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWaitMillis(TIMEOUT);
        config.setTestOnBorrow(TEST_ON_BORROW);
        config.setMaxTotal(MAX_ACTIVE);
        pool=new JedisPool(config,IP,PORT);
    }

    public synchronized static Jedis getJedis(){
        if(pool!=null)
            return pool.getResource();
        return null;
    }
}
