package com.cnsunway.saas.wash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.CancelOrderDialog2;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.dialog.ShareGiftDialog;
import com.cnsunway.saas.wash.dialog.ShareGiftDialog2;
import com.cnsunway.saas.wash.dialog.WayOfShareDialog;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.DateUtil;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Cloth;
import com.cnsunway.saas.wash.model.DirectInfo;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.ShareInfo;
import com.cnsunway.saas.wash.model.ShippingFee;
import com.cnsunway.saas.wash.resp.OrderDetailResp;
import com.cnsunway.saas.wash.resp.ShareKeyResp;
import com.cnsunway.saas.wash.resp.ShippingFeeResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.FontUtil;
import com.cnsunway.saas.wash.util.NumberUtil;
import com.cnsunway.saas.wash.util.ShareUtil;
import com.cnsunway.saas.wash.view.MySeekBar;
import com.cnsunway.saas.wash.viewmodel.HomeViewModel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.ButterKnife;
public class OrderDetailActivity2 extends LoadingActivity implements OnClickListener,CancelOrderDialog2.OnCancelOrderOkLinstener,ShareGiftDialog2.OnShareBtnClickedLinstener{
    TextView titleText;
    String orderNo;
    JsonVolley orderDetailVolley;
    RelativeLayout orderDetailParent;
    Order order;
    TextView orderNoText, orderAppointTimeText, orderUnameText, orderUphoneText, orderUAddrText;
    LinearLayout senderInfoParent;
    LinearLayout oneBtnOperationParent,twoBtnOperationParent;
    TextView oneOperationText,leftOperationText,rightOperationText;
    TextView operatorNameText, operatorMoileText;
    LinearLayout clothesDetailParent;
    TextView totalPriceText;
    TextView feeText,feeTipText;
    LinearLayout layoutOrderDetail;
    private static final int OPERATION_ORDER_CANCEL = 1;
    private static final int OPERATION_ORDER_PAY = 2;
    private static final int OPERATION_ORDER_SHARE = 3;
    private static final int OPERATION_ORDER_REMARK = 4;
    private static final int OPERATION_DO_ORDER = 5;
    private static final int OPERATION_ORDER_CONFIRM = 6;
    /* CancelOrderDialog cancelOrderDialog;*/
    CancelOrderDialog2 cancelOrderDialog;
    ShippingFee shippingFee;
    private Timer refreshTimer;
    private Timer timer;
    @Bind(R.id.tv_time)
    TextView tvTime;
    RelativeLayout networkLay;
    TextView orderMemoText;
    WayOfShareDialog wayOfShareDialog;
    boolean showGift = false;
    MySeekBar orderProgressBar;
    TextView statusText;
    ImageView dotImage;
    private final int PROGRESS_MAX = 100;
    TextView statusText1;
    TextView statusText2;
    TextView statusText3;
    TextView statusText4;
    TextView noMemoText;
    JsonVolley confirmDoneVolley;
    JsonVolley logVolley;
    JsonVolley shareKeyVolley;
    LocationForService locationForService;

    BroadcastReceiver shareSuccReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(getApplicationContext(),"call interface",Toast.LENGTH_SHORT).show();
            logVolley.requestPost(Const.Request.log,getHandler(),UserInfosPref.getInstance(getApplicationContext()).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
        }
    };
    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra("order_no");
        orderDetailVolley = new JsonVolley(this, Const.Message.MSG_ORDER_DETAIL_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);
        confirmDoneVolley = new JsonVolley(this,Const.Message.MSG_CONFIRM_DONE_SUCC,Const.Message.MSG_PAY_CONFIRM_FAIL);
        confirmDoneVolley.addParams("orderNo",orderNo);
        logVolley = new JsonVolley(this,Const.Message.LOG_SUCC,Const.Message.LOG_FAIL);
        shareKeyVolley = new JsonVolley(this,Const.Message.MSG_SHARE_KEY_SUCC,Const.Message.MSG_SHARE_KEY_FAIL);
        shareKeyVolley.addParams("orderNo",orderNo);
