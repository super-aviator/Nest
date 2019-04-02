package com.xqk.nest.websocket.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class GroupUtil {

    private static void sendMessage(Jedis jedis,String groupID,String message){
        jedis.publish(groupID,message);
    }

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis=RedisUtil.getJedis();



    }
}

class Publisher extends Thread{
    @Override
    public void run() {
        Jedis jedis=new Jedis();
        jedis.publish("10001","Hello World!");
        jedis.publish("10001","Hello World!");
        jedis.publish("10001","Hello World!");
        jedis.publish("10001","Hello World!");
    }
}



class Subber extends JedisPubSub{
    public Subber() {
        super();
    }

    @Override
    public void onMessage(String channel, String message) {
        System.out.println(message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("onSubscribe");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("channel is unsubscribed");
    }

    @Override
    public void subscribe(String... channels) {
        System.out.println("subscribe");
    }
}
