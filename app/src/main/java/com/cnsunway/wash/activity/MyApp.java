package com.cnsunway.wash.activity;


import android.content.Intent;
import android.util.Log;

import com.cnsunway.wash.services.MyPushIntentService;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;


/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MyApp extends com.activeandroid.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
//                userInfos.setUmengToken(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("onFailure","on failure:" + "s:" + s + "  s1:" +s1);
            }
        });

        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);

    }
}
