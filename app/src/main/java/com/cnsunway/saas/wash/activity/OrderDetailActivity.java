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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.cnsunway.saas.wash.model.Coupon;
import com.cnsunway.saas.wash.model.Coupons;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.PayData;
import com.cnsunway.saas.wash.model.ShippingFee;
import com.cnsunway.saas.wash.resp.GetPriceResp;
import com.cnsunway.saas.wash.resp.OrderDetailResp;
import com.cnsunway.saas.wash.resp.ShareKeyResp;
import com.cnsunway.saas.wash.resp.ShippingFeeResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.FontUtil;
import com.cnsunway.saas.wash.util.NumberUtil;
import com.cnsunway.saas.wash.util.ShareUtil;
import com.cnsunway.saas.wash.util.TextTool;
import com.cnsunway.saas.wash.view.MySeekBar;
import com.cnsunway.saas.wash.viewmodel.HomeViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderDetailActivity extends LoadingActivity implements OnClickListener,CancelOrderDialog2.OnCancelOrderOkLinstener,ShareGiftDialog2.OnShareBtnClickedLinstener{
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
    LinearLayout clothesDetailParentTop;
    LinearLayout clothesDetailParentCener;
    TextView totalPriceText,totalPriceText1;
    TextView totalPriceTextCenter;
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
    RelativeLayout bottomOperationParent;
    RelativeLayout bottomDividerParent;
    LinearLayout priceParentTop;
    LinearLayout priceParentCenter;
    LinearLayout noPriceParent;
    JsonVolley getPriceVolley;
    @Bind(R.id.text_sender_tips)
    TextView senderTipsText;
    ImageButton arrowImage;

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
        isShowContanct = getIntent().getBooleanExtra("is_show_contact",true);
        orderDetailVolley = new JsonVolley(this, Const.Message.MSG_ORDER_DETAIL_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);
        confirmDoneVolley = new JsonVolley(this,Const.Message.MSG_CONFIRM_DONE_SUCC,Const.Message.MSG_PAY_CONFIRM_FAIL);
        confirmDoneVolley.addParams("orderNo",orderNo);
        logVolley = new JsonVolley(this,Const.Message.LOG_SUCC,Const.Message.LOG_FAIL);
        shareKeyVolley = new JsonVolley(this,Const.Message.MSG_SHARE_KEY_SUCC,Const.Message.MSG_SHARE_KEY_FAIL);
        shareKeyVolley.addParams("orderNo",orderNo);
        getPriceVolley = new JsonVolley(this, Const.Message.MSG_GET_PAY_SUCC, Const.Message.MSG_GET_PAY_FAIL);
        getPriceVolley.addParams("orderNo", orderNo);
//       orderDetailVolley.addParams("orderNo", orderNo);
        cancelOrderDialog = new CancelOrderDialog2(this).builder();
        cancelOrderDialog.setOrderNo(orderNo);
        cancelOrderDialog.setCancelOkLinstener(this);
        wayOfShareDialog = new WayOfShareDialog(this).builder();
        registerReceiver(shareSuccReceiver,new IntentFilter(Const.MyFilter.FILTER_SHARE_SUCC));
    }

    TextView rightText;
    @Override
    protected void initViews() {
        statusText1 = (TextView) findViewById(R.id.text_status1);
        statusText2 = (TextView) findViewById(R.id.text_status2);
        statusText3 = (TextView) findViewById(R.id.text_status3);
        statusText4 = (TextView) findViewById(R.id.text_status4);
        noMemoText = (TextView) findViewById(R.id.text_no_memo);
        rightText = (TextView) findViewById(R.id.text_title_right);
//        if(isShowContanct){
//            contactUsImage.setVisibility(View.VISIBLE);
//        }else {
//            contactUsImage.setVisibility(View.GONE);
//        }

        priceParentTop = (LinearLayout) findViewById(R.id.price_parent_top);
        priceParentCenter = (LinearLayout) findViewById(R.id.price_parent_center);
        noPriceParent = (LinearLayout) findViewById(R.id.no_price_parent);
        bottomOperationParent = (RelativeLayout) findViewById(R.id.bottom_operation);
        bottomDividerParent = (RelativeLayout) findViewById(R.id.bottom_divider_parent);
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
        clothesDetailParentTop = (LinearLayout) findViewById(R.id.order_clothes_detail_parent_top);
        clothesDetailParentCener = (LinearLayout) findViewById(R.id.order_clothes_detail_parent_center);
        totalPriceText = (TextView) findViewById(R.id.text_order_total_price);
        totalPriceText1 = (TextView) findViewById(R.id.text_order_total_price1);
        totalPriceTextCenter = (TextView) findViewById(R.id.text_order_total_price_center);
        networkLay = (RelativeLayout) findViewById(R.id.rl_network_fail);
        orderMemoText = (TextView) findViewById(R.id.text_order_memo);
        orderProgressBar = (MySeekBar) findViewById(R.id.order_progress_bar);
        statusText = (TextView) findViewById(R.id.text_status);
//        orderProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                int position = seekBar.getProgress();
//                int x = seekBar.getWidth();
//                int barWidth = (int)seekBar.getX();
//                String text = statusText.getText().toString().trim();
//                float width = 0.0f;
////                if(text.length() >= 4){
////                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 4.0f;
////                }else {
////                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 3f -2;
////                }
//                width  = (position * x) / 100 - statusText.getWidth() / 2;
//                Log.e("progress","progress:" + progress);
//                if(progress <= 10){
//                    width += 38;
//                }else if(progress > 10 && progress < 50){
//                    width += 28;
//                } else if(progress > 90){
//                    width -= 15;
//                }
//
//                statusText.setX(width);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
////                int position = seekBar.getProgress();
////                float x = seekBar.getWidth();
////                float barWidth = seekBar.getX();
////                String text = statusText.getText().toString().trim();
////                float width = 0.0f;
////                if(text.length() >= 4){
////                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 4.0f;
////                }else {
////                    width  = (position * x) / 100.0f + barWidth - statusText.getWidth() / 3f - 5;
////                }
////
////                statusText.setX(width);

//
//            }
//        });
        networkLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                orderDetailVolley.requestGet(Const.Request.detail + "/" +orderNo , getHandler(), UserInfosPref.getInstance(OrderDetailActivity.this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                );
                showCenterLoading();
            }
        });
        if(UserInfosPref.getInstance(this).getUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else {
            locationForService = UserInfosPref.getInstance(this).getLocationServer();
            orderDetailVolley.requestGet(Const.Request.detail + "/" +orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
            showCenterLoading();
        }

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
            String price = "";
            if(TextUtils.isEmpty(order.getFreightCharge())){
                price = order.getTotalPrice();
            }else {
                price = new BigDecimal(order.getTotalPrice()).subtract(new BigDecimal(order.getFreightCharge())).floatValue() + "";
            }
            totalPriceText1.setText("￥" + NumberUtil.format2Dicimal(price));
            totalPriceText.setText("￥" + NumberUtil.format2Dicimal(price));
            totalPriceTextCenter.setText("￥" + NumberUtil.format2Dicimal(price));
        }else{
            totalPriceText.setText(R.string.to_be_priced);
        }
    }

    private void fillOrderSenderInfo(Order order){
        senderTipsText.setText("取送员：");
        senderInfoParent.setVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(order.getPickerName())){
            if(TextUtils.isEmpty(order.getDelivererName())){
                operatorNameText.setText("暂无");
            }else {
                operatorNameText.setText(order.getDelivererName());
            }
        }else {
            operatorNameText.setText(order.getPickerName());
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
        senderTipsText.setText(R.string.contact_kefu);
        senderInfoParent.setVisibility(View.VISIBLE);
        operatorNameText.setText("");
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
            noMemoText.setVisibility(View.GONE);
        }else {
            noMemoText.setVisibility(View.VISIBLE);
        }

    }

    private void fillOrderClotesDetailInfo(List<Cloth> items){
        priceParentCenter.setVisibility(View.GONE);
        priceParentTop.setVisibility(View.VISIBLE);
        clothesDetailParentTop.setVisibility(View.VISIBLE);
        clothesDetailParentTop.removeAllViews();
        for(Cloth item : items){
            View clothesItemView  = getLayoutInflater().inflate(R.layout.clothes_item,null);
            TextView clothesNameText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_name);
            TextView clothesPriceText = (TextView) clothesItemView.findViewById(R.id.clothes_item_price);
            TextView clothtesSubpriceText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_subprice);
            clothesNameText.setText(item.getProductName() + "x" + item.getSqmCount());
            clothesPriceText.setText(getString(R.string.price) +"￥" + item.getBasePrice());
            clothtesSubpriceText.setText("￥" + NumberUtil.format2Dicimal(item.getRealPrice() + ""));
            clothesDetailParentTop.addView(clothesItemView);
        }
