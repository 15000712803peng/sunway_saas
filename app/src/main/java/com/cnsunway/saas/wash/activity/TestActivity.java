package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.fragment.BaseFragment;
import com.cnsunway.saas.wash.fragment.HomeFragment2;
import com.cnsunway.saas.wash.fragment.MineFragment;
import com.cnsunway.saas.wash.fragment.OrderFragment;
import com.cnsunway.saas.wash.view.HomeTabHost;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2017/4/3 0003.
 */

public class TestActivity extends FragmentActivity implements HomeTabHost.OnTabChangeListener{

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

    BaseFragment mHomeFragment,mOrderFragment,mMineFragment;

    @Override
    public void onTabChange(int index) {
        switch (index){
            case Const.TAB.TAB_HOME:
                mPre = mCurrent;
                mCurrent = "home";
                if (null == mHomeFragment) {
                    mHomeFragment = new HomeFragment2();
                    FragmentTransaction ft = fm.beginTransaction();
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
                    ft.add(R.id.real_tab_content, mOrderFragment, mCurrent).commitAllowingStateLoss();
                } else {
                    toggle();
                }
                break;
            case Const.TAB.TAB_NOTIFICATION:
                mPre = mCurrent;
                mCurrent = "notification";
                if (null == mOrderFragment) {
                    mOrderFragment = new OrderFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.real_tab_content, mOrderFragment, mCurrent).commitAllowingStateLoss();
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
                    ft.add(R.id.real_tab_content, mMineFragment, mCurrent).commitAllowingStateLoss();
                } else {
                    toggle();
                }
                break;
        }
    }

    @Override
    public void onLockTab(int index) {

    }
}