//       orderDetailVolley.addParams("orderNo", orderNo);
        cancelOrderDialog = new CancelOrderDialog2(this).builder();
        cancelOrderDialog.setOrderNo(orderNo);
        cancelOrderDialog.setCancelOkLinstener(this);
        wayOfShareDialog = new WayOfShareDialog(this).builder();
        registerReceiver(shareSuccReceiver,new IntentFilter(Const.MyFilter.FILTER_SHARE_SUCC));
    }
    @Override
    protected void initViews() {
        statusText1 = (TextView) findViewById(R.id.text_status1);
        statusText2 = (TextView) findViewById(R.id.text_status2);
        statusText3 = (TextView) findViewById(R.id.text_status3);
        statusText4 = (TextView) findViewById(R.id.text_status4);
        noMemoText = (TextView) findViewById(R.id.text_no_memo);
        layoutOrderDetail=(LinearLayout)findViewById(R.id.ll_order_detail);
        dotImage = (ImageView) findViewById(R.id.image_arrow);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText(R.string.order_detail);
        orderNoText = (TextView) findViewById(R.id.text_order_detail_no);
        orderAppointTimeText = (TextView) findViewById(R.id.text_order_detail_appoint_time);
        orderUnameText = (TextView) findViewById(R.id.text_order_detail_username);
        orderUphoneText = (TextView) findViewById(R.id.text_order_detail_user_phone);
        orderUAddrText = (TextView) findViewById(R.id.text_order_detail_user_addr);
        orderDetailParent = (RelativeLayout) findViewById(R.id.order_deatail_parent);
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
        orderProgressBar = (MySeekBar) findViewById(R.id.order_progress_bar);
        statusText = (TextView) findViewById(R.id.text_status);
        orderProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int position = seekBar.getProgress();
                int x = seekBar.getWidth();
                int barWidth = (int)seekBar.getX();
                String text = statusText.getText().toString().trim();
                float width = 0.0f;
//                if(text.length() >= 4){
//                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 4.0f;
//                }else {
//                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 3f -2;
//                }
                width  = (position * x) / 100 - statusText.getWidth() / 2;
                Log.e("progress","progress:" + progress);
                if(progress <= 10){
                    width += 38;
                }else if(progress > 10 && progress < 50){
                    width += 28;
                } else if(progress > 90){
                    width -= 15;
                }

                statusText.setX(width);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

//                int position = seekBar.getProgress();
//                float x = seekBar.getWidth();
//                float barWidth = seekBar.getX();
//                String text = statusText.getText().toString().trim();
//                float width = 0.0f;
//                if(text.length() >= 4){
//                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 4.0f;
//                }else {
//                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 3f - 5;
//                }
//
//                statusText.setX(width);

            }
        });
        networkLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                orderDetailVolley.requestGet(Const.Request.detail + "/" +orderNo , getHandler(), UserInfosPref.getInstance(OrderDetailActivity2.this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                );
                showCenterLoading();
            }
        });
        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        orderDetailVolley.requestGet(Const.Request.detail + "/" +orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
//        JsonVolley shippingFeeVolley = new JsonVolley(this, Const.Message.MSG_SHIPPINGFEE_SUCC,Const.Message.MSG_SHIPPINGFEE_FAIL);
//        shippingFeeVolley.requestGet(Const.Request.shippingFee, getHandler(), UserInfosPref.getInstance(this).getUser().getToken());
        showCenterLoading();
    }

    private void fillTextCheckNull(TextView textView, String content) {
        if (!TextUtils.isEmpty(content)) {
            textView.setText(content);
        }
    }

    private void fillOrderUserInfo(Order order){
        String appointTime = "";
        if (!TextUtils.isEmpty(order.getExpectDateB()) && !TextUtils.isEmpty(order.getExpectDateE())) {
            appointTime = DateUtil.getServerDate(order.getExpectDateB()).substring(0, order.getExpectDateB().length() - 3) + "-" + DateUtil.getServerDate(order.getExpectDateE()).substring(order.getExpectDateE().length() - 8, order.getExpectDateE().length() - 3);
        }
        fillTextCheckNull(orderNoText, order.getOrderNo());
        fillTextCheckNull(orderUnameText, order.getPickContact());
        fillTextCheckNull(orderUphoneText, order.getPickMobile());
        fillTextCheckNull(orderUAddrText, order.getPickAddress());
        fillTextCheckNull(orderAppointTimeText, appointTime);
        if(!TextUtils.isEmpty(order.getTotalPrice())){
            totalPriceText.setText("￥" + NumberUtil.format2Dicimal(order.getTotalPrice()));
        }else{
            totalPriceText.setText(R.string.to_be_priced);
        }
    }

    private void fillOrderSenderInfo(Order order){
        senderInfoParent.setVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(order.getPickerName())){
            if(TextUtils.isEmpty(order.getDelivererName())){
                operatorNameText.setText("取送员：暂无");
            }else {
                operatorNameText.setText("取送员：" + order.getDelivererName());
            }
        }else {
            operatorNameText.setText("取送员：" + order.getPickerName());
        }

        if (!TextUtils.isEmpty(order.getDelivererMobile())){
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

        }else if(!TextUtils.isEmpty(order.getPickerMobile())){
            operatorMoileText.setText(Html.fromHtml("<u>" + order.getPickerMobile() + "</u>"));
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
        }
        if(!TextUtils.isEmpty(order.getMemo())){
            noMemoText.setVisibility(View.GONE);
            orderMemoText.setText(getString(R.string.memo_tips) + order.getMemo());
        }else {
            noMemoText.setVisibility(View.VISIBLE);
        }
    }

    private void fillOrderSenderInfo(){
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
        if(!TextUtils.isEmpty(order.getMemo())){
            orderMemoText.setText(getString(R.string.memo_tips) + order.getMemo());
        }

    }

    private void fillOrderClotesDetailInfo(List<Cloth> items){
        clothesDetailParent.setVisibility(View.VISIBLE);
        clothesDetailParent.removeAllViews();
        for(Cloth item : items){
            View clothesItemView  = getLayoutInflater().inflate(R.layout.clothes_item,null);
            TextView clothesNameText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_name);
            TextView clothesPriceText = (TextView) clothesItemView.findViewById(R.id.clothes_item_price);
            TextView clothtesSubpriceText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_subprice);
            clothesNameText.setText(item.getProductName() + "x" + item.getSqmCount());
            clothesPriceText.setText(getString(R.string.price) +"￥" + item.getBasePrice());
            clothtesSubpriceText.setText("￥" + NumberUtil.format2Dicimal(item.getRealPrice() + ""));
            clothesDetailParent.addView(clothesItemView);
        }
        View orderFeeView = getLayoutInflater().inflate(R.layout.order_fee_item,null);
        feeText = (TextView)orderFeeView.findViewById(R.id.text_order_fee);
        feeTipText = (TextView)orderFeeView.findViewById(R.id.text_order_fee_desc);
        if(!TextUtils.isEmpty(order.getFreightInfo())){
            feeTipText.setText(order.getFreightInfo());
        }
        feeText.setText("￥" + NumberUtil.format2Dicimal(order.getFreightCharge()));
