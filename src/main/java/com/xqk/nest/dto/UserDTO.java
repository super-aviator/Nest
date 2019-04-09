package com.xqk.nest.dto;

import com.xqk.nest.model.*;

import java.io.IOException;

public interface UserDTO {
    CommonReturnModel<Data> getUserInfo(long userId) throws IOException;
    void changeUserStatus(long userId, String status);
    void changeUserSign(long userId, String sign);
    Tuple<UserInfo, GroupInfo> getUserList(String username);
    void addFriend(long userId,long groupId);
    UserInfo getUser(long userId);
}