//        View orderFeeView = getLayoutInflater().inflate(R.layout.order_fee_item,null);
//        feeText = (TextView)orderFeeView.findViewById(R.id.text_order_fee);
//        feeTipText = (TextView)orderFeeView.findViewById(R.id.text_order_fee_desc);
//        if(!TextUtils.isEmpty(order.getFreightInfo())){
//            feeTipText.setText(order.getFreightInfo());
//        }
//        feeText.setText("￥" + NumberUtil.format2Dicimal(order.getFreightCharge()));
//        if(shippingFee != null && shippingFee.getTotalPrice() != null) {
//            if (order != null && order.getFreightCharge() != null && Integer.parseInt(order.getFreightCharge()) > 0) {
//                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_pay), shippingFee.getTotalPrice()));
//            } else {
//                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_free), shippingFee.getTotalPrice()));
//            }
//        }
//        clothesDetailParentTop.addView(orderFeeView);
        noCouponsText = (TextView) findViewById(R.id.text_no_coupon);
        View couponsView = getLayoutInflater().inflate(R.layout.order_coupons,null);
        arrowImage = (ImageButton) couponsView.findViewById(R.id.arrow_rigth);
        feeText = (TextView)couponsView.findViewById(R.id.text_order_fee);
        feeTipText = (TextView)couponsView.findViewById(R.id.text_order_fee_desc);
        if(!TextUtils.isEmpty(order.getFreightInfo())){
            feeTipText.setText(order.getFreightInfo());
        }
        feeText.setText("￥" + NumberUtil.format2Dicimal(order.getFreightCharge()));
        couponsDesText = (TextView) couponsView.findViewById(R.id.text_coupon_desc);
        couponsDiscountText = (TextView) couponsView.findViewById(R.id.text_coupons_discount);
        noCouponsText = (TextView) couponsView.findViewById(R.id.text_no_coupon);
        hasCouponsParent = (LinearLayout) couponsView.findViewById(R.id.has_coupons_parent);
        copounsTips = (TextView) couponsView.findViewById(R.id.text_copouns_tips);
        textPayAmmout = (TextView) couponsView.findViewById(R.id.text_pay);
        selCouponParent = (RelativeLayout) couponsView.findViewById(R.id.sel_coupou_parent);
        selCouponParent.setOnClickListener(this);
        clothesDetailParentTop.addView(couponsView);
//        getPriceVolley.requestPost(Const.Request.pay+"/" + orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken()
//                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());



    }

    LinearLayout onLineParent;

    LinearLayout offLineParent;

    private void fillOrderClotesDetailInfo(Order order,List<Cloth> items,boolean hasPaied){
        String subPrice = order.getTotalPrice();
        String deductPrice = "0";
        priceParentTop.setVisibility(View.GONE);
        priceParentCenter.setVisibility(View.VISIBLE);
        clothesDetailParentCener.setVisibility(View.VISIBLE);
        clothesDetailParentTop.setVisibility(View.GONE);
        clothesDetailParentCener.removeAllViews();
        for(Cloth item : items){
            View clothesItemView  = getLayoutInflater().inflate(R.layout.clothes_item,null);
            TextView clothesNameText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_name);
            TextView clothesPriceText = (TextView) clothesItemView.findViewById(R.id.clothes_item_price);
            TextView clothtesSubpriceText = (TextView) clothesItemView.findViewById(R.id.clothes_itme_subprice);
            clothesNameText.setText(item.getProductName() + "x" + item.getSqmCount());
            clothesPriceText.setText(getString(R.string.price) +"￥" + item.getBasePrice());
            clothtesSubpriceText.setText("￥" + NumberUtil.format2Dicimal(item.getRealPrice() + ""));
            clothesDetailParentCener.addView(clothesItemView);
        }

//        if(shippingFee != null && shippingFee.getTotalPrice() != null) {
//            if (order != null && order.getFreightCharge() != null && Integer.parseInt(order.getFreightCharge()) > 0) {
//                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_pay), shippingFee.getTotalPrice()));
//            } else {
//                feeTipText.setText(String.format(getResources().getString(R.string.order_fee_free), shippingFee.getTotalPrice()));
//            }
//        }

//        clothesDetailParentCener.addView(orderFeeView);
        noCouponsText = (TextView) findViewById(R.id.text_no_coupon);
        View couponsView = getLayoutInflater().inflate(R.layout.order_coupons,null);

        feeText = (TextView)couponsView.findViewById(R.id.text_order_fee);
        feeTipText = (TextView)couponsView.findViewById(R.id.text_order_fee_desc);
        if(!TextUtils.isEmpty(order.getFreightInfo())){
            feeTipText.setText(order.getFreightInfo());
        }
        feeText.setText("￥" + NumberUtil.format2Dicimal(order.getFreightCharge()));
        couponsDesText = (TextView) couponsView.findViewById(R.id.text_coupon_desc);
        couponsDiscountText = (TextView) couponsView.findViewById(R.id.text_coupons_discount);
        noCouponsText = (TextView) couponsView.findViewById(R.id.text_no_coupon);
        hasCouponsParent = (LinearLayout) couponsView.findViewById(R.id.has_coupons_parent);
        copounsTips = (TextView) couponsView.findViewById(R.id.text_copouns_tips);
        textPayAmmout = (TextView) couponsView.findViewById(R.id.text_pay);
        onLineParent = (LinearLayout) couponsView.findViewById(R.id.ll_online);
        offLineParent = (LinearLayout) couponsView.findViewById(R.id.ll_offline);
