package com.xqk.nest.dto;

import com.xqk.nest.model.UserInfo;

import java.io.IOException;

public interface UserDTO {
    String getUserInfo(long userId) throws IOException;
    void changeUserStatus(long userId, String status);
    void changeUserSign(long userId, String sign);
    String getUserList(String username);
    void addFriend(long userId,long groupId);
    UserInfo getUser(long userId);
}
