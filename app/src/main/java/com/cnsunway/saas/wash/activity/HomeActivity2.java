package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.fragment.BaseFragment;
import com.cnsunway.saas.wash.fragment.HomeFragment3;
import com.cnsunway.saas.wash.fragment.NotificationFragment;
import com.cnsunway.saas.wash.fragment.MineFragment;
import com.cnsunway.saas.wash.fragment.OrderFragment;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.helper.HxHelper;
import com.cnsunway.saas.wash.helper.HxInitHelper;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.User;
import com.cnsunway.saas.wash.receiver.EmptyCallBack;
import com.cnsunway.saas.wash.resp.HxAccountResp;
import com.cnsunway.saas.wash.sharef.Preferences;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.HomeTabHost;
import com.hyphenate.chat.ChatClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/4/3 0003.
 */

public class HomeActivity2 extends FragmentActivity implements HomeTabHost.OnTabChangeListener{
    HomeTabHost homeTabHost;
    FragmentManager fm;
    StringVolley headPortraitVolley;
    LocationForService locationForService;
    StringVolley getHxAccountVolley;
    Handler updateUserhander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Const.Message.MSG_GET_HX_ACCOUNT_SUCC:
                    if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                        HxAccountResp accountResp = (HxAccountResp) JsonParser.jsonToObject(msg.obj +"",HxAccountResp.class);
                        if(Preferences.getInstance() == null){
                            Preferences.init(getApplicationContext());
                            HxInitHelper.getInstance().init(getApplicationContext());
                        }

                        if(accountResp.getData() != null){
                            User user = UserInfosPref.getInstance(HomeActivity2.this).getUser();
                            if(user != null){
                                user.setMobile(accountResp.getData().getUsername());
                                user.setHxPwd(accountResp.getData().getPassword());
                                UserInfosPref.getInstance(HomeActivity2.this).saveUser(user);
                                ChatClient.getInstance().login(accountResp.getData().getUsername(),accountResp.getData().getPassword(),new EmptyCallBack());
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        User user = UserInfosPref.getInstance(this).getUser();
        fm = getSupportFragmentManager();
        homeTabHost = (HomeTabHost) findViewById(R.id.home_tab);
        homeTabHost.setup(this,R.id.real_tab_content);
        homeTabHost.setMyTabChangeListener(this);
        homeTabHost.setCurrent(Const.TAB.TAB_HOME);
        headPortraitVolley = new StringVolley(this, Const.Message.MSG_PROFILE_SUCC, Const.Message.MSG_PROFILE_FAIL);
        getHxAccountVolley = new StringVolley(this, Const.Message.MSG_GET_HX_ACCOUNT_SUCC, Const.Message.MSG_GET_HX_ACCOUNT_FAIL);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String message){
        if(message.equals("pushSucc")){
            homeTabHost.setNoficationIcon(true);
        }  else if (message.equals("pushActivitySucc")){
            if(mMessageFragment != null){
                NotificationFragment notificationFragment = (NotificationFragment) mMessageFragment;
                notificationFragment.hasNewNofitication(true);
            }

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    String mPre = "",mCurrent="";

    private void toggle() {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment mPreFragment = fm.findFragmentByTag(mPre);
        Fragment mCurrentFragment = fm.findFragmentByTag(mCurrent);
        if (null != mPreFragment) {
            ft.detach(mPreFragment);
        }

        if (null != mCurrentFragment) {
            ft.attach(mCurrentFragment);
        }
        ft.commitAllowingStateLoss();
    }

    BaseFragment mHomeFragment,mOrderFragment,mMineFragment,mMessageFragment;

    @Override
    public void onTabChange(int index) {
        switch (index){
            case Const.TAB.TAB_HOME:
                mPre = mCurrent;
                mCurrent = "home";
                if (null == mHomeFragment) {
                    mHomeFragment = new HomeFragment3();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment mPreFragment = fm.findFragmentByTag(mPre);
                    if(null != mPreFragment){
                        ft.detach(mPreFragment);
                    }
                    ft.add(R.id.real_tab_content, mHomeFragment, mCurrent).commitAllowingStateLoss();
                } else {
                    toggle();
                }
                break;
            case Const.TAB.TAB_ORDER:
                mPre = mCurrent;
                mCurrent = "order";
                if (null == mOrderFragment) {
                    mOrderFragment = new OrderFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment mPreFragment = fm.findFragmentByTag(mPre);
                    if(null != mPreFragment){
                        ft.detach(mPreFragment);
                    }
                    ft.add(R.id.real_tab_content, mOrderFragment, mCurrent).commitAllowingStateLoss();
                } else {
                    toggle();
                }
                break;
            case Const.TAB.TAB_NOTIFICATION:
                mPre = mCurrent;
                mCurrent = "notification";
                homeTabHost.setNoficationIcon(false);
                if (null == mMessageFragment) {
                    mMessageFragment = new NotificationFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment mPreFragment = fm.findFragmentByTag(mPre);
                    if(null != mPreFragment){
                        ft.detach(mPreFragment);
                    }
                    ft.add(R.id.real_tab_content, mMessageFragment, mCurrent).commitAllowingStateLoss();
                } else {
                    toggle();
                }

                break;
            case Const.TAB.TAB_MINE:
                mPre = mCurrent;
                mCurrent = "mine";
                if (null == mMineFragment) {
                    mMineFragment = new MineFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment mPreFragment = fm.findFragmentByTag(mPre);
                    if(null != mPreFragment){
                        ft.detach(mPreFragment);
                    }
                    ft.add(R.id.real_tab_content, mMineFragment, mCurrent).commitAllowingStateLoss();
                } else {
                    toggle();
                }
                break;

            case Const.TAB.TAB_DO_ORDER:

                if(UserInfosPref.getInstance(this).getUser() == null){
                    startActivity(new Intent(this,LoginActivity.class));
                }else {
                    startActivity(new Intent(this,DoOrderActivity2.class));
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){
            int tab = intent.getIntExtra("tab",-1);
            if(tab != -1){
                homeTabHost.setCurrent(tab);
            }
            if(UserInfosPref.getInstance(this).getUser() != null){
                HxHelper.getInstance(this).login(UserInfosPref.getInstance(this).getUser().getMobile(),UserInfosPref.getInstance(this).getUser().getHxPwd(),true);
            }
        }
        sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_TABS));
    }

    public void setHomeTabHost(){
        homeTabHost.setCurrent(Const.TAB.TAB_HOME);
    }

    @Override
    public void onLockTab(int index) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("tab",index);
        startActivityForResult(intent,index);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(UserInfosPref.getInstance(this).getUser() != null){
//            getHxAccountVolley.requestGet(Const.Request.hxAccount, updateUserhander, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
//
//        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 0 || requestCode == 1|| requestCode == 2 || requestCode == 3||requestCode == 4){
            homeTabHost.setCurrent(0);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
