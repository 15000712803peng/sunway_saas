package com.cnsunway.wash.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.CancelOrderDialog;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.framework.utils.DateUtil;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.Cloth;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.model.Order;
import com.cnsunway.wash.model.ShippingFee;
import com.cnsunway.wash.resp.OrderDetailResp;
import com.cnsunway.wash.resp.ShippingFeeResp;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.util.FontUtil;
import com.cnsunway.wash.view.OrderStatusView;
import com.cnsunway.wash.viewmodel.HomeViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BackWashOrderDetailActivity extends LoadingActivity implements OnClickListener, CancelOrderDialog.OnCancelOrderOkLinstener {
    TextView titleText;
    String orderNo;
    JsonVolley orderDetailVolley;
    LinearLayout orderDetailParent;
    Order order;
    TextView orderNoText, orderAppointTimeText, orderUnameText, orderUphoneText, orderUAddrText;
    LinearLayout senderInfoParent;
    LinearLayout oneBtnOperationParent, twoBtnOperationParent;
    TextView oneOperationText, leftOperationText, rightOperationText;
    TextView operatorNameText, operatorMoileText;
    LinearLayout clothesDetailParent;
    TextView totalPriceText;
    TextView feeText, feeTipText;
    LinearLayout layoutBackWash;
    private static final int OPERATION_ORDER_CANCEL = 1;
    private static final int OPERATION_ORDER_PAY = 2;
    private static final int OPERATION_ORDER_SHARE = 3;
    private static final int OPERATION_ORDER_REMARK = 4;
    CancelOrderDialog cancelOrderDialog;
    ShippingFee shippingFee;
    private Timer refreshTimer;
    private Timer timer;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.order_status)
    OrderStatusView orderStatus;
    @Bind(R.id.order_substatus)
    OrderStatusView orderSubStatus;
    @Bind(R.id.tv_substatus)
    TextView tvSubStatus;
    RelativeLayout networkLay;
    TextView orderMemoText;

    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra("order_no");
        orderDetailVolley = new JsonVolley(this, Const.Message.MSG_ORDER_DETAIL_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);
        cancelOrderDialog = new CancelOrderDialog(this).builder();
        cancelOrderDialog.setOrderNo(orderNo);
        cancelOrderDialog.setCancelOkLinstener(this);
    }

    @Override
    protected void initViews() {
        layoutBackWash = (LinearLayout) findViewById(R.id.ll_back_wash);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText(R.string.order_detail);
        orderNoText = (TextView) findViewById(R.id.text_order_detail_no);
        orderAppointTimeText = (TextView) findViewById(R.id.text_order_detail_appoint_time);
        orderUnameText = (TextView) findViewById(R.id.text_order_detail_username);
        orderUphoneText = (TextView) findViewById(R.id.text_order_detail_user_phone);
        orderUAddrText = (TextView) findViewById(R.id.text_order_detail_user_addr);
        orderDetailParent = (LinearLayout) findViewById(R.id.order_deatail_parent);
        senderInfoParent = (LinearLayout) findViewById(R.id.sender_info_parent);
        oneBtnOperationParent = (LinearLayout) findViewById(R.id.one_btn_operation_parent);
        twoBtnOperationParent = (LinearLayout) findViewById(R.id.two_btn_operation_parent);
        oneOperationText = (TextView) findViewById(R.id.text_one_operation);
        leftOperationText = (TextView) findViewById(R.id.text_left_operation);
        rightOperationText = (TextView) findViewById(R.id.text_right_operation);
        operatorNameText = (TextView) findViewById(R.id.text_order_operation_name);
        operatorMoileText = (TextView) findViewById(R.id.text_order_operation_mobile);
        clothesDetailParent = (LinearLayout) findViewById(R.id.order_clothes_detail_parent);
        totalPriceText = (TextView) findViewById(R.id.text_order_total_price);
        networkLay = (RelativeLayout) findViewById(R.id.rl_network_fail);
        orderMemoText = (TextView) findViewById(R.id.text_order_memo);

        networkLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                LocationForService locationForService = UserInfosPref.getInstance(BackWashOrderDetailActivity.this).getLocationServer();
                orderDetailVolley.requestGet(Const.Request.detail + "/" + orderNo, getHandler(), UserInfosPref.getInstance(BackWashOrderDetailActivity.this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                );
                showCenterLoading();
            }
        });
        LocationForService locationForService = UserInfosPref.getInstance(BackWashOrderDetailActivity.this).getLocationServer();

        orderDetailVolley.requestGet(Const.Request.detail + "/" + orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
//        JsonVolley shippingFeeVolley = new JsonVolley(this, Const.Message.MSG_SHIPPINGFEE_SUCC, Const.Message.MSG_SHIPPINGFEE_FAIL);
//        shippingFeeVolley.requestGet(Const.Request.shippingFee, getHandler(), UserInfosPref.getInstance(this).getUser().getToken());
        showCenterLoading();
    }

    private void fillTextCheckNull(TextView textView, String content) {
        if (!TextUtils.isEmpty(content)) {
            textView.setText(content);
        }
    }

    private void fillOrderUserInfo(Order order) {
        String appointTime = "";
        if(order.getCreatedDate() != null){
            appointTime = DateUtil.getServerDate(order.getCreatedDate());
        }
        fillTextCheckNull(orderNoText, order.getOrderNo());
        fillTextCheckNull(orderUnameText, order.getPickContact());
        fillTextCheckNull(orderUphoneText, order.getPickMobile());
        fillTextCheckNull(orderUAddrText, order.getPickAddress());
        fillTextCheckNull(orderAppointTimeText, appointTime);
        totalPriceText.setText("￥0.00");
    }

    private void fillOrderSenderInfo(Order order) {
        senderInfoParent.setVisibility(View.VISIBLE);
        fillTextCheckNull(operatorNameText, getString(R.string.sender_name_tips) + order.getPickerName());
        if (!TextUtils.isEmpty(order.getDelivererMobile())) {
            operatorMoileText.setText(Html.fromHtml("<u>" + order.getDelivererMobile() + "</u>"));
            operatorMoileText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNum = operatorMoileText.getText().toString().trim();
                    if (!TextUtils.isEmpty(phoneNum)) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                        startActivity(intent);
                    }
                }
            });
            if (!TextUtils.isEmpty(order.getMemo())) {
                orderMemoText.setText(getString(R.string.memo_tips) + order.getMemo());
            }
        }
    }

    private void fillOrderSenderInfo() {
        senderInfoParent.setVisibility(View.VISIBLE);
        operatorNameText.setText(R.string.contact_kefu);


        operatorMoileText.setText(Html.fromHtml("<u>" + "4009-210-682" + "</u>"));
        operatorMoileText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = operatorMoileText.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNum)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                    startActivity(intent);
                }
            }
        });
        if (!TextUtils.isEmpty(order.getMemo())) {
            orderMemoText.setText(getString(R.string.memo_tips) + order.getMemo());
        }

    }

    private void fillOrderClotesDetailInfo(List<Cloth> items) {
        clothesDetailParent.setVisibility(View.VISIBLE);
        clothesDetailParent.removeAllViews();
        for (Cloth item : items) {
            View clothesItemView = getLayoutInflater().inflate(R.layout.clothes_item, null);
            TextView clothesNameText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_name);
            TextView clothesPriceText = (TextView) clothesItemView.findViewById(R.id.clothes_item_price);
            TextView clothtesSubpriceText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_subprice);
            clothesNameText.setText(item.getName() + "x" + item.getCount());
            clothesPriceText.setText(getString(R.string.price) + "￥" + item.getPrice());
            clothtesSubpriceText.setText("￥0.00");
            clothesDetailParent.addView(clothesItemView);
        }
        View orderFeeView = getLayoutInflater().inflate(R.layout.order_fee_item, null);
        feeText = (TextView) orderFeeView.findViewById(R.id.text_order_fee);
        feeTipText = (TextView) orderFeeView.findViewById(R.id.text_order_fee_desc);
        feeText.setVisibility(View.INVISIBLE);
