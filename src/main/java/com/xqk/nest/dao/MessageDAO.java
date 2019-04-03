package com.xqk.nest.dao;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.GroupMembersMsg;
import com.xqk.nest.model.HistoryMsg;
import com.xqk.nest.model.HistoryMsgItem;
import com.xqk.nest.model.Tuple;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MessageDAO {

    //查询消息
    public String getMessage(long id, String type) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            List<HistoryMsgItem> msgList = session.selectList("mapper.selectMsg", new Tuple<>(id, type));
            HistoryMsg result = new HistoryMsg(0, "", msgList);
            return JSON.toJSONString(msgList);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new GroupMembersMsg(1, "(: 服务器错误"));
        }
    }

    //插入消息
    public void storeMessage(HistoryMsgItem item) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            session.insert("mapper.storeMsg", item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
