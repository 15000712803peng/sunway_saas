package com.cnsunway.wash.util;

import android.content.Context;

import com.cnsunway.wash.sharef.UserInfosPref;

/**
 * Created by peter on 16/3/24.
 */
public class LoginManager {
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    private UserInfosPref userInfosPref;
    //初始化定位
    static LoginManager instance;
    public static LoginManager get(Context context){
        if(instance == null){
            instance = new LoginManager(context);
        }
        return instance;
    }

    public LoginManager(Context context){
        setContext(context);
    }

    public boolean isLogined(){
        UserInfosPref userInfos = UserInfosPref.getInstance(context);
        return userInfos.getUser() != null && userInfos.getUser().getToken() != null;
    }

    public String getToken(){
        if(!isLogined()){
            return null;
        }
        UserInfosPref userInfos = UserInfosPref.getInstance(context);
        return userInfos.getUser().getToken();
    }

}