//        if(shippingFee != null && shippingFee.getTotalPrice() != null) {
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
        statusText.setText(HomeViewModel.getOrderStatus(order));
        startOrderProgress(order);
        fillOrderUserInfo(order);
        oneOperationText.setOnClickListener(this);
        leftOperationText.setOnClickListener(this);
        rightOperationText.setOnClickListener(this);
        int orderStatus = order.getOrderStatus();
        List<Cloth> items = order.getClothes();
        String action = order.getAction();
//        Log.e("--------------", "order status" + orderStatus);
        switch (orderStatus) {

            case Const.OrderStatus.ORDER_STATUS_CANCELED:
                break;
            case Const.OrderStatus.ORDER_STATUS_BOOKING:
                //预约完成

            case Const.OrderStatus.ORDER_STATUS_RESERVED:
                //待上门

                senderInfoParent.setVisibility(View.VISIBLE);
                clothesDetailParent.setVisibility(View.GONE);
                senderInfoParent.setVisibility(View.GONE);
                oneBtnOperationParent.setVisibility(View.VISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);
                oneOperationText.setText(R.string.operation_order_cancel);
                oneOperationText.setTag(OPERATION_ORDER_CANCEL);
                oneOperationText.setBackgroundResource(R.drawable.brown_corner);

                break;
            case Const.OrderStatus.ORDER_STATUS_PICKUP:
                //上门中
                clothesDetailParent.setVisibility(View.GONE);
                fillOrderSenderInfo(order);
                oneBtnOperationParent.setVisibility(View.VISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);
                oneOperationText.setText(R.string.operation_order_cancel);
                oneOperationText.setTag(OPERATION_ORDER_CANCEL);

                oneOperationText.setBackgroundResource(R.drawable.brown_corner);
                break;
            case Const.OrderStatus.ORDER_STATUS_WAIT_TO_PAY:

                fillOrderSenderInfo(order);
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(items);
                }else{
                    clothesDetailParent.setVisibility(View.GONE);
                }
                oneBtnOperationParent.setVisibility(View.INVISIBLE);
                twoBtnOperationParent.setVisibility(View.VISIBLE);
                leftOperationText.setText(R.string.operation_order_cancel);
                leftOperationText.setBackgroundResource(R.drawable.brown_corner);
                leftOperationText.setTag(OPERATION_ORDER_CANCEL);
                rightOperationText.setText(R.string.operation_order_pay);
                rightOperationText.setTag(OPERATION_ORDER_PAY);
                rightOperationText.setBackgroundResource(R.drawable.yellow_corner);
                //待支付
                break;
            case Const.OrderStatus.ORDER_STATUS_PAID:
                //已支付


            case Const.OrderStatus.ORDER_STATUS_FETCH_COMPELTE:
                //取件完成
                fillOrderSenderInfo(order);
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(items);
                }else{
                    clothesDetailParent.setVisibility(View.GONE);
                }
                if (action.equals("share")){
                    oneBtnOperationParent.setVisibility(View.VISIBLE);
                    ShareInfo shareInfo = order.getShareInfo();
                    oneOperationText.setText(shareInfo.getShareBtnText());
                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
                }else if(action.equals("direct")){
                    oneBtnOperationParent.setVisibility(View.VISIBLE);
                    DirectInfo directInfo = order.getDirectInfo();
                    oneOperationText.setText(directInfo.getBtnText());
                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
                }else {
                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
                }
                twoBtnOperationParent.setVisibility(View.INVISIBLE);


                break;

            case Const.OrderStatus.ORDER_STATUS_IN_STORE:


            case Const.OrderStatus.ORDER_STATUS_WASHING:
                //洗涤中

            case Const.OrderStatus.ORDER_STATUS_WASHING_FINISH:
                //洗涤完成

            case Const.OrderStatus.ORDER_STATUS_SENDING_BACK:
                fillOrderSenderInfo(order);
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(items);
                }else{
                    clothesDetailParent.setVisibility(View.GONE);
                }
                if (action.equals("share")){
                    oneBtnOperationParent.setVisibility(View.VISIBLE);
                    ShareInfo shareInfo = order.getShareInfo();
                    oneOperationText.setText(shareInfo.getShareBtnText());
                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
                }else if(action.equals("direct")){
                    oneBtnOperationParent.setVisibility(View.VISIBLE);
                    DirectInfo directInfo = order.getDirectInfo();
                    oneOperationText.setText(directInfo.getBtnText());
                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
                }else {
                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
                }
                twoBtnOperationParent.setVisibility(View.INVISIBLE);

                break;

            case Const.OrderStatus.ORDER_STATUS_DELIVERED:
                //已送达
                fillOrderSenderInfo(order);
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(items);
                }else{
                    clothesDetailParent.setVisibility(View.GONE);
                }

                if (action.equals("share")){
                    ShareInfo shareInfo = order.getShareInfo();
                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
                    twoBtnOperationParent.setVisibility(View.VISIBLE);
                    leftOperationText.setText(shareInfo.getShareBtnText());
                    leftOperationText.setBackgroundResource(R.drawable.blue_corner);
                    leftOperationText.setTag(OPERATION_ORDER_SHARE);
                    rightOperationText.setText(R.string.operation_order_confirm);
                    rightOperationText.setBackgroundResource(R.drawable.green_shape);
                    rightOperationText.setTag(OPERATION_ORDER_CONFIRM);
                }else if(action.equals("direct")){
                    DirectInfo directInfo = order.getDirectInfo();
                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
                    twoBtnOperationParent.setVisibility(View.VISIBLE);
                    leftOperationText.setText(directInfo.getBtnText());
                    leftOperationText.setBackgroundResource(R.drawable.blue_corner);
                    leftOperationText.setTag(OPERATION_ORDER_SHARE);
                    rightOperationText.setText(R.string.operation_order_confirm);
                    rightOperationText.setBackgroundResource(R.drawable.green_shape);
                    rightOperationText.setTag(OPERATION_ORDER_CONFIRM);
                }else {
                    oneBtnOperationParent.setVisibility(View.VISIBLE);
                    twoBtnOperationParent.setVisibility(View.INVISIBLE);
                    oneOperationText.setText(R.string.operation_order_confirm);
                    oneOperationText.setBackgroundResource(R.drawable.green_shape);
                    oneOperationText.setTag(OPERATION_ORDER_CONFIRM);
                }


                break;


            case Const.OrderStatus.ORDER_STATUS_COMPLETED:
                fillOrderSenderInfo();
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(items);
                }else{
                    clothesDetailParent.setVisibility(View.GONE);
                }
