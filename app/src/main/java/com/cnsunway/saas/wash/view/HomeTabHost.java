package com.cnsunway.saas.wash.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.sharef.UserInfosPref;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class HomeTabHost extends FrameLayout implements View.OnClickListener{

    ImageView redNotificationBadge;
    Context mContext;
    int mContainerId;
    private FrameLayout mRealTabContent;
    LinearLayout homeTabParent,orderTabParent,mineTabParent,doOrderTabParent;
    RelativeLayout notificationTabParent;
    ImageView homeIcon,orderIcon,noficationIcon,mineIcon;
    TextView homeText,orderText,noficationText,mineText;

    @Override
    public void onClick(View view) {
        doChange(view);
    }

    private OnTabChangeListener myTabChangeListener;


    public void setMyTabChangeListener(OnTabChangeListener myTabChangeListener) {
        this.myTabChangeListener = myTabChangeListener;
    }

    public interface OnTabChangeListener {

        public void onTabChange(int index);

        public void onLockTab(int index);
    }

    public HomeTabHost(Context context) {
        super(context);
        this.mContext = context;
    }

    RelativeLayout rootView;
    int mPre = -1,mCurrent = -1;

    public HomeTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        rootView = (RelativeLayout) inflate(context, R.layout.home_tab, this).findViewById(R.id.tab_root);
        redNotificationBadge = (ImageView) rootView.findViewById(R.id.red_notification_badge);
        homeTabParent = (LinearLayout) rootView.findViewById(R.id.main_tab_home);
        homeTabParent.setOnClickListener(this);
        orderTabParent = (LinearLayout) rootView.findViewById(R.id.main_tab_order);
        orderTabParent.setOnClickListener(this);
        notificationTabParent = (RelativeLayout) rootView.findViewById(R.id.main_tab_notification);
        notificationTabParent.setOnClickListener(this);
        mineTabParent = (LinearLayout) rootView.findViewById(R.id.main_tab_mine);
        mineTabParent.setOnClickListener(this);
        doOrderTabParent = (LinearLayout) rootView.findViewById(R.id.main_tab_do_order);
        doOrderTabParent.setOnClickListener(this);
        homeIcon = (ImageView) rootView.findViewById(R.id.icon_home);
        homeText = (TextView) rootView.findViewById(R.id.text_home);
        orderIcon = (ImageView) rootView.findViewById(R.id.icon_order);
        orderText = (TextView) rootView.findViewById(R.id.text_order);
        noficationIcon = (ImageView) rootView.findViewById(R.id.icon_notification);
        noficationText = (TextView) rootView.findViewById(R.id.text_notification);
        mineIcon = (ImageView) rootView.findViewById(R.id.icon_mine);
        mineText = (TextView) rootView.findViewById(R.id.text_mine);
        setClipChildren(false);

    }
    public void setNoficationIcon(boolean isSet){
        redNotificationBadge.setVisibility(isSet?View.VISIBLE:View.INVISIBLE);
    }
    public HomeTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void ensureContent() {
        if (mRealTabContent == null) {
            mRealTabContent = (FrameLayout)findViewById(mContainerId);
            if (mRealTabContent == null) {
                throw new IllegalStateException(
                        "No tab content FrameLayout found for id " + mContainerId);
            }
        }
    }

    public void setup(Context context,int containerId) {
        this.mContext = context;
        mContainerId = containerId;
        if (getId() == View.NO_ID) {
            setId(android.R.id.tabhost);
        }
    }

    private void toggleFocus(int mPre,int mCurrent){

        if(mPre == mCurrent){
            return;
        }
        if(mPre == Const.TAB.TAB_HOME){
            homeText.setSelected(false);
            homeIcon.setSelected(false);
        }else if(mPre == Const.TAB.TAB_ORDER){
            orderText.setSelected(false);
            orderIcon.setSelected(false);
        }else if(mPre == Const.TAB.TAB_NOTIFICATION){
            noficationText.setSelected(false);
            noficationIcon.setSelected(false);
        }else if(mPre == Const.TAB.TAB_MINE){
            mineText.setSelected(false);
            mineIcon.setSelected(false);
        }

        if(mCurrent == Const.TAB.TAB_HOME){
            homeText.setSelected(true);
            homeIcon.setSelected(true);
        }else if(mCurrent == Const.TAB.TAB_ORDER){
            orderText.setSelected(true);
            orderIcon.setSelected(true);
        }else if(mCurrent == Const.TAB.TAB_NOTIFICATION){
            noficationText.setSelected(true);
            noficationIcon.setSelected(true);
        }else if(mCurrent == Const.TAB.TAB_MINE){
            mineText.setSelected(true);
            mineIcon.setSelected(true);
        }
    }

    private void doChange(View view){
        if(view.getId() != R.id.main_tab_do_order)
        mPre = mCurrent;
       switch (view.getId()){
           case R.id.main_tab_home:
               mCurrent = Const.TAB.TAB_HOME;
               break;
           case R.id.main_tab_order:
               mCurrent = Const.TAB.TAB_ORDER;
               break;
           case R.id.main_tab_do_order:
//               mCurrent = Const.TAB.TAB_DO_ORDER;
               if(myTabChangeListener != null){
                   myTabChangeListener.onTabChange(Const.TAB.TAB_DO_ORDER);
               }

               return;

           case R.id.main_tab_notification:
               mCurrent = Const.TAB.TAB_NOTIFICATION;
               break;
           case R.id.main_tab_mine:
               mCurrent = Const.TAB.TAB_MINE;
               break;
       }

        if(UserInfosPref.getInstance(mContext).getUser() == null){
            if(myTabChangeListener != null){
                if(mCurrent == Const.TAB.TAB_HOME){
                    myTabChangeListener.onTabChange(mCurrent);
                    toggleFocus(mPre,mCurrent);
                }else {
                    myTabChangeListener.onLockTab(mCurrent);
                    toggleFocus(mPre,mCurrent);
                }
            }
        }else {
            myTabChangeListener.onTabChange(mCurrent);
            toggleFocus(mPre,mCurrent);
        }


    }

    public void setCurrent(int index){
        if(index == Const.TAB.TAB_HOME){
            doChange(homeTabParent);
        }else if(index == Const.TAB.TAB_ORDER){
            doChange(orderTabParent);
        }else if(index == Const.TAB.TAB_NOTIFICATION){
            doChange(notificationTabParent);
        }else if(index == Const.TAB.TAB_MINE){
            doChange(mineTabParent);
        }
    }

}
