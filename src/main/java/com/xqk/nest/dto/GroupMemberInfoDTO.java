package com.xqk.nest.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class GroupMemberInfoDTO {
        @JSONField(ordinal = 1)
        private String username;

        @JSONField(ordinal = 2)
        private Long id;

        @JSONField(ordinal = 3)
        private String avatar;

        @JSONField(ordinal = 4)
        private String sign;

        public GroupMemberInfoDTO(Long id, String username, String avatar, String sign) {
            this.username = username;
            this.id = id;
            this.sign = sign;
            this.avatar = avatar;
        }

    public GroupMemberInfoDTO() {
    }

    public String getUsername() {
            return username;
        }

        public Long getId() {
            return id;
        }

        public String getSign() {
            return sign;
        }


        public String getAvatar() {
            return avatar;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }


        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        @Override
        public String toString() {
            return "UserInfoDTO{" +
                    "username='" + username + '\'' +
                    ", id=" + id +
                    ", sign='" + sign + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }

}
