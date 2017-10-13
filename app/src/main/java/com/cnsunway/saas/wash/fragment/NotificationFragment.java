package com.cnsunway.saas.wash.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class NotificationFragment extends BaseFragment implements View.OnClickListener{
    TextView orderTabText,activityTabText,title,customTabText;
    FrameLayout messageParent;
    FragmentManager mFragmentManager;
    CustomMessageFragment customMessageFragment;
    OrderMessageFragment orderMessageFragment;
    ActivityMessageFragment activityMessageFragment;
    ImageView hasActivity,back;
    LinearLayout messageTab;
    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initFragmentDatas() {
        mFragmentManager = getChildFragmentManager();;
        orderMessageFragment = new OrderMessageFragment();
        customMessageFragment = new CustomMessageFragment();
        activityMessageFragment = new ActivityMessageFragment();
    }

    @Override
    protected void initMyViews(View view) {
        back = (ImageView) view.findViewById(R.id.image_title_back);
        back.setVisibility(View.GONE);
        title = (TextView) view.findViewById(R.id.text_title);
        title.setText("消息中心");
        messageTab = (LinearLayout) view.findViewById(R.id.tab_message);
        orderTabText = (TextView) view.findViewById(R.id.text_order_tab);
        activityTabText = (TextView) view.findViewById(R.id.text_activity_tab);
        customTabText = (TextView) view.findViewById(R.id.text_custom_tab);
        messageParent = (FrameLayout) view.findViewById(R.id.parent_message);
        hasActivity = (ImageView) view.findViewById(R.id.iv_has_activity);
        orderTabText.setSelected(true);
        customTabText.setSelected(true);
        activityTabText.setSelected(false);
        orderTabText.setOnClickListener(this);
        activityTabText.setOnClickListener(this);
        customTabText.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            setView(inflater.inflate(R.layout.fragment_notification,container,false));
            initMyViews(getView());
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.parent_message,orderMessageFragment);
//            ft.add(R.id.parent_message,customMessageFragment);
            ft.commitAllowingStateLoss();
            lastTab = orderMessageFragment;
//            doTabChanged(orderMessageFragment);
        }else {
            if (lastTab == orderMessageFragment && lastTab.isAdded()) {
                if(orderMessageFragment != null){
                    orderMessageFragment.onRefresh();
                }
            } else if (lastTab == activityMessageFragment && lastTab.isAdded()) {
                if(activityMessageFragment != null)
                activityMessageFragment.onRefresh();
            }
        }
//
        return getView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }*/

    BaseFragment lastTab;
    private void doTabChanged(BaseFragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            ft.replace(R.id.parent_message,fragment);
            ft.commitAllowingStateLoss();
            lastTab = fragment;
            return;
        }
        if(lastTab == fragment){
            return;
        }
        ft.detach(lastTab);
        ft.attach(fragment);
        ft.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
        lastTab = fragment;
}

    @Override
    public void onClick(View view) {
        if(view == orderTabText){
            doTabChanged(orderMessageFragment);
            orderTabText.setSelected(true);
            activityTabText.setSelected(false);
            customTabText.setSelected(false);
            orderTabText.setTextColor(this.getResources().getColor(R.color.text));
            activityTabText.setTextColor(this.getResources().getColor(R.color.text_gray2));
            customTabText.setTextColor(this.getResources().getColor(R.color.text_gray2));
            if(orderMessageFragment != null&& orderMessageFragment.isAdded()){
                orderMessageFragment.onRefresh();
            }

        }else if(view == activityTabText){
            if(activityMessageFragment !=  null && activityMessageFragment.isAdded()){
                activityMessageFragment.onRefresh();
            }
            doTabChanged(activityMessageFragment);
            orderTabText.setSelected(false);
            customTabText.setSelected(false);
            activityTabText.setSelected(true);
            orderTabText.setTextColor(this.getResources().getColor(R.color.text_gray2));
            customTabText.setTextColor(this.getResources().getColor(R.color.text_gray2));
            activityTabText.setTextColor(this.getResources().getColor(R.color.text));
            hasNewNofitication(false);
        }else if(view == customTabText){
            doTabChanged(customMessageFragment);
            customTabText.setSelected(true);
            activityTabText.setSelected(false);
            orderTabText.setSelected(false);
            customTabText.setTextColor(this.getResources().getColor(R.color.text));
            orderTabText.setTextColor(this.getResources().getColor(R.color.text_gray2));
            activityTabText.setTextColor(this.getResources().getColor(R.color.text_gray2));
           if(customMessageFragment != null && customMessageFragment.isAdded()){
                customMessageFragment.onRefresh();
            }

        }
    }

    public void hasNewNofitication(boolean isSet){
        hasActivity.setVisibility(isSet?View.VISIBLE:View.INVISIBLE);
    }

}
