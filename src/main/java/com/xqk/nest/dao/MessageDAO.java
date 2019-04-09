package com.xqk.nest.dao;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.dto.MessageDTO;
import com.xqk.nest.model.*;
import com.xqk.nest.websocket.model.ChatMessage;
import com.xqk.nest.websocket.model.HistoryChatMessage;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 事物记得提交啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊
 */
public class MessageDAO implements MessageDTO {

    //查询历史消息
    public CommonReturnModel<List<HistoryChatMessage>> getPagingMessage(long id, long revId, String type) {
        CommonReturnModel<List<HistoryChatMessage>> result;
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            List<HistoryChatMessage> msgList = session.selectList("mapper.selectMsg", new Triple<>(id, revId, type));
            result= new CommonReturnModel<>(0, "", msgList);

        } catch (Exception e) {
            e.printStackTrace();
            result=new CommonReturnModel<>(1, "(: 服务器错误",null);
            throw e;
        }
        return result;
    }

    //插入历史消息
    public void storeMessage(HistoryChatMessage item) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            session.insert("mapper.storeMsg", item);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommonReturnModel<UploadImageMod> uploadImage(MultipartFile image) throws IOException {
        CommonReturnModel<UploadImageMod> returnMsg;
        File file = new File("E:\\Nest\\web\\WEB-INF\\Nest\\pages\\dataImg\\" + image.getOriginalFilename());
        if (!file.exists())
            image.transferTo(file);
        returnMsg = new CommonReturnModel<>(0, "success", new UploadImageMod("./dataImg/"+image.getOriginalFilename()));
        return returnMsg;
    }
}