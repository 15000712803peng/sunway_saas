package com.cnsunway.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.fragment.BaseFragment;
import com.cnsunway.wash.fragment.HomeFragment3;
import com.cnsunway.wash.fragment.NotificationFragment;
import com.cnsunway.wash.fragment.MineFragment;
import com.cnsunway.wash.fragment.OrderFragment;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.view.HomeTabHost;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        fm = getSupportFragmentManager();
        homeTabHost = (HomeTabHost) findViewById(R.id.home_tab);
        homeTabHost.setup(this,R.id.real_tab_content);
        homeTabHost.setMyTabChangeListener(this);
        homeTabHost.setCurrent(Const.TAB.TAB_HOME);

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
                    startActivity(new Intent(this,DoOrderActivity.class));
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Toast.makeText(this,"home activity on new intent",Toast.LENGTH_SHORT).show();
        super.onNewIntent(intent);
        if(intent != null){
           int tab = intent.getIntExtra("tab",-1);
            if(tab != -1){
                homeTabHost.setCurrent(tab);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 0 || requestCode == 1|| requestCode == 2 || requestCode == 3||requestCode == 4){
            homeTabHost.setCurrent(0);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
