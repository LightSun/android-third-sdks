package com.heaven7.android.third.sdk.weixin;

import android.content.Context;
import android.graphics.Bitmap;

import com.heaven7.android.third.sdk.Config;
import com.heaven7.android.third.sdk.NetworkApi;
import com.heaven7.android.third.sdk.bean.WechatLoginData;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

public final class WeiXinHelper {

    private IWXAPI mWxApi;
    private NetworkApi mNetworkApi;

    private IWXShareCallback mShareCallback;
    private IWXLoginCallback mLoginCallback;

    private static class Creator{
        static final WeiXinHelper INSTANCE = new WeiXinHelper();
    }

    public static WeiXinHelper get(Context context){
        return Creator.INSTANCE.initWXApi(context);
    }
    public static WeiXinHelper get(){
        return Creator.INSTANCE;
    }
    private WeiXinHelper initWXApi(Context context){
        if(mWxApi == null){
            mWxApi = WXAPIFactory.createWXAPI(context.getApplicationContext(),
                    Config.WECHAT_APP_ID, true);
            mWxApi.registerApp(Config.WECHAT_APP_ID);
        }
        return this;
    }
    public void setNetworkApi(NetworkApi networkApi) {
        this.mNetworkApi = networkApi;
    }
    public NetworkApi getNetworkApi() {
        return mNetworkApi;
    }
    public IWXAPI getWxApi(){
        return mWxApi;
    }

    public boolean login(IWXLoginCallback callback){
        this.mLoginCallback = callback;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = buildTransaction(null);
        getWxApi().sendReq(req);
        return true;
    }
    public void shareWebUrlWithIcon(Bitmap icon, String shareUrl, String title, String desc,
                                    boolean shareToFriendCircle, IWXShareCallback callback) {
        final String id = buildTransaction("web_url");
        WXWebpageObject webObj = new WXWebpageObject();
        webObj.webpageUrl = shareUrl;
        webObj.extInfo = "this is extInfo";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webObj;
        msg.title = title;
        msg.description = desc;
        msg.setThumbImage(icon);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = id;
        req.message = msg;
        req.scene = shareToFriendCircle ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;

        mShareCallback = callback;
        mWxApi.sendReq(req);
    }

    /*private*/ void requestToken(SendAuth.Resp response) {
        String code = response.code;
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", Config.WECHAT_APP_ID);
        map.put("secret", Config.WECHAT_APP_SECRET);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        getNetworkApi().login(Config.WECHAT_AUTH_URL, map, new IWXLoginCallback() {
            @Override
            public void onLoginSuccess(WechatLoginData data) {
                 dispatchLoginSuccess(data);
            }
            @Override
            public void onLoginFailed(int code, Exception e) {
                dispatchLoginFailed(code, e);
            }
        });
    }
    private void dispatchLoginFailed(int code, Exception e) {
        if(mLoginCallback != null){
            mLoginCallback.onLoginFailed(code, e);
            mLoginCallback = null;
        }
    }
    private void dispatchLoginSuccess(WechatLoginData data) {
        if(mLoginCallback != null){
            mLoginCallback.onLoginSuccess(data);
            mLoginCallback = null;
        }
    }

    /*public*/ void onShareSuccess() {
        if(mShareCallback != null){
            mShareCallback.onShareSuccess();
            mShareCallback = null;
        }
    }
    /*public*/ void onShareFailed(String errStr) {
        if(mShareCallback != null){
            mShareCallback.onShareFailed(errStr);
            mShareCallback = null;
        }
    }
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
