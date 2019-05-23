package com.xqk.nest.service.Impl;

import com.xqk.nest.dao.MessageDAO;
import com.xqk.nest.dto.*;
import com.xqk.nest.service.MessageService;
import com.xqk.nest.websocket.dto.HistoryChatMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 事物记得提交啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊
 */
@Component
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDAO messageDAO;

    /**
     *     查询历史消息
     */
    @Override
    public CommonReturnDTO<List<HistoryChatMessageDTO>> getPagingMessage(long id, long revId, String type,int start,int limit) {
        return messageDAO.getPagingMessage(id, revId, type,start,limit);
    }

    /**
     *     插入历史消息
     */
    @Override
    public void storeMessage(HistoryChatMessageDTO item) {
        messageDAO.storeMessage(item);
    }

    @Override
    public CommonReturnDTO<UploadImageDTO> uploadImage(MultipartFile image) throws IOException {
        return messageDAO.uploadImage(image);
    }

    @Override
    public CommonReturnDTO<UploadFileDTO> uploadFile(MultipartFile file) throws IOException {
        return messageDAO.uploadFile(file);
    }

    @Override
    public int getMessageCount(long id,long revId,String type) {
        return messageDAO.getMessageCount(id,revId,type);
    }
}