//                if(order.isEvaluable()){
                if(order.isEvaluable()){
                    if (action.equals("share")){
                        twoBtnOperationParent.setVisibility(View.VISIBLE);
                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
                        ShareInfo shareInfo = order.getShareInfo();
                        leftOperationText.setText(shareInfo.getShareBtnText());
                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
                        rightOperationText.setText(R.string.operation_order_remark);
                        rightOperationText.setBackgroundResource(R.drawable.orange_corner);
                        rightOperationText.setTag(OPERATION_ORDER_REMARK);
                    }else if(action.equals("direct")){
                        twoBtnOperationParent.setVisibility(View.VISIBLE);
                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
                        DirectInfo directInfo = order.getDirectInfo();
                        leftOperationText.setText(directInfo.getBtnText());
                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
                        rightOperationText.setText(R.string.operation_order_remark);
                        rightOperationText.setBackgroundResource(R.drawable.orange_corner);
                        rightOperationText.setTag(OPERATION_ORDER_REMARK);
                    }else {
                        oneBtnOperationParent.setVisibility(View.VISIBLE);
                        twoBtnOperationParent.setVisibility(View.GONE);
                        oneOperationText.setText(R.string.operation_order_remark);
                        oneOperationText.setBackgroundResource(R.drawable.orange_corner);
                        oneOperationText.setTag(OPERATION_ORDER_REMARK);
                    }

                }else {
                    //分享有礼和再来一单
                    if (action.equals("share")){
                        twoBtnOperationParent.setVisibility(View.VISIBLE);
                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
                        ShareInfo shareInfo = order.getShareInfo();
                        leftOperationText.setText(shareInfo.getShareBtnText());
                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
                        rightOperationText.setText(R.string.more_order);
                        rightOperationText.setBackgroundResource(R.drawable.gray_border);
                        rightOperationText.setTag(OPERATION_DO_ORDER);
                        rightOperationText.setTextColor(Color.parseColor("#444A59"));

                    }else if(action.equals("direct")){
                        twoBtnOperationParent.setVisibility(View.VISIBLE);
                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
                        DirectInfo directInfo = order.getDirectInfo();
                        leftOperationText.setText(directInfo.getBtnText());
                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
                        rightOperationText.setText(R.string.more_order);
                        rightOperationText.setBackgroundResource(R.drawable.gray_border);
                        rightOperationText.setTag(OPERATION_DO_ORDER);
                        rightOperationText.setTextColor(Color.parseColor("#444A59"));
                    }else {
                        oneBtnOperationParent.setVisibility(View.VISIBLE);
                        twoBtnOperationParent.setVisibility(View.GONE);
                        oneOperationText.setText(R.string.more_order);
                        oneOperationText.setTextColor(Color.parseColor("#444A59"));
                        oneOperationText.setBackgroundResource(R.drawable.gray_border);
                        oneOperationText.setTag(OPERATION_DO_ORDER);
                    }
                }
