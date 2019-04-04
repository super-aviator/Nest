package com.xqk.nest.dao;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.HistoryMsg;
import com.xqk.nest.model.Tuple;
import com.xqk.nest.websocket.model.ChatMessage;
import com.xqk.nest.websocket.model.HistoryChatMessage;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MessageDAO {

    //查询历史消息
    public String getPagingMessage(long id, String type) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            List<HistoryChatMessage> msgList = session.selectList("mapper.selectMsg", new Tuple<>(id, type));
            HistoryMsg result = new HistoryMsg(0, "", msgList);
            return JSON.toJSONString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new HistoryMsg(1, "(: 服务器错误"));
        }
    }

    //插入历史消息
    public void storeMessage(HistoryChatMessage item) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            session.insert("mapper.storeMsg", item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
