package com.xqk.nest.service;

import com.xqk.nest.dto.*;

import java.io.IOException;

public interface UserService {
    CommonReturnDTO<DataDTO> getUserInfo(long userId) throws IOException;
    void changeUserStatus(long userId, String status);
    void changeUserSign(long userId, String sign);
    Tuple<UserInfoDTO, GroupInfoDTO> getUserList(String username);

    AddFriendDTO addFriend(long id, long uid, long from_group, long group);
    UserInfoDTO getUser(long userId);
    void sendAddFriendMessage( long id, long friendId, long groupId, String remark);
}
