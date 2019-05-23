package com.xqk.nest.dao;

import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.dto.*;
import com.xqk.nest.websocket.dto.HistoryChatMessageDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@PropertySource("classpath:config/application.properties")
public class MessageDAO {
    @Value("${uploadFilePath}")
    private String uploadFilePath;

    @Value("${uploadImagePath}")
    private String uploadImagePath;

    @Value("${fileURL}")
    private String fileURL;

    @Value("${imageURL}")
    private String imageURL;

    /**
     * 插入历史消息
     */
    public void storeMessage(HistoryChatMessageDTO item) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            session.insert("mapper.storeMsg", item);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询历史消息
     */
    public CommonReturnDTO<List<HistoryChatMessageDTO>> getPagingMessage(long id, long revId, String type, int start, int limit) {
        CommonReturnDTO<List<HistoryChatMessageDTO>> result;
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            List<HistoryChatMessageDTO> msgList = session.selectList("mapper.selectMsg", new HistoryMessagePagableDTO(id, revId, type, start, limit));
            result = new CommonReturnDTO<>(0, "", msgList);
        } catch (Exception e) {
            e.printStackTrace();
            result = new CommonReturnDTO<>(1, "(: 服务器错误", null);
        }
        return result;
    }

    /**
     * 文件上传接口
     */
    public CommonReturnDTO<UploadImageDTO> uploadImage(MultipartFile image) throws IOException {
        CommonReturnDTO<UploadImageDTO> returnMsg;
        File file = new File(uploadFilePath + image.getOriginalFilename());
        if (!file.exists()) {
            image.transferTo(file);
        }
        returnMsg = new CommonReturnDTO<>(0, "success", new UploadImageDTO(fileURL + image.getOriginalFilename()));
        return returnMsg;
    }

    /**
     * 图片上传接口
     */
    public CommonReturnDTO<UploadFileDTO> uploadFile(MultipartFile file) throws IOException {
        CommonReturnDTO<UploadFileDTO> returnMsg;
        File tempFile = new File(uploadImagePath + file.getOriginalFilename());
        if (!tempFile.exists()) {
            file.transferTo(tempFile);
        }
        returnMsg = new CommonReturnDTO<>(0, "success", new UploadFileDTO(imageURL + file.getOriginalFilename(), file.getOriginalFilename()));
        return returnMsg;
    }

    public int getMessageCount(long id, long revId, String type) {
        int count;
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            count = session.<Integer>selectOne("mapper.selectCount", new Triple<>(id, revId, type));
        } catch (Exception e) {
            e.printStackTrace();
            count = 0;
        }
        return count;
    }
}
