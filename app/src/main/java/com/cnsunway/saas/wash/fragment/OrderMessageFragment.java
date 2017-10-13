package com.cnsunway.saas.wash.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.OrderDetailActivity;
import com.cnsunway.saas.wash.adapter.OrderMessageAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.DateUtil;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.AllMessage;
import com.cnsunway.saas.wash.model.AllMessageResp;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.XListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hp on 2017/6/6.
 */
public class OrderMessageFragment extends BaseFragment implements XListView.IXListViewListener ,AdapterView.OnItemClickListener{
    OrderMessageAdapter orderMessageAdapter;
    XListView orderMessageLV;
    UserInfosPref userInfos;
    String token;
    JsonVolley messageVolley;
    List<AllMessage> allMessages;
    JsonVolley readVolley;
    private int page = 1;
    int totalPage = 0;
    RelativeLayout noDataParent;
    TextView noDateText;
    //=============
    RelativeLayout loadingParent;
    ImageView loadingImage;
    AnimationDrawable loadingAni;
    LocationForService locationForService;
    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_MESSAGE_SUCC:
                hideLoding();
                if (msg.arg1 == Const.Request.REQUEST_SUCC){
                    orderMessageLV.setRefreshTime(DateUtil.getCurrentDate());
                    orderMessageLV.stopRefresh("刷新成功");
                    AllMessageResp initResp = (AllMessageResp) JsonParser.jsonToObject(msg.obj+"",AllMessageResp.class);
                    page = initResp.getData().getPaginator().getPage();
                    totalPage = initResp.getData().getPaginator().getTotalPages();
                    int totalCount = initResp.getData().getPaginator().getTotalCount();
                    if (totalCount == 0){
                        showNoMessage();
                        return;
                    }
//                   totalPage = 3;
                    orderMessageLV.setVisibility(View.VISIBLE);
                    noDataParent.setVisibility(View.GONE);
                    if (page==1){
                        orderMessageLV.setPullLoadEnable(true);
                        allMessages = initResp.getData().getResults();
//                        sort(allMessages);//时间排序
                        initList(allMessages);
                    }else if (page>1){
                        allMessages = initResp.getData().getResults();
                        loadMoreList(allMessages);
                    }
                }else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    orderMessageLV.stopRefresh("刷新失败");
                    orderMessageLV.stopLoadMore();
                    OperationToast.showOperationResult(getActivity(),msg.obj+"",0);
                }
                break;
            case Const.Message.MSG_MESSAGE_FAIL:
                hideLoding();
                OperationToast.showOperationResult(getActivity(), "操作失败", 0);
                break;
            case Const.Message.MSG_READ_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC){
                    onRefresh();
                }else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    OperationToast.showOperationResult(getActivity(),msg.obj+"",0);
                }
                break;
            case Const.Message.MSG_READ_FAIL:
                OperationToast.showOperationResult(getActivity(),msg.obj+"",0);
                break;
        }
    }

    private void sort(final List<AllMessage> messages) {
        Collections.sort(messages, new Comparator<AllMessage>() {

            public int compare(AllMessage lhs, AllMessage rhs) {
                // 对日期字段进行升序
                String time1 = lhs.getPushDate();
                long milliTime1 = DateUtil.getMilliTime(time1);
                String time2 = lhs.getPushDate();
                long milliTime2 = DateUtil.getMilliTime(time2);
                if (milliTime1 >= milliTime2) {
                    return 1;
                }
                return -1;
            }
            });

    }
    private void showNoMessage(){
        orderMessageLV.setVisibility(View.GONE);
        noDataParent.setVisibility(View.VISIBLE);
    }
    private void loadMoreList(List<AllMessage> allMessages) {
        orderMessageLV.stopLoadMore();
        if (allMessages == null) {
            return;
        }
        orderMessageAdapter.getAllMessageList().addAll(allMessages);
        orderMessageAdapter.notifyDataSetChanged();
        if (page >= totalPage) {
            orderMessageLV.setPullLoadEnable(false);
        }
    }
    @Override
    protected void initFragmentDatas() {
        getActivity().registerReceiver(refreshReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_MESSAGE));
        getActivity().registerReceiver(refreshTabReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_TABS));
        messageVolley = new JsonVolley(getActivity(), Const.Message.MSG_MESSAGE_SUCC,Const.Message.MSG_MESSAGE_FAIL);
        messageVolley.addParams("msgType",1);
        messageVolley.addParams("userMobile",UserInfosPref.getInstance(getActivity()).getUserName());
        Log.e("userName",UserInfosPref.getInstance(getActivity()).getUserName()) ;
        messageVolley.addParams("page",page);
        userInfos = UserInfosPref.getInstance(getActivity());
        token = userInfos.getUser().getToken();
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
    }

    @Override
    protected void initMyViews(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            setView(inflater.inflate(R.layout.fragment_order_message,container,false));
            orderMessageLV = (XListView) view.findViewById(R.id.lv_order_message);
            orderMessageLV.setXListViewListener(this);
            orderMessageLV.setOnItemClickListener(this);
            orderMessageLV.setPullLoadEnable(true);
            noDataParent = (RelativeLayout)view.findViewById(R.id.rl_no_data);
            noDateText = (TextView) view.findViewById(R.id.tv_no_data);
            noDateText.setText("暂无订单消息");
            loadingParent = (RelativeLayout) getView().findViewById(R.id.rl_loading);
            loadingImage = (ImageView) getView().findViewById(R.id.iv_loading);
            loadingAni = (AnimationDrawable) loadingImage.getBackground();
            showLoading();
            messageVolley.requestPost(Const.Request.messageAll,getHandler(),token
                    ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );
        }

        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();

        return getView();

    }

    private void showLoading(){
        loadingParent.setVisibility(View.VISIBLE);
        loadingAni.start();
        orderMessageLV.setVisibility(View.INVISIBLE);
        noDataParent.setVisibility(View.INVISIBLE);
    }

    private void hideLoding(){
        loadingAni.stop();
        loadingParent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshReceiver);
        getActivity().unregisterReceiver(refreshTabReceiver);
    }

    private void initList(List<AllMessage> allMessages){
        if (orderMessageAdapter == null){
            orderMessageAdapter = new OrderMessageAdapter(this,allMessages);
            orderMessageAdapter.setAllMessageList(allMessages);
            orderMessageLV.setAdapter(orderMessageAdapter);
        }else {
            orderMessageAdapter.clear();
            orderMessageAdapter.getAllMessageList().addAll(allMessages);
            orderMessageAdapter.notifyDataSetChanged();
        }

        if (page >= totalPage) {
            orderMessageLV.setPullLoadEnable(false);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        userInfos = UserInfosPref.getInstance(getActivity());
        token = userInfos.getUser().getToken();
        messageVolley.addParams("page",page);
        messageVolley.addParams("userMobile",UserInfosPref.getInstance(getActivity()).getUserName());
        messageVolley.requestPost(Const.Request.messageAll,getHandler(),token
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );

    }

    @Override
    public void onLoadMore() {
        userInfos = UserInfosPref.getInstance(getActivity());
        token = userInfos.getUser().getToken();
        int nextPage = page  + 1;
        messageVolley.addParams ("page",nextPage);
        messageVolley.requestPost(Const.Request.messageAll,getHandler(),token
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        final AllMessage a = (AllMessage) adapterView.getAdapter().getItem(i);
//        String orderID = a.getOrderID();
        String orderID = a.getContent();
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("order_no", orderID);
        startActivityForResult(intent, 1);
        readVolley = new JsonVolley(getActivity(), Const.Message.MSG_READ_SUCC,Const.Message.MSG_READ_FAIL);
        readVolley.addParams("msgId",a.getMsgId());
        readVolley.addParams("msgType","1");
        readVolley.addParams("isRead",a.getIsRead()+"");
        token = userInfos.getUser().getToken();
        readVolley.requestPost(Const.Request.messageRead,getHandler(),token
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
    }
    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };

    BroadcastReceiver refreshTabReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };
}
