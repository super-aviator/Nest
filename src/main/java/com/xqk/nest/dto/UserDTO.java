package com.xqk.nest.dto;

import java.io.IOException;

public interface UserDTO {
    String getUserInfo(long userId) throws IOException;
    void changeUserStatus(long userId, String status);
    void changeUserSign(long userId, String sign);
}