//        selCouponParent = (RelativeLayout) couponsView.findViewById(R.id.sel_coupou_parent);
//        selCouponParent.setOnClickListener(this);
        clothesDetailParentCener.addView(couponsView);
        if(order.getCoupon() == null){
            //没有使用优惠券
            noCouponsText.setTextColor(getResources().getColor(R.color.text_gray));
            noCouponsText.setVisibility(View.VISIBLE);
            noCouponsText.setText("没有使用优惠券");
            hasCouponsParent.setVisibility(View.INVISIBLE);
        }else {
            if(!TextUtils.isEmpty(order.getCoupon().getAmount())){
                noCouponsText.setText("-￥" + NumberUtil.format2Dicimal(order.getCoupon().getAmount()));
                noCouponsText.setVisibility(View.VISIBLE);
                noCouponsText.setTextColor(getResources().getColor(R.color.red));
                hasCouponsParent.setVisibility(View.GONE);
                deductPrice = order.getCoupon().getAmount();
            }
            subPrice = new BigDecimal(subPrice).subtract(new BigDecimal(order.getCoupon().getAmount())).floatValue() +"";
        }

        if(TextUtils.isEmpty(order.getDeductedPrice()) || new BigDecimal(order.getDeductedPrice()).compareTo(new BigDecimal("0")) == 0){
            //没有门店折扣
            offLineParent.setVisibility(View.GONE);
            onLineParent.setVisibility(View.VISIBLE);

        }else {
            TextView des = (TextView) couponsView.findViewById(R.id.text_offline_desc);
            TextView deduct = (TextView) couponsView.findViewById(R.id.text_offline_deduct);
            deduct.setText(NumberUtil.format2Dicimal(order.getDeductedPrice()));
            deductPrice = order.getDeductedPrice();
            if(!TextUtils.isEmpty(order.getDeductMemo())){
                des.setText(order.getDeductMemo());
            }else {
                des.setVisibility(View.GONE);
            }
            offLineParent.setVisibility(View.VISIBLE);
            onLineParent.setVisibility(View.GONE);
            subPrice = new BigDecimal(subPrice).subtract(new BigDecimal(order.getDeductedPrice())).floatValue() +"";
        }
        textPayAmmout.setText("￥"+NumberUtil.format2Dicimal(subPrice));
        copounsTips.setText("优惠："  +"￥" + NumberUtil.format2Dicimal(deductPrice));

//        getPriceVolley.requestGet(Const.Request.pay, getHandler(), UserInfosPref.getInstance(this).getUser().getToken()
//                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

    }

    private void fillDetailViewByOrder(final Order order) {
        orderDetailParent.setVisibility(View.VISIBLE);
        statusText.setText(HomeViewModel.getOrderStatus(order));
        startOrderProgress(order);
        fillOrderUserInfo(order);
        if(arrowImage != null){
            arrowImage.setVisibility(View.GONE);
        }
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

                senderInfoParent.setVisibility(View.VISIBLE);
                clothesDetailParentTop.setVisibility(View.GONE);
                senderInfoParent.setVisibility(View.GONE);
                bottomOperationParent.setVisibility(View.GONE);
                fillOrderSenderInfo();
                bottomDividerParent.setVisibility(View.GONE);
                rightText.setVisibility(View.VISIBLE);
                rightText.setText("取消订单");
                rightText.setTextColor(getResources().getColor(R.color.red));
                rightText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelOrderDialog.show();
                    }
                });
                priceParentTop.setVisibility(View.GONE);
                noPriceParent.setVisibility(View.VISIBLE);
                break;

            case Const.OrderStatus.ORDER_STATUS_RESERVED:
                //待上门

            case Const.OrderStatus.ORDER_STATUS_PICKUP:
                //上门中
//                clothesDetailParent.setVisibility(View.GONE);
//                fillOrderSenderInfo(order);
//                oneBtnOperationParent.setVisibility(View.VISIBLE);
//                twoBtnOperationParent.setVisibility(View.INVISIBLE);
//                oneOperationText.setText(R.string.operation_order_cancel);
//                oneOperationText.setTag(OPERATION_ORDER_CANCEL);
//
//                oneOperationText.setBackgroundResource(R.drawable.brown_corner);
//                bottomOperationParent.setVisibility(View.GONE);
//                bottomDividerParent.setVisibility(View.GONE);
                rightText.setVisibility(View.VISIBLE);
                rightText.setText("取消订单");
                rightText.setTextColor(getResources().getColor(R.color.red));
                rightText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelOrderDialog.show();
                    }
                });

                senderInfoParent.setVisibility(View.VISIBLE);
                clothesDetailParentTop.setVisibility(View.GONE);
                senderInfoParent.setVisibility(View.GONE);
                bottomOperationParent.setVisibility(View.GONE);
                fillOrderSenderInfo(order);
                bottomDividerParent.setVisibility(View.GONE);

                priceParentTop.setVisibility(View.GONE);
                noPriceParent.setVisibility(View.VISIBLE);
                break;
            case Const.OrderStatus.ORDER_STATUS_WAIT_TO_PAY:

                fillOrderSenderInfo(order);
                rightText.setVisibility(View.VISIBLE);
                rightText.setText("取消订单");
                rightText.setTextColor(getResources().getColor(R.color.red));
                rightText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelOrderDialog.show();
                    }
                });
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    priceParentTop.setVisibility(View.VISIBLE);
                    fillOrderClotesDetailInfo(items);
                }else{
                    priceParentTop.setVisibility(View.GONE);
                }
                bottomDividerParent.setVisibility(View.VISIBLE);
                bottomOperationParent.setVisibility(View.VISIBLE);
                oneBtnOperationParent.setVisibility(View.VISIBLE);
                twoBtnOperationParent.setVisibility(View.INVISIBLE);
                oneOperationText.setText(R.string.operation_order_pay);
                oneOperationText.setTag(OPERATION_ORDER_PAY);
//                oneOperationText.setBackgroundResource(R.drawable.yellow_corner);
                oneOperationText.setBackgroundResource(R.mipmap.red_button_shadow);
                priceParentTop.setVisibility(View.VISIBLE);
                noPriceParent.setVisibility(View.GONE);
                if(arrowImage != null){
                    arrowImage.setVisibility(View.VISIBLE);
                }

