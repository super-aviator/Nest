package com.xqk.nest.dto;

import com.xqk.nest.model.CommonReturnModel;
import com.xqk.nest.model.UploadImageModel;
import com.xqk.nest.websocket.model.HistoryChatMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MessageDTO {
    CommonReturnModel<List<HistoryChatMessage>> getPagingMessage(long id, long revId, String type);
    void storeMessage(HistoryChatMessage item) ;
    CommonReturnModel<UploadImageModel> uploadImage(MultipartFile image) throws IOException;
}