//        feeText.setText("￥0.00");
//        if (shippingFee != null && shippingFee.getTotalPrice() != null) {3
//            if (order != null && order.getFreightCharge() != null && Integer.parseInt(order.getFreightCharge()) > 0) {
//                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_pay), shippingFee.getTotalPrice()));
//            } else {
//                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_free), shippingFee.getTotalPrice()));
//            }
//        }
        clothesDetailParent.addView(orderFeeView);
    }

    private void fillDetailViewByOrder(Order order) {
        orderDetailParent.setVisibility(View.VISIBLE);
        fillOrderUserInfo(order);
        oneOperationText.setOnClickListener(this);
        leftOperationText.setOnClickListener(this);
        rightOperationText.setOnClickListener(this);
        int orderStatus = order.getOrderStatus();
        List<Cloth> items = order.getClothes();
//        Log.e("--------------", "order status" + orderStatus);
        switch (orderStatus) {
            case Const.OrderStatus.ORDER_STATUS_BOOKING:
                //预约完成

            case Const.OrderStatus.ORDER_STATUS_RESERVED:
                //待上门

                senderInfoParent.setVisibility(View.VISIBLE);
                clothesDetailParent.setVisibility(View.GONE);
                senderInfoParent.setVisibility(View.GONE);
                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);

                break;
            case Const.OrderStatus.ORDER_STATUS_PICKUP:
                //上门中

                clothesDetailParent.setVisibility(View.GONE);
                fillOrderSenderInfo(order);
                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);

                break;
            case Const.OrderStatus.ORDER_STATUS_WAIT_TO_PAY:


                fillOrderSenderInfo(order);
                if (!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0) {
                    fillOrderClotesDetailInfo(items);
                } else {
                    clothesDetailParent.setVisibility(View.GONE);
                }
                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);

                break;
            case Const.OrderStatus.ORDER_STATUS_PAID:
                //已支付


            case Const.OrderStatus.ORDER_STATUS_FETCH_COMPELTE:
                //取件完成
                fillOrderSenderInfo(order);
                if (!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0) {
                    fillOrderClotesDetailInfo(items);
                } else {
                    clothesDetailParent.setVisibility(View.GONE);
                }
                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);

                break;

            case Const.OrderStatus.ORDER_STATUS_IN_STORE:


            case Const.OrderStatus.ORDER_STATUS_WASHING:
                //洗涤中

            case Const.OrderStatus.ORDER_STATUS_WASHING_FINISH:
                //洗涤完成

                fillOrderSenderInfo();
                if (items != null && items.size() > 0) {
                    fillOrderClotesDetailInfo(items);
                } else {
                    clothesDetailParent.setVisibility(View.GONE);
                }
                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);

                break;

            case Const.OrderStatus.ORDER_STATUS_SENDING_BACK:
                //送返中

                fillOrderSenderInfo(order);
                if (!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0) {
                    fillOrderClotesDetailInfo(items);
                } else {
                    clothesDetailParent.setVisibility(View.GONE);
                }
                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);

                break;

            case Const.OrderStatus.ORDER_STATUS_DELIVERED:
                //已送达
                fillOrderSenderInfo(order);
                if (!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0) {
                    fillOrderClotesDetailInfo(items);
                } else {
                    clothesDetailParent.setVisibility(View.GONE);
                }
                    //评价
                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
                    twoBtnOperationParent.setVisibility(View.VISIBLE);
                    leftOperationText.setText(R.string.operation_order_remark);
                    leftOperationText.setBackgroundResource(R.drawable.brown_corner);
                    leftOperationText.setTag(OPERATION_ORDER_REMARK);
                    rightOperationText.setText(R.string.operation_order_share);
                    rightOperationText.setBackgroundResource(R.drawable.yellow_corner);
                    rightOperationText.setTag(OPERATION_ORDER_SHARE);
                    rightOperationText.setVisibility(View.GONE);


                break;


            case Const.OrderStatus.ORDER_STATUS_COMPLETED:
                fillOrderSenderInfo(order);
                if (!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0) {
                    fillOrderClotesDetailInfo(items);
                } else {
                    clothesDetailParent.setVisibility(View.GONE);
                }

                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);
                break;
            case Const.OrderStatus.ORDER_STATUS_PAID_EXCEPTION:
                //支付异常
                break;

        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        stopRefreshTimer();
        stopTimer();
        ButterKnife.unbind(this);
    }

    @Override
    protected void handlerMessage(Message msg) {

        switch (msg.what) {
            case Const.Message.MSG_ORDER_DETAIL_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    OrderDetailResp detailResp = (OrderDetailResp) JsonParser.jsonToObject(msg.obj + "", OrderDetailResp.class);
                    order = detailResp.getData();
                    initTopView(order);
                    fillDetailViewByOrder(order);
                    startTimer();
                    startRefreshTimer();
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    showNoData(msg.obj + "");
                }
                break;

            case Const.Message.MSG_ORDER_DETAIL_FAIL:
                hideLoading();
                showNetFail();
                break;

            case Const.Message.MSG_SHIPPINGFEE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    ShippingFeeResp resp = (ShippingFeeResp) JsonParser.jsonToObject(msg.obj + "", ShippingFeeResp.class);
                    if (resp != null && resp.getData() != null) {
                        ShippingFee fee = resp.getData();
                        shippingFee = fee;
                        if (fee.getShippingFee() != null && fee.getTotalPrice() != null && feeTipText != null) {
                            if (order != null && order.getFreightCharge() != null && Integer.parseInt(order.getFreightCharge()) > 0) {
                                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_pay), fee.getTotalPrice()));
                            } else {
                                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_free), fee.getTotalPrice()));
                            }
                        }

                    }
                } else {
                }
                break;

            case Const.Message.MSG_SHIPPINGFEE_FAIL:
                break;


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_back_wash_order_detail);
        ButterKnife.bind(this, this);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutBackWash, "OpenSans-Regular.ttf");
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == oneOperationText || view == leftOperationText || view == rightOperationText) {
            int operation = (int) view.getTag();
            switch (operation) {
                case OPERATION_ORDER_CANCEL:
                    Log.e("-------", "cancel");
                    cancelOrderDialog.show();
                    break;
                case OPERATION_ORDER_PAY:
                    Intent intent = new Intent(this, GetPayOrderActivity.class);
                    intent.putExtra("order_no", order.getOrderNo());
                    intent.putExtra("order_price", order.getTotalPrice());
                    startActivityForResult(intent, OPERATION_ORDER_PAY);
                    break;
                case OPERATION_ORDER_REMARK:
                    Intent remarkIntent = new Intent(this, EvaluateActivity.class);
                    remarkIntent.putExtra("order_no", order.getOrderNo());
                    remarkIntent.putExtra("sender_name", order.getPickerName());
                    remarkIntent.putExtra("site_name",order.getSiteName());
                    startActivityForResult(remarkIntent, OPERATION_ORDER_REMARK);
                    break;


            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPERATION_ORDER_REMARK && resultCode == RESULT_OK) {
            setOperationMsg(getString(R.string.refreshing));
            LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();

            orderDetailVolley.requestGet(Const.Request.detail + "/" + orderNo, this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );
        }
    }

    @Override
    public void cancelOk() {
        OperationToast.showOperationResult(this, R.string.cancel_succ);
        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
        sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
        finish();
    }

    public void initTopView(Order order) {
        orderStatus.setOrder(order);
        orderSubStatus.setOrderProgress(order, HomeViewModel.getOrderSubProgress(order));
        tvTime.setText(HomeViewModel.getOrderTimeForDetail(order));
        tvStatus.setText(HomeViewModel.getOrderStatusSummarize(order));
        tvSubStatus.setText(HomeViewModel.getVirtualStatusStr(order));
    }

    private void startRefreshTimer() {
        if (isFinishing()) {
            return;
        }
        if (refreshTimer != null) {
            stopRefreshTimer();
        }
        refreshTimer = new Timer();
        refreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshDetail();
            }
        }, 1000 * 60);
    }

    private void stopRefreshTimer() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer = null;
        }
    }

    private void refreshDetail() {
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        orderDetailVolley.requestGet(Const.Request.detail + "/" + orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken()
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    private void startTimer() {
        if (timer != null) {
            return;
        }
        timer = new Timer();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                initTopView(order);
            }
        };
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(runnable);
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