//                leftOperationText.setBackgroundResource(R.drawable.brown_corner);
//                leftOperationText.setTag(OPERATION_ORDER_CANCEL);
//                rightOperationText.setText(R.string.operation_order_pay);
//                rightOperationText.setTag(OPERATION_ORDER_PAY);
//                rightOperationText.setBackgroundResource(R.drawable.yellow_corner);
//                bottomOperationParent.setVisibility(View.GONE);
//                bottomDividerParent.setVisibility(View.GONE);

                //待支付
                break;
            case Const.OrderStatus.ORDER_STATUS_PAID:
                //已支付

            case Const.OrderStatus.ORDER_STATUS_FETCH_COMPELTE:

                fillOrderSenderInfo(order);
                noPriceParent.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(order,items,true);
                }else{
                    clothesDetailParentTop.setVisibility(View.GONE);
                    clothesDetailParentCener.setVisibility(View.GONE);
                }
                bottomOperationParent.setVisibility(View.GONE);
                bottomDividerParent.setVisibility(View.GONE);
                shareBtn.setVisibility(View.INVISIBLE);
//                if (action.equals("share")){
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    ShareInfo shareInfo = order.getShareInfo();
////                    oneOperationText.setText(shareInfo.getShareBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                    shareBtn.setVisibility(View.VISIBLE);
//
//                }else if(action.equals("direct")){
//                    shareBtn.setVisibility(View.VISIBLE);
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    DirectInfo directInfo = order.getDirectInfo();
////                    oneOperationText.setText(directInfo.getBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                }else {
//                    shareBtn.setVisibility(View.INVISIBLE);
////                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                }
//                twoBtnOperationParent.setVisibility(View.INVISIBLE);


                break;

            case Const.OrderStatus.ORDER_STATUS_IN_STORE:


            case Const.OrderStatus.ORDER_STATUS_WASHING:
                //洗涤中

            case Const.OrderStatus.ORDER_STATUS_WASHING_FINISH:
                noPriceParent.setVisibility(View.GONE);
                fillOrderSenderInfo();
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(order,items,true);
                }else{
                    clothesDetailParentCener.setVisibility(View.GONE);
                    clothesDetailParentTop.setVisibility(View.GONE);
                }
                bottomOperationParent.setVisibility(View.GONE);
                bottomDividerParent.setVisibility(View.GONE);
//                if (action.equals("share")){
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    ShareInfo shareInfo = order.getShareInfo();
////                    oneOperationText.setText(shareInfo.getShareBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                    shareBtn.setVisibility(View.VISIBLE);
//
//                }else if(action.equals("direct")){
//                    shareBtn.setVisibility(View.VISIBLE);
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    DirectInfo directInfo = order.getDirectInfo();
////                    oneOperationText.setText(directInfo.getBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                }else {
//                    shareBtn.setVisibility(View.INVISIBLE);
////                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                }
                shareBtn.setVisibility(View.INVISIBLE);

                break;


            //洗涤完成

            case Const.OrderStatus.ORDER_STATUS_SENDING_BACK:
                noPriceParent.setVisibility(View.GONE);
                fillOrderSenderInfo(order);
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(order,items,true);
                }else{
                    clothesDetailParentCener.setVisibility(View.GONE);
                    clothesDetailParentTop.setVisibility(View.GONE);
                }
                bottomOperationParent.setVisibility(View.GONE);
                bottomDividerParent.setVisibility(View.GONE);
//                if (action.equals("share")){
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    ShareInfo shareInfo = order.getShareInfo();
////                    oneOperationText.setText(shareInfo.getShareBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                    shareBtn.setVisibility(View.VISIBLE);
//
//                }else if(action.equals("direct")){
//                    shareBtn.setVisibility(View.VISIBLE);
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    DirectInfo directInfo = order.getDirectInfo();
////                    oneOperationText.setText(directInfo.getBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                }else {
//                    shareBtn.setVisibility(View.INVISIBLE);
////                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                }
                shareBtn.setVisibility(View.INVISIBLE);

                break;

            case Const.OrderStatus.ORDER_STATUS_DELIVERED:
                noPriceParent.setVisibility(View.GONE);
                //已送达
                fillOrderSenderInfo(order);
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(order,items,true);
                }else{
                    clothesDetailParentCener.setVisibility(View.GONE);
                    clothesDetailParentTop.setVisibility(View.GONE);
                }

//                if (action.equals("share")){
//                    ShareInfo shareInfo = order.getShareInfo();
//                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                    twoBtnOperationParent.setVisibility(View.VISIBLE);
//                    leftOperationText.setText(shareInfo.getShareBtnText());
//                    leftOperationText.setBackgroundResource(R.drawable.blue_corner);
//                    leftOperationText.setTag(OPERATION_ORDER_SHARE);
//                    rightOperationText.setText(R.string.operation_order_confirm);
//                    rightOperationText.setBackgroundResource(R.drawable.green_shape);
//                    rightOperationText.setTag(OPERATION_ORDER_CONFIRM);
//                }else if(action.equals("direct")){
//                    DirectInfo directInfo = order.getDirectInfo();
//                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                    twoBtnOperationParent.setVisibility(View.VISIBLE);
//                    leftOperationText.setText(directInfo.getBtnText());
//                    leftOperationText.setBackgroundResource(R.drawable.blue_corner);
//                    leftOperationText.setTag(OPERATION_ORDER_SHARE);
//                    rightOperationText.setText(R.string.operation_order_confirm);
//                    rightOperationText.setBackgroundResource(R.drawable.green_shape);
//                    rightOperationText.setTag(OPERATION_ORDER_CONFIRM);
//                }else {
//                    oneBtnOperationParent.setVisibility(View.VISIBLE);
//                    twoBtnOperationParent.setVisibility(View.INVISIBLE);
//                    oneOperationText.setText(R.string.operation_order_confirm);
//                    oneOperationText.setBackgroundResource(R.drawable.green_shape);
//                    oneOperationText.setTag(OPERATION_ORDER_CONFIRM);
//                }


//                if (action.equals("share")){
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    ShareInfo shareInfo = order.getShareInfo();
////                    oneOperationText.setText(shareInfo.getShareBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                    shareBtn.setVisibility(View.VISIBLE);
//
//                }else if(action.equals("direct")){
//                    shareBtn.setVisibility(View.VISIBLE);
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    DirectInfo directInfo = order.getDirectInfo();
////                    oneOperationText.setText(directInfo.getBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                }else {
//                    shareBtn.setVisibility(View.INVISIBLE);
////                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                }
                shareBtn.setVisibility(View.INVISIBLE);

                oneBtnOperationParent.setVisibility(View.VISIBLE);
                oneOperationText.setText(R.string.operation_order_confirm);
//                oneOperationText.setBackgroundResource(R.drawable.green_shape);
                oneOperationText.setBackgroundResource(R.mipmap.green_button_shadow);
                oneOperationText.setTag(OPERATION_ORDER_CONFIRM);


                break;


            case Const.OrderStatus.ORDER_STATUS_COMPLETED:
                noPriceParent.setVisibility(View.GONE);
                fillOrderSenderInfo();
                if(!TextUtils.isEmpty(order.getTotalPrice()) && items != null && items.size() > 0){
                    fillOrderClotesDetailInfo(order,items,true);
                }else{
                    clothesDetailParentCener.setVisibility(View.GONE);
                    clothesDetailParentTop.setVisibility(View.GONE);
                }
