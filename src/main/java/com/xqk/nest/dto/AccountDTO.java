package com.xqk.nest.dto;

public interface AccountDTO {
    int pwCorrect(String username, String password);
    long getUserId(String username);
    int signUp(String username,String password,String avatar,String sign);
    boolean hasSingUp(String username);
}
