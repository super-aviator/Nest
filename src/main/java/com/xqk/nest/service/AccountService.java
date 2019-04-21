package com.xqk.nest.service;

import com.xqk.nest.dto.CommonReturnDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {
    int pwCorrect(String username, String password);
    long getUserId(String username);
    CommonReturnDTO<Integer> signUp(String username, String password, MultipartFile img, String sign);
    boolean hasSingUp(String username);
}
