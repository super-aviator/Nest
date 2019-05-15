package com.xqk.nest.service;

import com.xqk.nest.dto.CommonReturnDTO;
import com.xqk.nest.dto.UploadFileDTO;
import com.xqk.nest.dto.UploadImageDTO;
import com.xqk.nest.websocket.dto.HistoryChatMessageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MessageService {
    CommonReturnDTO<List<HistoryChatMessageDTO>> getPagingMessage(long id, long revId, String type);
    void storeMessage(HistoryChatMessageDTO item) ;
    CommonReturnDTO<UploadImageDTO> uploadImage(MultipartFile image) throws IOException;
    CommonReturnDTO<UploadFileDTO> uploadFile(MultipartFile file) throws IOException;
}
