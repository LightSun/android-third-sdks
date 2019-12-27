package com.heaven7.android.third.sdk.weixin;

public interface IWXShareCallback {

    void onShareFailed(String msg);

    void onShareSuccess();
}