package com.heaven7.android.third.sdk;

import com.heaven7.android.third.sdk.weixin.IWXLoginCallback;

import java.util.Map;

public interface NetworkApi {

    /*
    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String url, @FieldMap HashMap<String, Object> params);
     */
    void login(String url, Map<String, String> map, IWXLoginCallback callback);

    /*
     * @Headers({"Content-Type: application/json", "Accept: application/json"})
     *     @POST
     */
    //void postBody(String url, Map<String, String> map, Callback callback);
}