//                if (action.equals("share")){
//                    oneBtnOperationParent.setVisibility(View.VISIBLE);
//                    ShareInfo shareInfo = order.getShareInfo();
//                    oneOperationText.setText(shareInfo.getShareBtnText());
//                    oneOperationText.setBackgroundResource(R.drawable.yellow_corner);
//                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                }else if(action.equals("direct")){
//                    oneBtnOperationParent.setVisibility(View.VISIBLE);
//                    DirectInfo directInfo = order.getDirectInfo();
//                    oneOperationText.setText(directInfo.getBtnText());
//                    oneOperationText.setBackgroundResource(R.drawable.yellow_corner);
//                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                }else {
//                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                }

                break;
            case Const.OrderStatus.ORDER_STATUS_PAID_EXCEPTION:
                //支付异常
                break;

        }

    }

    Timer timer2;
    int progress = 0;
    private void startOrderProgress(Order order){
        timer2 = new Timer();
        switch (order.getOrderStatus()) {
            case Const.OrderStatus.ORDER_STATUS_CANCELED:
                progress = PROGRESS_MAX -5;
                dotImage.setImageResource(R.mipmap.clock5);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer5));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb5));
                statusText1.setSelected(false);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
            break;
            case Const.OrderStatus.ORDER_STATUS_BOOKING:
                dotImage.setImageResource(R.mipmap.clock1);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer1));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb1));
                progress = PROGRESS_MAX / 3  / 6;
                statusText1.setSelected(true);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                //预约完成
                break;

            case Const.OrderStatus.ORDER_STATUS_RESERVED:
                //待上门
                progress = PROGRESS_MAX / 3  / 6 * 2;
                dotImage.setImageResource(R.mipmap.clock1);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer1));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb1));
                statusText1.setSelected(true);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                break;
            case Const.OrderStatus.ORDER_STATUS_PICKUP:
                progress = PROGRESS_MAX / 3  / 6 * 3;
                dotImage.setImageResource(R.mipmap.clock1);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer1));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb1));
                statusText1.setSelected(true);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                //上门中
                break;
            case Const.OrderStatus.ORDER_STATUS_WAIT_TO_PAY:
                progress = PROGRESS_MAX / 3  / 6 * 4;
                dotImage.setImageResource(R.mipmap.clock2);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer2));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb2));

                statusText1.setSelected(true);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);

                //待支付
                break;
            case Const.OrderStatus.ORDER_STATUS_PAID:
                progress = PROGRESS_MAX / 3  / 6 * 5;
                dotImage.setImageResource(R.mipmap.clock2);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer2));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb2));

                statusText1.setSelected(true);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                //已支付

                break;
            case Const.OrderStatus.ORDER_STATUS_FETCH_COMPELTE:
                progress = PROGRESS_MAX / 3  ;

                dotImage.setImageResource(R.mipmap.clock2);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer2));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb2));
                //取件完成
                statusText1.setSelected(true);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_IN_STORE:

                progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 / 3;
                dotImage.setImageResource(R.mipmap.clock3);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer3));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb3));
                //入库检验
                statusText1.setSelected(false);
                statusText2.setSelected(true);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_WASHING:
                progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /3 * 2;
                dotImage.setImageResource(R.mipmap.clock3);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer3));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb3));
                //洗涤中
                statusText1.setSelected(false);
                statusText2.setSelected(true);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_WASHING_FINISH:

