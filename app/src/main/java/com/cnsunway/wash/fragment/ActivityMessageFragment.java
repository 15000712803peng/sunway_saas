package com.cnsunway.wash.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.activity.CouponActivity;
import com.cnsunway.wash.activity.WebActivity;
import com.cnsunway.wash.adapter.ActivityMessageAdapter;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.framework.utils.DateUtil;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.AllMessage;
import com.cnsunway.wash.model.AllMessageResp;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.view.XListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hp on 2017/6/6.
 */
public class ActivityMessageFragment extends BaseFragment implements XListView.IXListViewListener ,AdapterView.OnItemClickListener{
    XListView activityMessageLV;
    ActivityMessageAdapter activityMessageAdapter;
    List<AllMessage> allMessages;
    UserInfosPref userInfos;
    String token;
    JsonVolley messageVolley,readVolley;
    private int page = 1;
    int totalPage = 0;
    RelativeLayout noDataParent;
    TextView noDateText;
    LocationForService locationForService;
    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_MESSAGE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    activityMessageLV.setRefreshTime(DateUtil.getCurrentDate());
                    activityMessageLV.stopRefresh("刷新成功");
                    AllMessageResp initResp = (AllMessageResp) JsonParser.jsonToObject(msg.obj + "", AllMessageResp.class);
                    page = initResp.getData().getPaginator().getPage();
                    totalPage = initResp.getData().getPaginator().getTotalPages();
                    int totalCount = initResp.getData().getPaginator().getTotalCount();
                    if (totalCount == 0){
                        showNoMessage();
                        return;
                    }

