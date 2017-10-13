package com.cnsunway.saas.wash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.adapter.HistoryOrderAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.view.XListView;
import com.cnsunway.saas.wash.viewmodel.HistoryOrderModel;
import com.cnsunway.saas.wash.viewmodel.HomeViewModel;
import com.cnsunway.saas.wash.viewmodel.ViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class HistoryOrdersActivity extends InitActivity implements  XListView.IXListViewListener{
    @Bind(R.id.text_title)
    TextView titleText;
    @Bind(R.id.ll_noorder)
    View layoutNoOrder;
    @Bind(R.id.ll_hasorder)
    View layoutHasOrder;
    @Bind(R.id.rl_network_fail)
    View noNetworkView;
    @Bind(R.id.listview)
    XListView listView;
    @Bind(R.id.rl_loading)
    RelativeLayout loadingParent;
    @Bind(R.id.iv_loading)
    ImageView loadingImage;
    AnimationDrawable loadingAni;
    HistoryOrderAdapter orderAdapter;
    @Bind(R.id.text_refresh)
    TextView refreshText;
    HistoryOrderModel orderModel;
    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    protected  void showCenterLoading(){
        loadingParent.setVisibility(View.VISIBLE);
        layoutNoOrder.setVisibility(View.GONE);
        layoutHasOrder.setVisibility(View.GONE);
        noNetworkView.setVisibility(View.GONE);
        loadingAni.start();
    }
    private void showNetworkFail(){
        noNetworkView.setVisibility(View.VISIBLE);
        layoutNoOrder.setVisibility(View.GONE);
        layoutHasOrder.setVisibility(View.GONE);
        loadingParent.setVisibility(View.GONE);
        loadingAni.stop();


    }

    private void showNoOrder(){
        layoutNoOrder.setVisibility(View.VISIBLE);
        noNetworkView.setVisibility(View.GONE);
        layoutHasOrder.setVisibility(View.GONE);
        loadingParent.setVisibility(View.GONE);
        loadingAni.stop();

    }

    private void showHasOrder(){
        layoutHasOrder.setVisibility(View.VISIBLE);
        noNetworkView.setVisibility(View.GONE);
        layoutNoOrder.setVisibility(View.GONE);
        loadingParent.setVisibility(View.GONE);
        loadingAni.stop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history_orders);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        titleText.setText(R.string.title_order_history);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this);
        refreshText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCenterLoading();
                onRefresh();
            }
        });


        loadingAni = (AnimationDrawable) loadingImage.getBackground();
        load();
    }

    public void back(View view){
        finish();
    }

    @Override
    public void onRefresh() {
            orderModel.onRefresh();
    }

    @Override
    public void onLoadMore() {
            orderModel.onLoadMore();
    }

    public void load(){
        showCenterLoading();
        orderModel = new HistoryOrderModel();
        orderModel = new HistoryOrderModel();
        orderModel.addPropertyChangeListener(ViewModel.PROPERTY_NETREQUEST_STATUS, volleyStatusListener);
        orderModel.addPropertyChangeListener(HomeViewModel.PROPERTY_RESPONSE, serverErrorListener);
        orderModel.addPropertyChangeListener(HomeViewModel.PROPERTY_HOMELISTS, homeListListener);
        orderModel.requestData(this);

    }

    private PropertyChangeListener homeListListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (orderModel.getHomeLists() == null || orderModel.getHomeLists().size() <= 0) {
                showNoOrder();
                return;
            }
            showHasOrder();

            if(orderAdapter == null) {
                orderAdapter = new HistoryOrderAdapter(HistoryOrdersActivity.this, orderModel.getHomeLists());
                listView.setAdapter(orderAdapter);
            }else{
                orderAdapter.setOrderList(orderModel.getHomeLists());
                orderAdapter.notifyDataSetChanged();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Order o = (Order) adapterView.getAdapter().getItem(i);
                    if(o.getType() == 1){
                        Intent intent = new Intent(HistoryOrdersActivity.this, OrderDetailActivity.class);
                        intent.putExtra("order_no", o.getOrderNo());
                        startActivityForResult(intent, 1);
                    }else if(o.getType() == 2){
                        Intent intent = new Intent(HistoryOrdersActivity.this, OrderDetailActivity.class);
                        intent.putExtra("order_no", o.getOrderNo());
                        startActivityForResult(intent, 1);
                    }
                }
            });
            layoutNoOrder.setVisibility(View.INVISIBLE);
            layoutHasOrder.setVisibility(View.VISIBLE);

            int count = orderModel.getHomeLists().size();
            if(count >= orderModel.getTotal()){
                listView.setPullLoadEnable(false);

            }else{
                listView.setPullLoadEnable(true);
            }
        }
    };

    private PropertyChangeListener volleyStatusListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            switch (orderModel.getRequestStatus()) {
                case ViewModel.STATUS_NET_ERROR:
                case ViewModel.STATUS_SERVER_ERROR:
                case ViewModel.STATUS_REQUEST_FAIL:
                    showNetworkFail();
                    break;

                case ViewModel.STATUS_REQUEST_SUCC:
                    break;

            }

            hideLoading();
            listView.stopLoadMore();
            listView.stopRefresh();
        }
    };

    private PropertyChangeListener serverErrorListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            BaseResp resp = orderModel.getResp();
            if (resp.getResponseCode() != 0) {
                String msg = resp.getResponseMsg();
                //显示错误
                showNetworkFail();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(refreshReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_TABS));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refreshReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };
}