//                if(order.isEvaluable()){
                if(order.isEvaluable()){
//                    if (action.equals("share")){
//                        twoBtnOperationParent.setVisibility(View.VISIBLE);
//                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                        ShareInfo shareInfo = order.getShareInfo();
//                        leftOperationText.setText(shareInfo.getShareBtnText());
//                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
//                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
//                        rightOperationText.setText(R.string.operation_order_remark);
//                        rightOperationText.setBackgroundResource(R.drawable.orange_corner);
//                        rightOperationText.setTag(OPERATION_ORDER_REMARK);
//                    }else if(action.equals("direct")){
//                        twoBtnOperationParent.setVisibility(View.VISIBLE);
//                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                        DirectInfo directInfo = order.getDirectInfo();
//                        leftOperationText.setText(directInfo.getBtnText());
//                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
//                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
//                        rightOperationText.setText(R.string.operation_order_remark);
//                        rightOperationText.setBackgroundResource(R.drawable.orange_corner);
//                        rightOperationText.setTag(OPERATION_ORDER_REMARK);
//                    }else {
//                        oneBtnOperationParent.setVisibility(View.VISIBLE);
//                        twoBtnOperationParent.setVisibility(View.GONE);
//                        oneOperationText.setText(R.string.operation_order_remark);
//                        oneOperationText.setBackgroundResource(R.drawable.orange_corner);
//                        oneOperationText.setTag(OPERATION_ORDER_REMARK);
//                    }

//                    if (action.equals("share")){
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    ShareInfo shareInfo = order.getShareInfo();
////                    oneOperationText.setText(shareInfo.getShareBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                        shareBtn.setVisibility(View.VISIBLE);
//
//                    }else if(action.equals("direct")){
//                        shareBtn.setVisibility(View.VISIBLE);
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    DirectInfo directInfo = order.getDirectInfo();
////                    oneOperationText.setText(directInfo.getBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                    }else {
//                        shareBtn.setVisibility(View.INVISIBLE);
////                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                    }
                    shareBtn.setVisibility(View.INVISIBLE);

                    oneBtnOperationParent.setVisibility(View.VISIBLE);
                    twoBtnOperationParent.setVisibility(View.GONE);
                    oneOperationText.setText(R.string.operation_order_remark);
//                        oneOperationText.setBackgroundResource(R.drawable.orange_corner);
                    oneOperationText.setBackgroundResource(R.mipmap.orange_button_shadow);
                    oneOperationText.setTag(OPERATION_ORDER_REMARK);

                }else {

//                    if (action.equals("share")){
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    ShareInfo shareInfo = order.getShareInfo();
////                    oneOperationText.setText(shareInfo.getShareBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                        shareBtn.setVisibility(View.VISIBLE);
//
//                    }else if(action.equals("direct")){
//                        shareBtn.setVisibility(View.VISIBLE);
////                    oneBtnOperationParent.setVisibility(View.VISIBLE);
////                    DirectInfo directInfo = order.getDirectInfo();
////                    oneOperationText.setText(directInfo.getBtnText());
////                    oneOperationText.setBackgroundResource(R.drawable.blue_corner);
////                    oneOperationText.setTag(OPERATION_ORDER_SHARE);
//                    }else {
//                        shareBtn.setVisibility(View.INVISIBLE);
////                    oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                    }
                    shareBtn.setVisibility(View.INVISIBLE);
                    oneBtnOperationParent.setVisibility(View.VISIBLE);
                    twoBtnOperationParent.setVisibility(View.GONE);
                    oneOperationText.setText(R.string.more_order);
                    oneOperationText.setTextColor(Color.parseColor("#444A59"));
                    oneOperationText.setBackgroundResource(R.drawable.gray_border);
                    oneOperationText.setTag(OPERATION_DO_ORDER);
                    //分享有礼和再来一单
//                    if (action.equals("share")){
//                        twoBtnOperationParent.setVisibility(View.VISIBLE);
//                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                        ShareInfo shareInfo = order.getShareInfo();
//                        leftOperationText.setText(shareInfo.getShareBtnText());
//                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
//                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
//                        rightOperationText.setText(R.string.more_order);
//                        rightOperationText.setBackgroundResource(R.drawable.gray_border);
//                        rightOperationText.setTag(OPERATION_DO_ORDER);
//                        rightOperationText.setTextColor(Color.parseColor("#444A59"));
//
//                    }else if(action.equals("direct")){
//                        twoBtnOperationParent.setVisibility(View.VISIBLE);
//                        oneBtnOperationParent.setVisibility(View.INVISIBLE);
//                        DirectInfo directInfo = order.getDirectInfo();
//                        leftOperationText.setText(directInfo.getBtnText());
//                        leftOperationText.setBackgroundResource(R.drawable.blue_corner);
//                        leftOperationText.setTag(OPERATION_ORDER_SHARE);
//                        rightOperationText.setText(R.string.more_order);
//                        rightOperationText.setBackgroundResource(R.drawable.gray_border);
//                        rightOperationText.setTag(OPERATION_DO_ORDER);
//                        rightOperationText.setTextColor(Color.parseColor("#444A59"));
//                    }else {
//                        oneBtnOperationParent.setVisibility(View.VISIBLE);
//                        twoBtnOperationParent.setVisibility(View.GONE);
//                        oneOperationText.setText(R.string.more_order);
//                        oneOperationText.setTextColor(Color.parseColor("#444A59"));
//                        oneOperationText.setBackgroundResource(R.drawable.gray_border);
//                        oneOperationText.setTag(OPERATION_DO_ORDER);
//                    }
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
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                statusText1.setSelected(false);
                statusText2.setSelected(false);
                statusText3.setSelected(false);
                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_BOOKING:
                dotImage.setImageResource(R.mipmap.clock1);
                bigDot1.setImageResource(R.mipmap.big_dot1);
                bigDot2.setImageResource(R.mipmap.big_dot1);
                bigDot3.setImageResource(R.mipmap.big_dot1);
                bigDot4.setImageResource(R.mipmap.big_dot1);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer1));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                progress = PROGRESS_MAX / 3  / 6;
//                statusText1.setSelected(true);
//                statusText2.setSelected(false);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
                //预约完成
                break;

            case Const.OrderStatus.ORDER_STATUS_RESERVED:
                //待上门
                bigDot1.setImageResource(R.mipmap.big_dot1);
                bigDot2.setImageResource(R.mipmap.big_dot1);
                bigDot3.setImageResource(R.mipmap.big_dot1);
                bigDot4.setImageResource(R.mipmap.big_dot1);
                progress = PROGRESS_MAX / 3  / 6 * 2;
                dotImage.setImageResource(R.mipmap.clock1);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer1));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
