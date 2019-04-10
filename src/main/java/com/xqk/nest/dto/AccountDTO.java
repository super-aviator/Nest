package com.xqk.nest.dto;

import com.xqk.nest.model.CommonReturnModel;
import com.xqk.nest.model.Tuple;
import org.springframework.web.multipart.MultipartFile;

public interface AccountDTO {
    int pwCorrect(String username, String password);
    long getUserId(String username);
    CommonReturnModel<Integer> signUp(String username, String password, MultipartFile img, String sign);
    boolean hasSingUp(String username);
}
