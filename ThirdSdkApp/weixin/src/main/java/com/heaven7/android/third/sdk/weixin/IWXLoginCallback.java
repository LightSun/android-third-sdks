package com.heaven7.android.third.sdk.weixin;

import com.heaven7.android.third.sdk.bean.WechatLoginData;

public interface IWXLoginCallback {

    void onLoginSuccess(WechatLoginData data);

    void onLoginFailed(int code, Exception e);
}