                    activityMessageLV.setVisibility(View.VISIBLE);
                    noDataParent.setVisibility(View.GONE);
                    if (page==1){
                        activityMessageLV.setPullLoadEnable(true);
                        allMessages = initResp.getData().getResults();
//                        sort(allMessages);//时间排序
                        initList(allMessages);
                    }else if (initResp.getData().getPaginator().getPage()>1){
                        allMessages = initResp.getData().getResults();
                        loadMoreList(allMessages);
                    }
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    activityMessageLV.stopRefresh("刷新失败");
                    activityMessageLV.stopLoadMore();
                    OperationToast.showOperationResult(getActivity(), "加载失败", 0);
                }
                break;
            case Const.Message.MSG_MESSAGE_FAIL:
                OperationToast.showOperationResult(getActivity(), "操作失败", 0);
                break;
            case Const.Message.MSG_READ_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC){
                    onRefresh();
                }else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    OperationToast.showOperationResult(getActivity(),"加载失败",0);
                }
                break;
            case Const.Message.MSG_READ_FAIL:
                OperationToast.showOperationResult(getActivity(),"操作失败",0);
                break;
        }

    }
    private void loadMoreList(List<AllMessage> allMessages) {
        activityMessageLV.stopLoadMore();
        if (allMessages == null) {
            return;
        }
        activityMessageAdapter.getMessagesList().addAll(allMessages);
        activityMessageAdapter.notifyDataSetChanged();
        if (page >= totalPage) {
            activityMessageLV.setPullLoadEnable(false);
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


    @Override
    protected void initFragmentDatas() {
        userInfos = UserInfosPref.getInstance(getContext());
        token = userInfos.getUser().getToken();
        messageVolley = new JsonVolley(getContext(), Const.Message.MSG_MESSAGE_SUCC,Const.Message.MSG_MESSAGE_FAIL);
        messageVolley.addParams("msgType",21);
//        messageVolley.addParams("msgType",22);
//        messageVolley.addParams("msgType",23);
//        messageVolley.addParams("userMobile","13764914819");
//        messageVolley.addParams("userMobile","13764914805");
       messageVolley.addParams("userMobile",UserInfosPref.getInstance(getActivity()).getUserName());
        Log.e("userName",UserInfosPref.getInstance(getActivity()).getUserName()) ;
        messageVolley.addParams("page",page);
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        messageVolley.requestPost(Const.Request.messageAll,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    protected void initMyViews(View view) {
        messageVolley.requestPost(Const.Request.messageAll,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            setView(inflater.inflate(R.layout.fragment_activity_message,container,false));
            activityMessageLV = (XListView) view.findViewById(R.id.lv_activity_message);
            activityMessageLV.setXListViewListener(this);
            activityMessageLV.setOnItemClickListener(this);
            activityMessageLV.setPullLoadEnable(true);
            noDataParent = (RelativeLayout)view.findViewById(R.id.rl_no_data);
            noDateText = (TextView) view.findViewById(R.id.tv_no_data);
            noDateText.setText("暂无活动消息");
            messageVolley.requestPost(Const.Request.messageAll,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );

        }

        return getView();

    }

    @Override
    public void onRefresh() {
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        page = 1;
        userInfos = UserInfosPref.getInstance(getContext());
        token = userInfos.getUser().getToken();
        messageVolley.addParams("page",page);
        messageVolley.addParams("userMobile",UserInfosPref.getInstance(getActivity()).getUserName());
        Log.e("userName1",UserInfosPref.getInstance(getActivity()).getUserName()) ;
        messageVolley.requestPost(Const.Request.messageAll,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    public void onLoadMore() {
        userInfos = UserInfosPref.getInstance(getContext());
        token = userInfos.getUser().getToken();
        int nextPage = page  + 1;
        Log.e("----","next page2:" + nextPage);
        messageVolley.addParams ("page",nextPage);
        messageVolley.requestPost(Const.Request.messageAll,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }
    private void showNoMessage(){
        activityMessageLV.setVisibility(View.GONE);
        noDataParent.setVisibility(View.VISIBLE);
    }
    private void initList(List<AllMessage> activityMessages){
        if (activityMessageAdapter == null){
            activityMessageAdapter = new ActivityMessageAdapter(this,activityMessages);
            activityMessageAdapter.setMessagesList(activityMessages);
            activityMessageLV.setAdapter(activityMessageAdapter);
        }else {
            activityMessageAdapter.clear();
            activityMessageAdapter.getMessagesList().addAll(activityMessages);
            activityMessageAdapter.notifyDataSetChanged();
        }
        if (page >= totalPage) {
            activityMessageLV.setPullLoadEnable(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        final AllMessage a = (AllMessage) adapterView.getAdapter().getItem(i);
        if (a.getMsgType() == 21){//21充值成功--余额明细
            String orderNo = a.getDepositOrderNo();
            Intent intent = new Intent(getActivity(), WebActivity.class);
//            intent.putExtra("order_no", orderNo);
            intent.putExtra("url", Const.Request.balance);
            intent.putExtra("title", "余额明细");
            startActivity(intent);
            readVolley = new JsonVolley(getContext(), Const.Message.MSG_READ_SUCC,Const.Message.MSG_READ_FAIL);

                    readVolley.addParams("msgId",a.getMsgId());
                    readVolley.addParams("msgType","21");
                    readVolley.addParams("isRead",a.getIsRead());
                    token = userInfos.getUser().getToken();
                    readVolley.requestPost(Const.Request.messageRead,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                    );


        }else if (a.getMsgType() == 22){//22优惠券过期提醒--优惠券
            startActivity(new Intent(getActivity(), CouponActivity.class));
            readVolley = new JsonVolley(getContext(), Const.Message.MSG_READ_SUCC,Const.Message.MSG_READ_FAIL);

                    readVolley.addParams("msgId",a.getMsgId());
                    readVolley.addParams("msgType","22");
                    readVolley.addParams("isRead",a.getIsRead());
                    token = userInfos.getUser().getToken();
                    readVolley.requestPost(Const.Request.messageRead,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                    );

        }else if (a.getMsgType() == 23){//23新活动上线提醒--活动详情页
            String url = a.getLinkUrl();
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("title", "活动");
            startActivity(intent);
            readVolley = new JsonVolley(getContext(), Const.Message.MSG_READ_SUCC,Const.Message.MSG_READ_FAIL);

                    readVolley.addParams("msgId",a.getMsgId());
                    readVolley.addParams("msgType","23");
                    readVolley.addParams("isRead",a.getIsRead());
                    readVolley.addParams("title",a.getTitle());
                    readVolley.addParams("picUrl",a.getPicUrl());
                    readVolley.addParams("content",a.getContent());
                    readVolley.requestPost(Const.Request.messageRead,getHandler(),token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                    );
        }
    }
}
