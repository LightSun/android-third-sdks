package com.heaven7.android.third.sdk.bean;

public class WechatLoginData {

    private String openId;
    private String accessToken;

    public WechatLoginData(String openId, String accessToken) {
        this.openId = openId;
        this.accessToken = accessToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