//                statusText1.setSelected(true);
//                statusText2.setSelected(false);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
                break;
            case Const.OrderStatus.ORDER_STATUS_PICKUP:
                progress = PROGRESS_MAX / 3  / 6 * 3;
                dotImage.setImageResource(R.mipmap.clock1);
                bigDot1.setImageResource(R.mipmap.big_dot1);
                bigDot2.setImageResource(R.mipmap.big_dot1);
                bigDot3.setImageResource(R.mipmap.big_dot1);
                bigDot4.setImageResource(R.mipmap.big_dot1);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer1));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
//                statusText1.setSelected(true);
//                statusText2.setSelected(false);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
                //上门中
                break;
            case Const.OrderStatus.ORDER_STATUS_WAIT_TO_PAY:
                progress = PROGRESS_MAX / 3  / 6 * 4;
                dotImage.setImageResource(R.mipmap.clock2);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer2));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                bigDot1.setImageResource(R.mipmap.big_dot2);
                bigDot2.setImageResource(R.mipmap.big_dot2);
                bigDot3.setImageResource(R.mipmap.big_dot2);
                bigDot4.setImageResource(R.mipmap.big_dot2);
//                statusText1.setSelected(true);
//                statusText2.setSelected(false);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);

                //待支付
                break;
            case Const.OrderStatus.ORDER_STATUS_PAID:
                progress = PROGRESS_MAX / 3  / 6 * 5;
                dotImage.setImageResource(R.mipmap.clock2);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer2));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));

                bigDot1.setImageResource(R.mipmap.big_dot2);
                bigDot2.setImageResource(R.mipmap.big_dot2);
                bigDot3.setImageResource(R.mipmap.big_dot2);
                bigDot4.setImageResource(R.mipmap.big_dot2);
//                statusText1.setSelected(true);
//                statusText2.setSelected(false);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
                //已支付

                break;
            case Const.OrderStatus.ORDER_STATUS_FETCH_COMPELTE:
                progress = PROGRESS_MAX / 3  ;

                dotImage.setImageResource(R.mipmap.clock2);
                bigDot1.setImageResource(R.mipmap.big_dot2);
                bigDot2.setImageResource(R.mipmap.big_dot2);
                bigDot3.setImageResource(R.mipmap.big_dot2);
                bigDot4.setImageResource(R.mipmap.big_dot2);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer2));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                //取件完成
//                statusText1.setSelected(true);
//                statusText2.setSelected(false);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_IN_STORE:

                progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 / 3;
                dotImage.setImageResource(R.mipmap.clock3);
                bigDot1.setImageResource(R.mipmap.big_dot3);
                bigDot2.setImageResource(R.mipmap.big_dot3);
                bigDot3.setImageResource(R.mipmap.big_dot3);
                bigDot4.setImageResource(R.mipmap.big_dot3);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer3));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                //入库检验
//                statusText1.setSelected(false);
//                statusText2.setSelected(true);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_WASHING:
                progress = PROGRESS_MAX / 3  + PROGRESS_MAX / 3 /3 * 2;
                dotImage.setImageResource(R.mipmap.clock3);
                bigDot1.setImageResource(R.mipmap.big_dot3);
                bigDot2.setImageResource(R.mipmap.big_dot3);
                bigDot3.setImageResource(R.mipmap.big_dot3);
                bigDot4.setImageResource(R.mipmap.big_dot3);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer3));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                //洗涤中
//                statusText1.setSelected(false);
//                statusText2.setSelected(true);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
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
                bigDot1.setImageResource(R.mipmap.big_dot3);
                bigDot2.setImageResource(R.mipmap.big_dot3);
                bigDot3.setImageResource(R.mipmap.big_dot3);
                bigDot4.setImageResource(R.mipmap.big_dot3);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer3));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                //洗涤完成
//                statusText1.setSelected(false);
//                statusText2.setSelected(true);
//                statusText3.setSelected(false);
//                statusText4.setSelected(false);
                break;

            case Const.OrderStatus.ORDER_STATUS_SENDING_BACK:
                progress = 2 * PROGRESS_MAX / 3 + PROGRESS_MAX / 3 / 3;
                dotImage.setImageResource(R.mipmap.clock4);
                bigDot1.setImageResource(R.mipmap.big_dot4);
                bigDot2.setImageResource(R.mipmap.big_dot4);
                bigDot3.setImageResource(R.mipmap.big_dot4);
                bigDot4.setImageResource(R.mipmap.big_dot4);
                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer4));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                // 送返中
//                statusText1.setSelected(false);
//                statusText2.setSelected(false);
//                statusText3.setSelected(true);
//                statusText4.setSelected(false);

                break;

            case Const.OrderStatus.ORDER_STATUS_DELIVERED:
                progress = 2 * PROGRESS_MAX / 3 + PROGRESS_MAX / 3 / 3 * 2;
                dotImage.setImageResource(R.mipmap.clock4);
                bigDot1.setImageResource(R.mipmap.big_dot4);
                bigDot2.setImageResource(R.mipmap.big_dot4);
                bigDot3.setImageResource(R.mipmap.big_dot4);
                bigDot4.setImageResource(R.mipmap.big_dot4);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer4));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
                //已送达
//                statusText1.setSelected(false);
//                statusText2.setSelected(false);
//                statusText3.setSelected(true);
//                statusText4.setSelected(false);

                break;


            case Const.OrderStatus.ORDER_STATUS_COMPLETED:
                progress = PROGRESS_MAX;
                dotImage.setImageResource(R.mipmap.clock5);

                bigDot1.setImageResource(R.mipmap.big_dot5);
                bigDot2.setImageResource(R.mipmap.big_dot5);
                bigDot3.setImageResource(R.mipmap.big_dot5);
                bigDot4.setImageResource(R.mipmap.big_dot5);

                orderProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.order_progress_layer5));
                orderProgressBar.setThumb(getResources().getDrawable(R.drawable.transparent_dot));
//                statusText1.setSelected(false);
//                statusText2.setSelected(false);
//                statusText3.setSelected(false);
//                statusText4.setSelected(true);
                //已完成

                break;
            case Const.OrderStatus.ORDER_STATUS_PAID_EXCEPTION:
                //支付异常
                break;
        }
//        timer2.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        }, 0 * 1000, 200);

        Message msg = handler.obtainMessage();
        msg.what = Const.Message.MSG_PROGRESS_INCREASED;
