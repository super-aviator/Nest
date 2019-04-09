package com.xqk.nest.dto;

import com.xqk.nest.model.GroupInfo;
import com.xqk.nest.model.Tuple;
import com.xqk.nest.model.UserInfo;
import com.xqk.nest.model.UserInfoMsg;

import java.io.IOException;

public interface UserDTO {
    UserInfoMsg getUserInfo(long userId) throws IOException;
    void changeUserStatus(long userId, String status);
    void changeUserSign(long userId, String sign);
    Tuple<UserInfo, GroupInfo> getUserList(String username);
    void addFriend(long userId,long groupId);
    UserInfo getUser(long userId);
}
