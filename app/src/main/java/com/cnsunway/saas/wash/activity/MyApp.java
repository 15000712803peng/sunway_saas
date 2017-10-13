package com.cnsunway.saas.wash.activity;


import android.preference.Preference;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.cnsunway.saas.wash.helper.HxHelper;
import com.cnsunway.saas.wash.helper.HxInitHelper;
import com.cnsunway.saas.wash.services.MyPushIntentService;
import com.cnsunway.saas.wash.sharef.Preferences;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;


/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MyApp extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
//                userInfos.setUmengToken(deviceToken );
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        Preferences.init(this);
        HxInitHelper.getInstance().init(this);
        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        ActiveAndroid.initialize(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