//        msg.arg1 = progress;
        msg.sendToTarget();
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
    @Bind(R.id.big_dot1)
    ImageView bigDot1;
    @Bind(R.id.big_dot2)
    ImageView bigDot2;
    @Bind(R.id.big_dot3)
    ImageView bigDot3;
    @Bind(R.id.big_dot4)
    ImageView bigDot4;

    boolean isStart1 = true;
    boolean isStart2 = true;
    boolean isStart3 = true;
    boolean isStart4 = true;


    @Override
    protected void handlerMessage(Message msg) {

        switch (msg.what) {
            case Const.Message.MSG_ORDER_DETAIL_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
//                    Log.e("---------","order_detail:" + msg.obj);
                    isStart1 = true;
                    isStart2 = true;
                    isStart3 = true;
                    isStart4 = true;
                    OrderDetailResp detailResp = (OrderDetailResp) JsonParser.jsonToObject(msg.obj + "", OrderDetailResp.class);
                    order = detailResp.getData();
                    subPrice = Float.parseFloat(order.getTotalPrice());
                    clockOn = order.isClockOn();
//                    logVolley.addParams("orderNo",order.getOrderNo());
//                    if(order.getShareInfo() != null && !TextUtils.isEmpty(order.getShareInfo().getAppSharePromocode())){
//                        logVolley.addParams("promoCode",order.getShareInfo().getAppSharePromocode());
//                    }

                    initTopView(order);
//
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
                int currentProgress = orderProgressBar.getProgress() + 1;
                if(currentProgress < progress){
                    orderProgressBar.setProgress(currentProgress);
                    handler.sendEmptyMessageDelayed(Const.Message.MSG_PROGRESS_INCREASED,20);
                }else {
                    orderProgressBar.setProgress(progress);
                    handler.removeMessages(Const.Message.MSG_PROGRESS_INCREASED);
//                    timer2.cancel();

                }


                if(currentProgress>= 10 && currentProgress < 10 + 80 / 3){

                    if(isStart1){
                        setDotAnim(bigDot1);
                        isStart1 = false;
                    }

                    bigDot2.setVisibility(View.INVISIBLE);
                    bigDot3.setVisibility(View.INVISIBLE);
                    bigDot4.setVisibility(View.INVISIBLE);
                    statusText1.setSelected(true);
                    statusText2.setSelected(false);
                    statusText3.setSelected(false);
                    statusText4.setSelected(false);
                }else if(currentProgress >= 10 + 80 / 3 && currentProgress < 10 + 80 / 3 * 2){
//                    bigDot1.setVisibility(View.VISIBLE);
//                    bigDot2.setVisibility(View.VISIBLE);

                    if(isStart2){
                        setDotAnim(bigDot2);
                        isStart2 = false;
                    }
                    bigDot3.setVisibility(View.INVISIBLE);
                    bigDot4.setVisibility(View.INVISIBLE);
                    statusText1.setSelected(true);
                    statusText2.setSelected(true);
                    statusText3.setSelected(false);
                    statusText4.setSelected(false);
                }else if(currentProgress >= 10 + 80 / 3 * 2 && currentProgress <= 90){
//                    bigDot1.setVisibility(View.VISIBLE);
//                    bigDot2.setVisibility(View.VISIBLE);
//                    bigDot3.setVisibility(View.VISIBLE);

                    if(isStart3){
                        setDotAnim(bigDot3);
                        isStart3 = false;
                    }
                    bigDot4.setVisibility(View.INVISIBLE);
                    statusText1.setSelected(true);
                    statusText2.setSelected(true);
                    statusText3.setSelected(true);
                    statusText4.setSelected(false);
                }else if(currentProgress >= 90){
                    statusText1.setSelected(true);
                    statusText2.setSelected(true);
                    statusText3.setSelected(true);
                    statusText4.setSelected(true);

                    if(isStart4){
                        setDotAnim(bigDot4);
                        isStart4 = false;
                    }
                    bigDot1.setVisibility(View.VISIBLE);
                    bigDot2.setVisibility(View.VISIBLE);
                    bigDot3.setVisibility(View.VISIBLE);
                    bigDot4.setVisibility(View.VISIBLE);
                }
            }
            break;

            case Const.Message.MSG_CONFIRM_DONE_SUCC:
                if(msg.arg1 == Const.Request.REQUEST_SUCC){
                    OperationToast.showOperationResult(this,"确认成功",0);
                    showCenterLoading();
                    sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
                    sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_TABS));
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

            case Const.Message.MSG_GET_PAY_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    GetPriceResp priceResp = (GetPriceResp) JsonParser.jsonToObject(msg.obj + "", GetPriceResp.class);
                    balance = priceResp.getData().getBalance();
                    initPrice(priceResp.getData().getDftCoupon());
                    if (priceResp.getData().getDftCoupon() != null) {
                        if (priceResp.getData().getCoupons() != null && priceResp.getData().getCoupons().size() > 0) {
                            coupons.getCoupons().clear();
                            for (Coupon c : priceResp.getData().getCoupons()) {
                                if (c.getUsable() != 1 && (c.getStatus() == 1 || c.getStatus() == 2)) {
                                    c.setStatus(5);
                                }
                            }
                            coupons.getCoupons().addAll(priceResp.getData().getCoupons());

                        }

//            coupons .getCoupons().addAll(data.getUnusableCoupons());
                    }

                    if(priceResp.getData().getUnusableCoupons()  != null && priceResp.getData().getUnusableCoupons().size() >0){
                        ununsedCoupons.getCoupons().addAll(priceResp.getData().getUnusableCoupons());
                    }
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    OperationToast.showOperationResult(this, msg.obj + "", 0);
                }

                break;

            case Const.Message.MSG_GET_PAY_FAIL:
                OperationToast.showOperationResult(this, R.string.request_fail);
                hideLoading();
                break;

            case Const.Message.MSG_PAY_CONFIRM_SUCC:

                break;

        }

    }

    TextView couponsDesText;
    TextView couponsDiscountText;
    TextView noCouponsText;
    LinearLayout hasCouponsParent;
    TextView copounsTips;
    TextView textPayAmmout;
    RelativeLayout selCouponParent;
    float couponDiscount;
    PayData couponData;
    public final static int WAY_COUPON = 12;

    float subPrice = 0f;
    String balance;



    private void setDotAnim(ImageView dot){
        Animation  ani = AnimationUtils.loadAnimation(this,R.anim.dot_anim);
        dot.setVisibility(View.VISIBLE);
        dot.startAnimation(ani);


    }

    private void initPrice(Coupon coupon) {

        if(coupon == null){
            noCouponsText.setVisibility(View.VISIBLE);
            hasCouponsParent.setVisibility(View.INVISIBLE);
            copounsTips.setText("优惠："  +"￥0.00");
            textPayAmmout.setText("￥" + NumberUtil.format2Dicimal(order.getTotalPrice()));
            subPrice = Float.parseFloat(order.getTotalPrice());
            return;
        }
        couponDiscount = (float) (Math.round(Float.parseFloat(coupon.getAmount()) * 100)) / 100;
        couponData = new PayData(WAY_COUPON, couponDiscount + "", coupon.getCouponNo());
        if(!TextUtils.isEmpty(coupon.getDescription())){
//                couponsDesText.setText(data.getDftCoupon().getDescription());
//                couponsDes
// Text.setVisibility(View.VISIBLE);


        }else {
            couponsDesText.setVisibility(View.INVISIBLE);
        }

        couponsDiscountText.setText("-" + "￥" + NumberUtil.format2Dicimal(coupon.getAmount()));
        copounsTips.setText("优惠："  + NumberUtil.format2Dicimal(coupon.getAmount()));
        couponsDiscountText.setTag(coupon.getCouponNo());
        if(!TextUtils.isEmpty(order.getTotalPrice())){
            BigDecimal price = new BigDecimal(Float.parseFloat(order.getTotalPrice()));
            subPrice = price.subtract(new BigDecimal(coupon.getAmount())).floatValue();
            textPayAmmout.setText("￥" + NumberUtil.format2Dicimal(subPrice+""));
        }

        noCouponsText.setVisibility(View.INVISIBLE);
        hasCouponsParent.setVisibility(View.VISIBLE);


//        if (data.getDftCoupon() != null) {
//
//            if (data.getCoupons() != null && data.getCoupons().size() > 0) {
////                coupons.getCoupons().clear();
//                for (Coupon c : data.getCoupons()) {
//                    if (c.getUsable() != 1 && (c.getStatus() == 1 || c.getStatus() == 2)) {
//                        c.setStatus(5);
//                    }
//                }
//                coupons.getCoupons().addAll(data.getCoupons());
//
//            }

//            coupons .getCoupons().addAll(data.getUnusableCoupons());
//        }
//
//        if(data.getUnusableCoupons()  != null && data.getUnusableCoupons().size() >0){
//            ununsedCoupons.getCoupons().addAll(data.getUnusableCoupons());
//        }
//        myBlance = (float) (Math.round(Float.parseFloat(data.getBalance() + "") * 100)) / 100;
//        refreshFinalPay(couponDiscount);

    }
    @Bind(R.id.button_share)
    ImageButton shareBtn;