//               if(HomeViewModel.getOrderStatus(order).equals("杀菌中")){
//                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /3 * 2 + PROGRESS_MAX / 3 /3 / 3 / 5;
//
//               }else if(HomeViewModel.getOrderStatus(order).equals("护理中")){
//                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /3 * 2 + PROGRESS_MAX / 3 /3 / 3 / 5 * 1;
////                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3  /5 * 2;
//               }else if(HomeViewModel.getOrderStatus(order).equals("质检中")){
////                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3  /5 * 3;
//                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /3 * 2 + PROGRESS_MAX / 3 /3 / 3 / 5 * 2;
//               }else if(HomeViewModel.getOrderStatus(order).equals("包装中")){
////                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3  / 5* 4;
//                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /3 * 2 + PROGRESS_MAX / 3 /3 / 3 / 5 * 3;
//               }else
                if(HomeViewModel.getOrderStatus(order).equals("待出库")){
//                   progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /5 * 5;
                    progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /3 * 2 + PROGRESS_MAX / 3 /3 / 3 / 5 * 5;
                }


//               progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3;
                dotImage.setImageResource(R.mipmap.clock3);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer3));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb3));
                //洗涤完成
                statusText1.setSelected(false);
                statusText2.setSelected(true);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_SENDING_BACK:
                progress = 2 * PROGRESS_MAX / 3 + PROGRESS_MAX / 3 / 3;
                dotImage.setImageResource(R.mipmap.clock4);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer4));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb4));
                // 送返中
                statusText1.setSelected(false);
                statusText2.setSelected(false);
                statusText3.setSelected(true);
                statusText4.setSelected(false);

                break;

            case Const.OrderStatus.ORDER_STATUS_DELIVERED:
                progress = 2 * PROGRESS_MAX / 3 + PROGRESS_MAX / 3 / 3 * 2;
                dotImage.setImageResource(R.mipmap.clock4);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer4));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb4));
                //已送达
                statusText1.setSelected(false);
                statusText2.setSelected(false);
                statusText3.setSelected(true);
                statusText4.setSelected(false);

                break;


            case Const.OrderStatus.ORDER_STATUS_COMPLETED:
                progress = PROGRESS_MAX -5;
                dotImage.setImageResource(R.mipmap.clock5);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer5));
                orderProgressBar.setThumb(getResources().getDrawable(R.mipmap.thumb5));
                statusText1.setSelected(false);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(true);
                //已完成

                break;
            case Const.OrderStatus.ORDER_STATUS_PAID_EXCEPTION:
                //支付异常
                break;
        }
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = Const.Message.MSG_PROGRESS_INCREASED;
                msg.arg1 = progress;
                msg.sendToTarget();
            }
        }, 0 * 1000, 30);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRefreshTimer();
        stopTimer();
        ButterKnife.unbind(this);
        unregisterReceiver(shareSuccReceiver);
    }

  /*  @Override
    protected void onNewIntent(Intent intent) {

        Toast.makeText(getApplication(),"onNewIntent",Toast.LENGTH_SHORT).show();
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(getApplication(),"onRestoreInstanceState",Toast.LENGTH_SHORT).show();
    }*/

    boolean clockOn;

    @Override
    protected void handlerMessage(Message msg) {

        switch (msg.what) {
            case Const.Message.MSG_ORDER_DETAIL_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
//                    Log.e("---------","order_detail:" + msg.obj);
                    OrderDetailResp detailResp = (OrderDetailResp) JsonParser.jsonToObject(msg.obj + "", OrderDetailResp.class);
                    order = detailResp.getData();
                    clockOn = order.isClockOn();
                    logVolley.addParams("orderNo",order.getOrderNo());
                    if(order.getShareInfo() != null && !TextUtils.isEmpty(order.getShareInfo().getAppSharePromocode())){
                        logVolley.addParams("promoCode",order.getShareInfo().getAppSharePromocode());
                    }

                    initTopView(order);
                    if(showGift){
                        if(TextUtils.isEmpty(order.getAction())){
                            return;
                        }
//                        if(order.getAction().equals("share") || order.getAction().equals("direct")){
//                            ShareGiftDialog2 shareGiftDialog  =  new ShareGiftDialog2(this,order).builder();
//                            shareGiftDialog.setShareBtnClickedLinstener(this);
//                            shareGiftDialog.show();
//                        }
                        Intent intent = new Intent(this,WebActivity.class);
                        intent.putExtra("url",Const.Request.paySuccess + detailResp.getData().getTotalPrice());
                        intent.putExtra("title","支付成功");
                        intent.putExtra("order",JsonParser.objectToJsonStr(order));

//                        if(!TextUtils.isEmpty(UserInfosPref.getInstance(this).getUser().getMobile())){
//                            UmengEventHelper.payEvent(this,UserInfosPref.getInstance(this).getUser().getMobile());
//                        }
                        startActivity(intent);
                        showGift = false;
                    }
                    fillDetailViewByOrder(order);
                    startTimer();
                    startRefreshTimer();
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    orderDetailParent.setVisibility(View.INVISIBLE);
                    showNoData(msg.obj + "");
                }
                break;

            case Const.Message.MSG_ORDER_DETAIL_FAIL:
                hideLoading();
                orderDetailParent.setVisibility(View.INVISIBLE);
                showNetFail();
                break;
            case Const.Message.MSG_SHIPPINGFEE_SUCC:
                if(msg.arg1 == Const.Request.REQUEST_SUCC){
                    ShippingFeeResp resp = (ShippingFeeResp) JsonParser.jsonToObject(msg.obj + "", ShippingFeeResp.class);
                    if(resp != null && resp.getData() != null){
                        ShippingFee fee = resp.getData();
                        shippingFee = fee;
                        if(fee.getShippingFee() != null && fee.getTotalPrice() != null && feeTipText != null) {
                            if (order != null && order.getFreightCharge() != null && Integer.parseInt(order.getFreightCharge()) > 0) {
                                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_pay),fee.getTotalPrice()));
                            } else {
                                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_free),fee.getTotalPrice()));
                            }
                        }

                    }
                }else{
                }
                break;

            case Const.Message.MSG_SHIPPINGFEE_FAIL:
                break;

            case Const.Message.MSG_PROGRESS_INCREASED:
            {
                int progress = msg.arg1;
                int currentProgress = orderProgressBar.getProgress() + 1;
                if(currentProgress < progress){
                    orderProgressBar.setProgress(currentProgress);
                }else {
                    orderProgressBar.setProgress(progress);
                    timer2.cancel();
                }
            }
            break;

            case Const.Message.MSG_CONFIRM_DONE_SUCC:
                if(msg.arg1 == Const.Request.REQUEST_SUCC){
                    OperationToast.showOperationResult(this,"确认成功",0);
                    showCenterLoading();
                     locationForService = UserInfosPref.getInstance(this).getLocationServer();
                    orderDetailVolley.requestGet(Const.Request.detail + "/" +orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

                }else {
                    OperationToast.showOperationResult(this,"操作失败",0);
                }

                break;

            case Const.Message.MSG_CONFIRM_DONE_FAIL:
                OperationToast.showOperationResult(this,"操作失败",0);
                break;
            case Const.Message.LOG_SUCC:

                break;

            case Const.Message.LOG_FAIL:

                break;

            case Const.Message.MSG_SHARE_KEY_SUCC:
                if(msg.arg1 == Const.Request.REQUEST_SUCC){
                    ShareKeyResp resp = (ShareKeyResp) JsonParser.jsonToObject(msg.obj + "",ShareKeyResp.class);
                    if(!TextUtils.isEmpty(resp.getData().getShareKey())){
                        new ShareUtil(wayOfShareDialog).share(this,order,resp.getData().getShareKey());
                    }else {
                        OperationToast.showOperationResult(this,"操作失败",0);
                    }
                }else {

                }
                break;

            case Const.Message.MSG_SHARE_KEY_FAIL:
                OperationToast.showOperationResult(this,"操作失败",0);
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail2);
        ButterKnife.bind(this, this);
        super.onCreate(savedInstanceState);
        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        FontUtil.applyFont(this, layoutOrderDetail, "OpenSans-Regular.ttf");
    }

    public void back(View view) {
        finish();
    }



    @Override
    public void onClick(View view) {
        if(view == oneOperationText || view == leftOperationText || view == rightOperationText){
            int operation = (int) view.getTag();
            switch (operation){
                case OPERATION_ORDER_CANCEL:
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
                    remarkIntent.putExtra("sender_name", order.getDelivererName());
                    remarkIntent.putExtra("site_name",order.getSiteName());
                    startActivityForResult(remarkIntent, OPERATION_ORDER_REMARK);
                    Log.e("site name","site name:" + order.getSiteName());
                    break;
                case OPERATION_ORDER_SHARE:
                    String action = order.getAction();
                    if(TextUtils.isEmpty(action)){
                        return;
                    }
                    if(action.equals("share")){
                        if(!TextUtils.isEmpty(order.getShareInfo().getAppSharePromocode())){
                            shareKeyVolley.addParams("promoCode",order.getShareInfo().getAppSharePromocode());
                            setOperationMsg("加载中");
                             locationForService = UserInfosPref.getInstance(this).getLocationServer();
                            shareKeyVolley.requestPost(Const.Request.shareKey,this,getHandler(),UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                            );
                        }

                    }else {
                        new ShareUtil(wayOfShareDialog).share(this,order);
                    }

                    break;

                case OPERATION_DO_ORDER:
                    startActivity(new Intent(this, DoOrderActivity.class));
                    break;

                case OPERATION_ORDER_CONFIRM:
//                    OperationToast.showOperationResult(this,"confirm",0);
                    setOperationMsg(getString(R.string.operating));
                     locationForService = UserInfosPref.getInstance(this).getLocationServer();
                    confirmDoneVolley.requestPost(Const.Request.confirmDone,this,getHandler(),UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                    break;

            }
        }
    }

    ShareGiftDialog shareGiftDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPERATION_ORDER_PAY && resultCode == RESULT_OK) {
            showGift = true;
            setOperationMsg(getString(R.string.refreshing));
             locationForService = UserInfosPref.getInstance(this).getLocationServer();
            orderDetailVolley.requestGet(Const.Request.detail + "/" + orderNo, this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );
            sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));

