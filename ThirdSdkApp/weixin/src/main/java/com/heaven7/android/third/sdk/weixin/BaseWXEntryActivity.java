package com.heaven7.android.third.sdk.weixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/*
<activity
            android:name="{$pkg}.wxapi.WXEntryActivity"
                    android:exported="true"
                    android:launchMode="singleInstance"
                    android:screenOrientation="unspecified"
                    android:theme="@android:style/Theme.NoTitleBar"
                    android:windowSoftInputMode="adjustPan|adjustUnspecified|stateHidden" />
*/
public abstract class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WeiXinHelper.get(this).getWxApi();
        if(api.handleIntent(getIntent(), this)){
            finish();
        }
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp response) {
        if(response instanceof SendAuth.Resp){
            String result = "";
            switch (response.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    switch (response.getType()) {
                        case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                            //微信分享,只是做了简单的finish操作
                            break;
                        default:
                            WeiXinHelper.get(this).requestToken((SendAuth.Resp) response);
                            break;
                    }
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "发送取消";
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "发送被拒绝";
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    finish();
                    break;
                default:
                    result = "发送返回";
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }else if(response instanceof SendMessageToWX.Resp){
            SendMessageToWX.Resp resp = (SendMessageToWX.Resp) response;
            if(resp.errCode ==0){
                //share success
                 WeiXinHelper.get(getApplicationContext()).onShareSuccess();
            }else{
                //cancel or refused
                WeiXinHelper.get(getApplicationContext()).onShareFailed(resp.errStr);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if(api.handleIntent(intent, this)){
            finish();
        }
    }
}