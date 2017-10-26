package com.cnsunway.saas.wash.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.DoOrderActivity;
import com.cnsunway.saas.wash.activity.HistoryOrdersActivity;
import com.cnsunway.saas.wash.activity.LoginActivity;
import com.cnsunway.saas.wash.activity.OrderDetailActivity;
import com.cnsunway.saas.wash.activity.PayActivity;
import com.cnsunway.saas.wash.activity.PayActivity2;
import com.cnsunway.saas.wash.activity.WebActivity;
import com.cnsunway.saas.wash.adapter.OrderAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.LoadingDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.PayData;
import com.cnsunway.saas.wash.resp.GetPriceResp;
import com.cnsunway.saas.wash.resp.OrderDetailResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.XListView;
import com.cnsunway.saas.wash.viewmodel.OrderListModel;
import com.cnsunway.saas.wash.viewmodel.ViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

/**
 * Created by LL on 2016/3/19.
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener,XListView.IXListViewListener ,LoadingDialogInterface,OrderAdapter.OnConfirmClickedListenr,OrderAdapter.OnPayClickedLinster{

    OrderListModel orderListModel;
    public static final int OPERATION_ORDER_PAY = 2;
    TextView doOrderText, priceListText,historyOrderText;
    View layoutNoOrder;
    View layoutHasOrder;
    View noNetworkView;
    XListView listView;
    OrderAdapter orderAdapter;
    JsonVolley orderDetailVolley;
    RelativeLayout loadingParent;
    ImageView loadingImage;
    AnimationDrawable loadingAni;
    TextView refreshText;
    LinearLayout noLoginLay;
    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_ORDER_DETAIL_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    OrderDetailResp detailResp = (OrderDetailResp) JsonParser.jsonToObject(msg.obj + "", OrderDetailResp.class);
                    final Order order = detailResp.getData();
                    String action = order.getAction();
                    if(action.equals("share") || action.equals("direct")){
                        /*ShareGiftDialog2 shareGiftDialog  =  new ShareGiftDialog2(getActivity(),order).builder();
                        shareGiftDialog.setShareBtnClickedLinstener(new ShareGiftDialog2.OnShareBtnClickedLinstener() {
                            @Override
                            public void shareBtnClicked() {
                                WayOfShareDialog wayOfShareDialog = new WayOfShareDialog(getActivity()).builder();
                                new ShareUtil(wayOfShareDialog).share(getActivity(),order);
                            }
                        });
                        shareGiftDialog.show();*/


                        Intent intent = new Intent(getActivity(),WebActivity.class);
                        intent.putExtra("url",Const.Request.paySuccess + detailResp.getData().getTotalPrice());
                        intent.putExtra("title","支付成功");
                        intent.putExtra("order",JsonParser.objectToJsonStr(order));
                        startActivity(intent);

                    }
                }
                break;
            case Const.Message.MSG_CONFIRM_DONE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    OperationToast.showOperationResult(getActivity(),"确认成功",0);
                    getActivity().sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_TABS));
                }else {
                    OperationToast.showOperationResult(getActivity(),"操作失败",0);
                }
                break;

            case Const.Message.MSG_CONFIRM_DONE_FAIL:

                OperationToast.showOperationResult(getActivity(),"操作失败",0);
                break;

            case Const.Message.MSG_GET_PAY_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    GetPriceResp priceResp = (GetPriceResp) JsonParser.jsonToObject(msg.obj + "", GetPriceResp.class);
                    if (priceResp.getData() != null &&priceResp.getData().getDftCoupon() != null) {
                        Intent intent = new Intent(getActivity(),PayActivity.class);
                        String balance = priceResp.getData().getBalance();
                        float couponDiscount = (float) (Math.round(Float.parseFloat(priceResp.getData() .getDftCoupon().getAmount()) * 100)) / 100;
                        PayData couponData = new PayData(12, couponDiscount + "", priceResp.getData() .getDftCoupon().getCouponNo());
                        intent.putExtra("order_no", orderNo);
                        if(TextUtils.isEmpty(balance)){
                            balance = "";
                        }
                        if(couponData != null){
                            intent.putExtra("coupon_data",JsonParser.objectToJsonStr(couponData));
                        }
                        intent.putExtra("balance",balance);
                        String subPrice = totalPrice;
                        if(!TextUtils.isEmpty(subPrice)){
                            subPrice = new BigDecimal(subPrice).subtract(new BigDecimal(priceResp.getData().getDftCoupon().getAmount())).floatValue() +"";
                        }
                        intent.putExtra("order_price", Float.parseFloat(subPrice));
                        startActivityForResult(intent, OPERATION_ORDER_PAY);


                    }else {
                        Intent intent = new Intent(getActivity(),PayActivity.class);
                        intent.putExtra("order_no", orderNo);
                        String balance = priceResp.getData().getBalance();
                        if(TextUtils.isEmpty(balance)){
                            balance = "";
                        }

                        intent.putExtra("balance",balance);

                        if(!TextUtils.isEmpty(totalPrice)){
                            intent.putExtra("order_price", Float.parseFloat(totalPrice));
                        }

//                        if(!TextUtils.isEmpty(subPrice)){
//                            subPrice = new BigDecimal(subPrice).subtract(new BigDecimal(priceResp.getData().getDftCoupon().getCouponCount())).floatValue() +"";
//                        }

                        startActivityForResult(intent, OPERATION_ORDER_PAY);
                    }
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    OperationToast.showOperationResult(getActivity(), msg.obj + "", R.mipmap.wrong_icon);
                }
                break;
            case Const.Message.MSG_GET_PAY_FAIL:
                OperationToast.showOperationResult(getActivity(), R.string.request_fail,R.mipmap.wrong_icon);
                break;

        }
    }

    @Override
    protected void initFragmentDatas() {

    }

    @Override
    protected void initMyViews(View view) {


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
//        if(orderAdapter != null){
//            listView.setAdapter(orderAdapter);
//            orderAdapter.notifyDataSetChanged();
//            orderAdapter.setConfirmClickedListenr(OrderFragment.this);
//        }

    }

    public void load(){
        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        if(userInfos.getUser() == null){
            showNoLogin();
            return;
        }

        if(orderAdapter == null){
            showCenterLoading();
        }
        orderListModel = new OrderListModel();
        if(userInfos.getUser() != null){
            orderListModel = new OrderListModel();
//            Log.e("order fragment token","token" +  UserInfosPref.getInstance(getActivity()).getUser().getToken());
            orderListModel.addPropertyChangeListener(ViewModel.PROPERTY_NETREQUEST_STATUS, volleyStatusListener);
            orderListModel.addPropertyChangeListener(OrderListModel.PROPERTY_RESPONSE, serverErrorListener);
            orderListModel.addPropertyChangeListener(OrderListModel.PROPERTY_HOMELISTS, orderListListener);
            orderListModel.requestOrders(getActivity());
        }else {
            orderListModel.setHomeLists(null);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getView() == null) {
            setView(inflater.inflate(R.layout.fragment_order, container, false));
            layoutNoOrder = getView().findViewById(R.id.ll_noorder);
            layoutHasOrder = getView().findViewById(R.id.ll_hasorder);
            layoutNoOrder.setOnClickListener(refreshClick);
            refreshText = (TextView) getView().findViewById(R.id.text_refresh);
            refreshText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCenterLoading();
                    onRefresh();
                }
            });

            noNetworkView = getView().findViewById(R.id.rl_network_fail);
            listView = (XListView) getView().findViewById(R.id.listview);
            listView.setXListViewListener(this);
            listView.setPullLoadEnable(true);
            listView.setPullRefreshEnable(true);
            listView.setOnScrollListener(scrollListener);