//    @Bind(R.id.image_contact_me)
//    ImageView contactUsImage;
    boolean isShowContanct = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this, this);
        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                orderShare();
            }
        });

        super.onCreate(savedInstanceState);
        coupons.setCoupons(new ArrayList<Coupon>());
        ununsedCoupons.setCoupons(new ArrayList<Coupon>());
        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        FontUtil.applyFont(this, layoutOrderDetail, "OpenSans-Regular.ttf");
        getIntent().getBooleanExtra("is_show_contact",true);

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
                    Intent intent = new Intent(this, PayActivity2.class);
                    intent.putExtra("order_no", order.getOrderNo());
                    intent.putExtra("order_price", subPrice);
                    if(couponData != null){
                        intent.putExtra("coupon_data",JsonParser.objectToJsonStr(couponData));
                    }
//                    if(TextUtils.isEmpty(balance)){
//                        balance = "";
//                    }
//                    intent.putExtra("balance",balance);
                    startActivityForResult(intent, OPERATION_ORDER_PAY);
                    break;
                case OPERATION_ORDER_REMARK:
                    Intent remarkIntent = new Intent(this, EvaluateActivity.class);
                    remarkIntent.putExtra("order_no", order.getOrderNo());
                    remarkIntent.putExtra("sender_name", order.getDelivererName());
                    remarkIntent.putExtra("site_name",order.getStoreName());
                    startActivityForResult(remarkIntent, OPERATION_ORDER_REMARK);
                    break;
                case OPERATION_ORDER_SHARE:
                    orderShare();

                    break;

                case OPERATION_DO_ORDER:
                    startActivity(new Intent(this, DoOrderActivity.class));
                    break;

                case OPERATION_ORDER_CONFIRM:
//                    OperationToast.showOperationResult(this,"confirm",0);
                    setOperationMsg(getString(R.string.operating));
                    locationForService = UserInfosPref.getInstance(this).getLocationServer();
                    confirmDoneVolley.requestPost(Const.Request.confirmDone+"/"+orderNo+"/receive",this,getHandler(),UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                    break;
            }
        }else if(view == selCouponParent){
            Intent intent = new Intent(this, SelCouponActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            for(Coupon c : coupons.getCoupons()){
                c.setSelected(false);
                if(couponsDiscountText.getTag() != null){
                    if(c.getCouponNo().equals(couponsDiscountText.getTag()+"")){
                        c.setSelected(true);
                    }
                }
            }

            intent.putExtra("usableCoupons", JsonParser.objectToJsonStr(coupons));
            intent.putExtra("unusableCoupons", JsonParser.objectToJsonStr(ununsedCoupons));



            startActivityForResult(intent, REQUEST_SELECT_COUPON);

        }
    }

    private static final int REQUEST_SELECT_COUPON = 1;

    Coupons coupons = new Coupons();
    Coupons ununsedCoupons = new Coupons();

    private void orderShare(){
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

    }

    ShareGiftDialog shareGiftDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPERATION_ORDER_PAY && resultCode == RESULT_OK) {
            showGift = true;
            setOperationMsg(getString(R.string.refreshing));
            locationForService = UserInfosPref.getInstance(this).getLocationServer();
            orderDetailVolley.requestGet(Const.Request.detail + "/" + orderNo, this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
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
        }else if(requestCode == REQUEST_SELECT_COUPON && resultCode == RESULT_OK){
            String desc = data.getStringExtra("desc");
            String ammout = data.getStringExtra("ammout");
            String id = data.getStringExtra("id");
            String couponStr = data.getStringExtra("coupon");
            Coupon coupon = (Coupon) JsonParser.jsonToObject(couponStr,Coupon.class);

            if (!TextUtils.isEmpty(ammout)) {
                couponsDiscountText.setText("-" + NumberUtil.format2Dicimal(ammout));
            }
            couponsDiscountText.setTag(id);
            couponDiscount = (float) (Math.round((Float.parseFloat(ammout)) * 100)) / 100;
            couponData = new PayData(12, couponDiscount + "", id);
            initPrice(coupon);
        }
    }

    @Override
    public void cancelOk() {
        OperationToast.showOperationResult(this, R.string.cancel_succ);
        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
        sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_TABS));
        sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
        finish();
    }

    long timeInterval = 0;
    public void initTopView(Order order){
        if(tvTime != null && order != null ) {
            if(!TextUtils.isEmpty(order.getOrderTime())){
                tvTime.setText(TextTool.changeNumberBig(TextTool.addLetterSpace(HomeViewModel.getOrderTimeForHome2(order))));

//                tvTime.setText(HomeViewModel.getOrderTimeForDetail3(order.getOrderTime(),timeInterval));
                if(clockOn){
                    timeInterval += 1;
                }
            }else{
//                tvTime.setText(HomeViewModel.getOrderTimeForDetail2(order));
                tvTime.setText(TextTool.changeNumberBig(TextTool.addLetterSpace(HomeViewModel.getOrderTimeForHome2(order))));

            }
//            tvTime.setText(HomeViewModel.getOrderTimeForDetail2(order));

        }

    }
    private void startRefreshTimer(){
//        if(isFinishing()){
//            return;
//        }
//        if(refreshTimer != null){
//            stopRefreshTimer();
//        }
//        refreshTimer = new Timer();
//        refreshTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                refreshDetail();
//            }
//        }, 1000 * 60);
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
