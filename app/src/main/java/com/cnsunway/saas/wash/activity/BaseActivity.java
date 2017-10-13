package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hyphenate.helpdesk.easeui.UIProvider;
import com.umeng.analytics.MobclickAgent;


public class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }
    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        UIProvider.getInstance(this).getNotifier().reset();
    }
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }




}