//            doOrderText = (TextView) getView().findViewById(R.id.text_do_order);
//            doOrderText.setOnClickListener(this);
            priceListText = (TextView) getView().findViewById(R.id.text_price);
            priceListText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),HistoryOrdersActivity.class);
                    intent.putExtra("url",Const.Request.priceList+"/1");
                    intent.putExtra("title","价目表");
                    startActivity(intent);
                }
            });
            historyOrderText = (TextView) getView().findViewById(R.id.tv_history_order);
            historyOrderText.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),HistoryOrdersActivity.class));
                }
            });
            loadingParent = (RelativeLayout) getView().findViewById(R.id.rl_loading);
            noLoginLay = (LinearLayout) getView().findViewById(R.id.ll_no_login);
            loadingImage = (ImageView) getView().findViewById(R.id.iv_loading);
            loadingAni = (AnimationDrawable) loadingImage.getBackground();

        }
        //刷新界面
        load();
        return getView();
    }




    protected  void showCenterLoading(){
        loadingParent.setVisibility(View.VISIBLE);
        layoutNoOrder.setVisibility(View.GONE);
        layoutHasOrder.setVisibility(View.GONE);
        noNetworkView.setVisibility(View.GONE);
        noLoginLay.setVisibility(View.GONE);
        loadingAni.start();
    }
    private void showNetworkFail(){
        noNetworkView.setVisibility(View.VISIBLE);
        layoutNoOrder.setVisibility(View.GONE);
        layoutHasOrder.setVisibility(View.GONE);
        loadingParent.setVisibility(View.GONE);
        loadingAni.stop();
        noLoginLay.setVisibility(View.GONE);

    }

    private void showNoOrder(){
        layoutNoOrder.setVisibility(View.VISIBLE);
        noNetworkView.setVisibility(View.GONE);
        layoutHasOrder.setVisibility(View.GONE);
        loadingParent.setVisibility(View.GONE);
        loadingAni.stop();
        noLoginLay.setVisibility(View.GONE);
    }

    private void showHasOrder(){
        layoutHasOrder.setVisibility(View.VISIBLE);
        noNetworkView.setVisibility(View.GONE);
        layoutNoOrder.setVisibility(View.GONE);
        loadingParent.setVisibility(View.GONE);
        loadingAni.stop();
        noLoginLay.setVisibility(View.GONE);
    }

    private void showNoLogin(){
        noLoginLay.setVisibility(View.VISIBLE);
        noNetworkView.setVisibility(View.GONE);
        layoutNoOrder.setVisibility(View.GONE);
        loadingParent.setVisibility(View.GONE);
        loadingAni.stop();
        layoutHasOrder.setVisibility(View.GONE);
    }


    protected  void hideCenterLoading(){
        loadingAni.stop();
        loadingParent.setVisibility(View.INVISIBLE);
    }

    LocationForService locationForService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(refreshHomeOrdersReceiver, new IntentFilter(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
        getActivity().registerReceiver(refreshReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_TABS));
        orderDetailVolley = new JsonVolley(getActivity(), Const.Message.MSG_ORDER_DETAIL_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);
        confirmDoneVolley = new JsonVolley(getActivity(),Const.Message.MSG_CONFIRM_DONE_SUCC,Const.Message.MSG_PAY_CONFIRM_FAIL);
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        getActivity().registerReceiver(adapterReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_ADAPTER));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshHomeOrdersReceiver);
        getActivity().unregisterReceiver(refreshReceiver);
        getActivity().unregisterReceiver(adapterReceiver);
    }
    BroadcastReceiver refreshHomeOrdersReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        };
    };

    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == SCROLL_STATE_IDLE) {

            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //totalItemCount包含header和footer
            int lastItem = totalItemCount-1;
        }
    };
    @Override
    public void onClick(View v) {
        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
       if (v == doOrderText) {
            if(userInfos.getUser() == null){
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                startActivity(new Intent(getActivity(), DoOrderActivity.class));
            }

        }
    }

    private PropertyChangeListener volleyStatusListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            switch (orderListModel.getRequestStatus()) {
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
    private PropertyChangeListener orderListListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (orderListModel.getHomeLists() == null || orderListModel.getHomeLists().size() <= 0) {
                showNoOrder();
                if(orderAdapter != null){
                    if(orderAdapter.getOrderList() != null){
                        orderAdapter.getOrderList().clear();
                        orderAdapter.notifyDataSetChanged();

                    }
                    orderAdapter.setConfirmClickedListenr(OrderFragment.this);
                    orderAdapter.setOnPayClickedLinster(OrderFragment.this);
                }

                return;
            }
           showHasOrder();

            if(orderAdapter == null) {
                orderAdapter = new OrderAdapter(OrderFragment.this, orderListModel.getHomeLists());
                orderAdapter.setConfirmClickedListenr(OrderFragment.this);
                orderAdapter.setOnPayClickedLinster(OrderFragment.this);
                listView.setAdapter(orderAdapter);
            }else{

                orderAdapter.setOrderList(orderListModel.getHomeLists());
                orderAdapter.notifyDataSetChanged();

            }

//            orderAdapter = new OrderAdapter(OrderFragment.this, orderListModel.getHomeLists());
//            listView.setAdapter(orderAdapter);
            listView.setOnItemClickListener(OrderFragment.this);

            int count = orderListModel.getHomeLists().size();
            if(count >= orderListModel.getTotal()){
                listView.setPullLoadEnable(false);
//                listView.showHistoryFooter(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        getActivity().startActivity(new Intent(getActivity(), HistoryOrdersActivity.class));
//                    }
//                });
            }else{
                listView.setPullLoadEnable(true);

            }
        }
    };

    private PropertyChangeListener serverErrorListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            BaseResp resp = orderListModel.getResp();
            if (resp.getResponseCode() != 0) {
                String msg = resp.getResponseMsg();
                //显示错误
                showNetworkFail();
            }
        }
    };



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Order o = (Order) adapterView.getAdapter().getItem(i);
        if(o.getType() == 1){
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("order_no", o.getOrderNo());
            startActivityForResult(intent, 1);
        }else if(o.getType() == 2){
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("order_no", o.getOrderNo());
            startActivityForResult(intent, 1);
        }
    }

    private View.OnClickListener refreshClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };
    @Override
    public void onRefresh() {
        orderListModel.onRefresh();
    }

    @Override
    public void onLoadMore() {
        orderListModel.onLoadMore();
    }

    @Override
    public void showLoading() {
        showLoadingDialog(getString(R.string.operating));
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    LoadingDialog loadingDialog;

    protected void showLoadingDialog(String message) {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = new LoadingDialog(getActivity(), message);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPERATION_ORDER_PAY && resultCode == Activity.RESULT_OK) {
                  onRefresh();
            /*if(data == null){
                return;
            }*/
//            final Order order = (Order)data.getSerializableExtra("order");
//            if(order == null){
//                return;
//            }
           /* String orderNo = data.getStringExtra("order_no");
            orderDetailVolley.requestGet(Const.Request.detail + "/" + orderNo, getHandler(), UserInfosPref.getInstance(getActivity()).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());*/
//            getActivity().sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));

        }
    }

    JsonVolley confirmDoneVolley;

    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            load();
        }
    };

    BroadcastReceiver adapterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            load();
//            if(orderAdapter != null){
//                listView.setAdapter(orderAdapter);
//                orderAdapter.notifyDataSetChanged();
//                orderAdapter.setConfirmClickedListenr(OrderFragment.this);
//            }
        }
    };

    @Override
    public void confirmClicked(String orderNo) {
        confirmDoneVolley.addParams("orderNo",orderNo);
        confirmDoneVolley.requestPost(Const.Request.confirmDone+"/"+orderNo + "/receive",this,getHandler(),UserInfosPref.getInstance(getActivity()).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    JsonVolley getPriceVolley;
    String totalPrice;
    String orderNo;
    @Override
    public void onPayClicked(String myPrice,String orderNo) {
        this.orderNo = orderNo;
        this.totalPrice = myPrice;
        Intent intent = new Intent(getActivity(), PayActivity2.class);
        intent.putExtra("order_no", orderNo);
        intent.putExtra("order_price", Float.parseFloat(myPrice));
        startActivityForResult(intent, OPERATION_ORDER_PAY);
    }
}
