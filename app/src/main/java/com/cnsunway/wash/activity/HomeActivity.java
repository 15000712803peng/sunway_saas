package com.cnsunway.wash.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.data.MyTab;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.util.LocationManager;
import com.cnsunway.wash.view.MyTabHost;
import com.cnsunway.wash.viewmodel.HomeOrderModel;
import com.cnsunway.wash.viewmodel.HomeViewModel;
import com.cnsunway.wash.viewmodel.OrderCacheManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LL on 2016/3/18.
 */
public class HomeActivity extends InitActivity{

    @Bind(R.id.tab_host)
    MyTabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home2);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        HomeViewModel.setActivity(this);
        OrderCacheManager.get(this);
        HomeOrderModel.setActivity(this);
        LocationManager.get().init(getApplicationContext());
    }

    @Override
    protected void initViews() {
            initTabHost();
    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    public void selectHomeTab(){
        mTabHost.setCurrentTab(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_TABS));
        sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_ADAPTER));
        super.onNewIntent(intent);
    }

    private final int TAB_ORDER = 2;
    private final int TAB_MiNE = 3;

    private void initTabHost() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_content_frame);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        MyTab[] tabs = MyTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            MyTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            mTabHost.addTab(tab, mainTab.getClz(), null);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
//               Log.e("tab",s);
                if(s.equals(getString(R.string.order))){
                    //用户未登陆
                    if(UserInfosPref.getInstance(HomeActivity.this).getUser() == null){

                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivityForResult(intent,TAB_ORDER);
                    }
                }else if(s.equals(getString(R.string.mine))){

                    if(UserInfosPref.getInstance(HomeActivity.this).getUser() == null){
                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivityForResult(intent,TAB_MiNE);
                    }


                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == TAB_MiNE || requestCode == TAB_ORDER){
                mTabHost.setCurrentTab(0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
