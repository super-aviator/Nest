package com.xqk.nest.websocket.test;

import com.xqk.nest.websocket.util.RedisPool;
import redis.clients.jedis.Jedis;

import java.util.*;

public class ArticleOpt {
    private static final long ONE_WEEK_ON_SECONDS = 7 * 86400;
    private static final long VOTE_BASE = 432;

    public String postArticle(Jedis conn, String title, String link, String poster) {
        long time = System.currentTimeMillis() / 1000;

        String id = "article:" + conn.incr("article:");//获取文章id,字符串类型自增得到。
        conn.hset(id, "title", title);
        conn.hset(id, "link", link);
        conn.hset(id, "poster", poster);
        conn.hset(id, "time", String.valueOf(time));
        conn.hset(id, "votes", String.valueOf(1));//票数默认为一票

        conn.zadd("time:", time, id);//有序集合time:中为每一篇文章增加一个时间记录。
        conn.zadd("score:", VOTE_BASE, id);//在有序集合score:中为每一篇文章增加一个票数。

        System.out.println(id);
        return id;
    }

    public void voteTO(Jedis conn, long articleID, String voterID) {
        long timeNow = System.currentTimeMillis() / 1000;
        long timeArt = Long.parseLong(conn.hget("article:" + articleID, "time"));
        if (timeNow - timeArt > ONE_WEEK_ON_SECONDS) return;

        String id = "article:" + articleID;
        if (conn.sadd("voted:"+articleID, "user:" + voterID) == 1) {//在集合article:中判断用户是否投票过，如果没有，则增加文章投票数
            conn.zincrby("score:", VOTE_BASE, id);//增加文章评分
            conn.hincrBy(id, "votes", 1);
        }

    }

    public Set<String> getArticlesID(Jedis conn) {
        return conn.zrevrange("score:", 0, 2);//取五篇评分最高的文章
    }

    public List<Map<String, String>> getArticleDetail(Jedis conn, Set<String> idList) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (String id : idList) {
            Map<String,String> map=conn.hgetAll(id);
            map.put("id",id);
            list.add(map);
        }

        return list;
    }

    public void printArticleDetail(List<Map<String, String>> list) {
        Collections.sort(list, new Comparator<Map<String, String>>() {
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                String id1=o1.get("votes");
                String id2=o2.get("votes");
                return id2.compareTo(id1);
            }
        });

        for (Map map : list) {
            System.out.println("----------------------------");
            System.out.println(map.get("id"));
            System.out.println("title:" + map.get("title"));
            System.out.println("link:" + map.get("link"));
            System.out.println("poster:" + map.get("poster"));
            System.out.println("time:" + map.get("time"));
            System.out.println("votes:" + map.get("votes"));
            System.out.println("----------------------------");
        }
    }

    public static void main(String[] args) {
        ArticleOpt opt = new ArticleOpt();
        Jedis jedis= RedisPool.getJedis();
        jedis.set("article:","10000");
        opt.postArticle(jedis,"今天是个好日子", "http://www.baidu.com1","user:123");
        opt.postArticle(jedis,"好好写论文吧思密达", "http://www.baidu.com2","user:124");
        opt.postArticle(jedis,"想去吃午饭去", "http://www.baidu.com3","user:125");
        opt.postArticle(jedis,"看我养的猫咪", "http://www.baidu.com4","user:126");
        opt.postArticle(jedis,"还没找打工作，心累", "http://www.baidu.com5","user:127");
        opt.postArticle(jedis,"我爱加班，加班是我又白又胖", "http://www.baidu.com6","user:128");
        opt.postArticle(jedis,"楼上的都是傻逼", "http://www.baidu.com7","user:129");

        opt.voteTO(jedis,10001,"130");
        opt.voteTO(jedis,10001,"131");
        opt.voteTO(jedis,10002,"132");
        opt.voteTO(jedis,10003,"133");
        opt.voteTO(jedis,10002,"134");
        opt.voteTO(jedis,10005,"135");
        opt.voteTO(jedis,10006,"136");
        opt.voteTO(jedis,10007,"137");
        opt.voteTO(jedis,10001,"138");
        opt.voteTO(jedis,10007,"139");
        opt.voteTO(jedis,10007,"140");
        opt.voteTO(jedis,10007,"141");
        opt.voteTO(jedis,10007,"142");
        opt.voteTO(jedis,10001,"143");
        opt.voteTO(jedis,10002,"144");

        opt.printArticleDetail(opt.getArticleDetail(jedis,opt.getArticlesID(jedis)));
    }
}