//            shareGiftDialog  =  new ShareGiftDialog(this,order).builder();
//            shareGiftDialog.setShareBtnClickedLinstener(this);
//            shareGiftDialog.show();
//            Log.e("action", "action:" + order.getAction());

        }else if(requestCode == OPERATION_ORDER_REMARK && resultCode == RESULT_OK){
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
        sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_TABS));
        finish();
    }

    long timeInterval = 0;
    public void initTopView(Order order){
        if(tvTime != null && order != null ) {
            if(!TextUtils.isEmpty(order.getOrderTime())){
                tvTime.setText(HomeViewModel.getOrderTimeForDetail3(order.getOrderTime(),timeInterval));
                if(clockOn){
                    timeInterval += 1;
                }

            }else{
                tvTime.setText(HomeViewModel.getOrderTimeForDetail2(order));
            }
//            tvTime.setText(HomeViewModel.getOrderTimeForDetail2(order));

        }

    }
    private void startRefreshTimer(){
        if(isFinishing()){
            return;
        }
        if(refreshTimer != null){
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

    private void stopRefreshTimer(){
        if(refreshTimer != null){
            refreshTimer.cancel();
            refreshTimer = null;
        }
    }

    private void refreshDetail(){
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        orderDetailVolley.requestGet(Const.Request.detail+ "/" +orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    private void startTimer(){
        if(timer != null){
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

    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void shareBtnClicked() {
        new ShareUtil(wayOfShareDialog).share(this,order);
    }
}
