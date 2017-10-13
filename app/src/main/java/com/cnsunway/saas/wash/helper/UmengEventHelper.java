package com.cnsunway.saas.wash.helper;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class UmengEventHelper {

    public static void loginEvent(Context context, String user){
        Map loginMap = new HashMap();
        loginMap.put("userid", user);
        MobclickAgent.onEvent(context, "__login", loginMap);
    }

    public static void doOrderEvent(Context context,String user){
        Map beginPayMap = new HashMap();
        beginPayMap.put("userid", user);
        MobclickAgent.onEvent(context, "__submit_payment", beginPayMap);
    }

    public static void payEvent(Context context,String user){
        Map successPayMap = new HashMap();
        successPayMap.put("userid", user);
        MobclickAgent.onEvent(context,"__finish_payment", successPayMap);
    }
}
