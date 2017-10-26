package com.cnsunway.saas.wash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.BanlancePayDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.MyPayRequest;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.PayData;
import com.cnsunway.saas.wash.model.PayResult;
import com.cnsunway.saas.wash.resp.AlipayResp;
import com.cnsunway.saas.wash.resp.GetPayBanlanceResp;
import com.cnsunway.saas.wash.resp.WepayResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.AlipayTool;
import com.cnsunway.saas.wash.util.NumberUtil;
import com.cnsunway.saas.wash.util.WepayTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/16 0016.
 */

public class PayActivity2 extends InitActivity implements View.OnClickListener,BanlancePayDialog.OnBanlanceOkClickedLinstener{

    @Bind(R.id.text_title)
    TextView titleText;
    float subPrice;
    @Bind(R.id.text_subprice)
    TextView subPriceText;
//    String banlance;
    @Bind(R.id.text_account)
    TextView accountText;
    float myBlance;
    @Bind(R.id.text_balance_discount)
    TextView banlanceDiscountText;
    @Bind(R.id.pay)
    LinearLayout payParent;
    String orderNo;
    @Bind(R.id.pay_choice_parent)
    LinearLayout payChoiceParent;
    JsonVolley cancelPayVolley;
    JsonVolley payBanlanceVolley;
    @Override
    public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        titleText.setText("支付订单");
        subPrice = getIntent().getFloatExtra("order_price",0f);
//        balance = getIntent().getStringExtra("balance");
        orderNo = getIntent().getStringExtra("order_no");
        order = (Order)getIntent().getSerializableExtra("order");
        subPriceText.setText("￥" + NumberUtil.format2Dicimal(subPrice +""));
        myPayRequest = new MyPayRequest();
        myPayRequest.setOrderNo(orderNo);
        settleAccountsVolley = new JsonVolley(this, Const.Message.MSG_SETTLE_ACCOUNTS_SUCC, Const.Message.MSG_SETTLE_ACCOUNTS_FAIL);
        settleAccountsVolley.addParams("orderNo", orderNo);
        notifyPaySuccessVolley = new JsonVolley(this,Const.Message.MSG_RECHARGE_NOTIFY_SUCC,Const.Message.MSG_RECHARGE_NOTIFY_FAIL);
        notifyPaySuccessVolley.addParams("outTradeNo",orderNo);
        getPayInfoVolley = new StringVolley(this, Const.Message.MSG_GET_PAY_INFO_SUCC, Const.Message.MSG_GET_PAY_INFO_FAIL);
        getPayInfoVolley.addParams("orderNo", orderNo);
        cancelPayVolley = new JsonVolley(this,Const.Message.MSG_PAY_CANCEL_SUCC,Const.Message.MSG_PAY_CANCEL_FAIL);
        cancelPayVolley.addParams("orderNo",orderNo);
        payBanlanceVolley = new JsonVolley(this,Const.Message.MSG_GET_PAY_BANLANCE_SUCC,Const.Message.MSG_GET_PAY_BANLANCE_FAIL);
        String couponStr = getIntent().getStringExtra("coupon_data");
        if(!TextUtils.isEmpty(couponStr)){
            couponData = (PayData) JsonParser.jsonToObject(couponStr,PayData.class);
        }
        alipayTool = new AlipayTool(this, handler);
        wepayTool = new WepayTool(this);
        alipayBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    payChoice = SELECT_ALI_PAY;
                    wepayBox.setChecked(false);
                }
            }
        });

        wepayBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    payChoice = SELECT_WE_PAY;
                    alipayBox.setChecked(false);
                }
            }
        });
        payBanlanceVolley.requestGet(Const.Request.payBanlance + "/" + orderNo, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
//        refreshFinalPay();
        payParent.setOnClickListener(this);
        payVolley = new JsonVolley(this,Const.Message.MSG_GET_PAY_SUCC,Const.Message.MSG_GET_PAY_FAIL);
    }
    float finalPrice = 0f;
    float balanceDiscount;
    @Bind(R.id.text_final_pay)
    TextView finalPriceText;
    @Bind(R.id.text_pay_tips)
    TextView payTips;
    @Bind(R.id.pay_choice_bg)
    LinearLayout payChoiceUnableBg;
    @Bind(R.id.ali_pay_box)
    CheckBox alipayBox;
    @Bind(R.id.we_pay_box)
    CheckBox wepayBox;
    JsonVolley notifyPaySuccessVolley;

    private void paySucc() {
        OperationToast.showOperationResult(getApplicationContext(), R.string.pay_succ,R.mipmap.success_icon);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        notifyPaySuccessVolley.requestPost(Const.Request.notifyPaySuccess, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );

    }
    private void payFail(){
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        cancelPayVolley.requestGet(Const.Request.payCancel,getHandler(),UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    private void refreshFinalPay() {

        if(Float.compare(subPrice,0.0f) <= 0){
            finalPrice = 0;
        }else {
            if (Float.compare(subPrice, myBlance) > 0) {
                balanceDiscount = myBlance;
                finalPrice = (float) (Math.round((subPrice - balanceDiscount) * 100)) / 100;
                payTips.setText("还需支付");
                finalPriceText.setText("￥" + finalPrice);
            } else {
                alipayBox.setEnabled(false);
                wepayBox.setEnabled(false);
                payChoiceParent.setEnabled(false);
                payChoiceParent.setBackgroundColor(Color.parseColor("#aaffffff"));
                balanceDiscount = (float) (Math.round((subPrice) * 100)) / 100;
                payTips.setText("余额支付");
                finalPriceText.setText("￥" + NumberUtil.format2Dicimal(balanceDiscount + ""));
                finalPrice = 0;
                payChoiceUnableBg.setVisibility(View.VISIBLE);
            }
        }

        banlanceDiscountText.setText("-" + "￥" + NumberUtil.format2Dicimal(balanceDiscount + ""));

        if (Float.compare(finalPrice, 0) == 0) {
            payChoice = SELECT_BANLANCE_PAY;
        } else {
            payChoice = 0;
        }
    }

    int payChoice = 0;
    private static final int SELECT_WE_PAY = 32;
    private static final int SELECT_ALI_PAY = 33;
    private static final int SELECT_BANLANCE_PAY = 3;
    public final static int WAY_BALANCE = 31;//
    public final static int WAY_COUPON = 12;//
    public final static int WAY_THIRD_PARTY_PAY = 30;

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    Order order;
    StringVolley getPayInfoVolley;
    AlipayTool alipayTool;
    WepayTool wepayTool;
    private static final String ALIPAY_SUCC_RESULT = "9000";

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_RECHARGE_NOTIFY_SUCC:
                Intent intent = new Intent();
                intent.putExtra("order_no",orderNo);
                sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
                setResult(RESULT_OK, intent);
                finish();
                break;
            case Const.Message.MSG_RECHARGE_NOTIFY_FAIL:
                Intent intent2 = new Intent();
                intent2.putExtra("order_no", orderNo);

                sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
                setResult(RESULT_OK, intent2);
                finish();
                break;

            case Const.Message.MSG_SETTLE_ACCOUNTS_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    if (payChoice == SELECT_BANLANCE_PAY) {
                        paySucc();
                    } else {
//                        payChoiceDialog.setAmmout(finalPrice +"");
//                        payChoiceDialog.show();
                        if(payChoice == SELECT_ALI_PAY){
                            getPayInfoVolley.addParams("payChannel", SELECT_ALI_PAY + "");
                            setOperationMsg(getString(R.string.paying));
                            LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
                            getPayInfoVolley.requestPost(Const.Request.getPrePayInfo, handler, this, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                        }else if(payChoice == SELECT_WE_PAY){
                            getPayInfoVolley.addParams("payChannel", SELECT_WE_PAY + "");
                            setOperationMsg(getString(R.string.paying));
                            LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
                            getPayInfoVolley.requestPost(Const.Request.getPrePayInfo, handler, this, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                        }
                    }

                } else {
                    payFail();
                    OperationToast.showOperationResult(this, R.string.pay_fail,R.mipmap.wrong_icon);
                }

                break;

            case Const.Message.MSG_SETTLE_ACCOUNTS_FAIL:
                OperationToast.showOperationResult(getApplicationContext(), R.string.pay_fail);

                break;

            case Const.Message.MSG_GET_PAY_INFO_SUCC:

                if (msg.arg1 == Const.Request.REQUEST_SUCC) {

                    if (payChoice == SELECT_ALI_PAY) {
                        AlipayResp alipayResp = (AlipayResp) JsonParser.jsonToObject(msg.obj + "", AlipayResp.class);
                        alipayTool.pay(alipayResp.getData());
                    } else if (payChoice == SELECT_WE_PAY) {
                        WepayResp wepayResp = (WepayResp) JsonParser.jsonToObject(msg.obj + "", WepayResp.class);
                        wepayTool.pay(wepayResp.getData());
                    }

                } else {
                    OperationToast.showOperationResult(this, R.string.pay_fail,R.mipmap.wrong_icon);

                }
                break;

            case Const.Message.MSG_ALIPAY_RESULT:

                PayResult payResult = new PayResult((String) msg.obj);
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, ALIPAY_SUCC_RESULT)) {
                    paySucc();
                } else {
                    if (TextUtils.equals(resultStatus, "8000")) {

                    } else {
                        OperationToast.showOperationResult(this, R.string.pay_fail,R.mipmap.wrong_icon);
                        payFail();
                    }
                }


                break;

            case Const.Message.MSG_GET_PAY_BANLANCE_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    GetPayBanlanceResp response = (GetPayBanlanceResp) JsonParser.jsonToObject(msg.obj + "",GetPayBanlanceResp.class);
                    String balance = response.getData().getBalance();
                            accountText.setText("目前账户余额："+ NumberUtil.format2Dicimal(balance) + "元");
                    myBlance = new BigDecimal(balance).floatValue();
                    refreshFinalPay();

                }
                break;

            case Const.Message.MSG_GET_PAY_BANLANCE_FAIL:
                break;

            case Const.Message.MSG_GET_PAY_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    if(payChoice == WAY_BALANCE){
                        paySucc();
                    }else if (payChoice == SELECT_ALI_PAY) {
                        Toast.makeText(this,"alipay",Toast.LENGTH_SHORT).show();
                        AlipayResp alipayResp = (AlipayResp) JsonParser.jsonToObject(msg.obj + "", AlipayResp.class);
                        alipayTool.pay(alipayResp.getData());
                    } else if (payChoice == SELECT_WE_PAY) {
                        Toast.makeText(this,"wepay",Toast.LENGTH_SHORT).show();
                        WepayResp wepayResp = (WepayResp) JsonParser.jsonToObject(msg.obj + "", WepayResp.class);
                        wepayTool.pay(wepayResp.getData());
                    }
                }
                break;

            case Const.Message.MSG_GET_PAY_FAIL:
                break;

        }
    }

    public void back(View view){
        finish();
    }

    MyPayRequest myPayRequest;
    PayData banlanceData;
    PayData couponData;
    JsonVolley settleAccountsVolley;
    BanlancePayDialog banlancePayDialog;
    PayData thirdPartData;
    JsonVolley payVolley;

    @Override
    public void onClick(View view) {
        if(view == payParent){
            if (payChoice == SELECT_BANLANCE_PAY) {
//                JSONArray payListArray = null;
//                myPayRequest.getPayList().remove(new PayData(WAY_BALANCE));
//                myPayRequest.getPayList().remove(new PayData(WAY_COUPON));
//                if (Float.compare(balanceDiscount, 0) != 0) {
//                    banlanceData = new PayData(WAY_BALANCE, balanceDiscount + "");
//                    myPayRequest.getPayList().add(banlanceData);
//                }
//                if (couponData != null) {
//                    myPayRequest.getPayList().add(couponData);
//                }
//                try {
//                    JSONObject object = new JSONObject(JsonParser.objectToJsonStr(myPayRequest));
//                    payListArray = object.getJSONArray("payList");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (payListArray != null) {
//                    settleAccountsVolley.addParams("payList", payListArray);
//                }
                banlancePayDialog = new BanlancePayDialog(this, balanceDiscount + "", balanceDiscount + "").builder();
                banlancePayDialog.setBanlanceOkLinstener(this);
                banlancePayDialog.show();
            }else {
                if(!wepayBox.isChecked() && !alipayBox.isChecked()){
                    OperationToast.showOperationResult(this,"请选择支付方式",R.mipmap.reminder_icon);
                    return;
                }
                if(wepayBox.isChecked()){
                    payChoice = SELECT_WE_PAY;
                }
                if(alipayBox.isChecked()){
                    payChoice = SELECT_ALI_PAY;

                }
//                    thirdPartData = new PayData(WAY_THIRD_PARTY_PAY, finalPrice + "");
//                    JSONArray payListArray = null;
//                    myPayRequest.getPayList().remove(new PayData(WAY_BALANCE));
//                    myPayRequest.getPayList().remove(new PayData(WAY_COUPON));
//                    myPayRequest.getPayList().remove(new PayData(WAY_THIRD_PARTY_PAY));
//                    if (Float.compare(balanceDiscount, 0) != 0) {
//                        banlanceData = new PayData(WAY_BALANCE, balanceDiscount + "");
//                        myPayRequest.getPayList().add(banlanceData);
//                    }
//                    if (couponData != null) {
//                        myPayRequest.getPayList().add(couponData);
//                    }
//                    myPayRequest.getPayList().add(thirdPartData);
//                    try {
//                        JSONObject object = new JSONObject(JsonParser.objectToJsonStr(myPayRequest));
//                        payListArray = object.getJSONArray("payList");
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (payListArray != null) {
//                        settleAccountsVolley.addParams("payList", payListArray);
//                    }

                LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
                payVolley.addParams("amount",subPrice);
                payVolley.addParams("balance",myBlance);
                payVolley.addParams("thirdPartyPayChannel",payChoice);
                    payVolley.requestPost(Const.Request.pay + "/" + orderNo,this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                    );
                }
            }
    }

    @Override
    public void banlanceOkClicked() {
        setOperationMsg(getString(R.string.paying));
        payChoice = WAY_BALANCE;
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
//        payVolley.addParams("amount",subPrice);
        payVolley.addParams("balance",subPrice);
//        payVolley.addParams("thirdPartyPayChannel",WAY_BALANCE);
        payVolley.requestPost(Const.Request.pay+"/" + orderNo, this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());


    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(wepayResultReceiver, new IntentFilter(Const.MyFilter.FILTER_WE_PAY_RESULT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wepayResultReceiver);
    }

    private static final int WEPAY_SUCC_RESULT = 1;

    BroadcastReceiver wepayResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int reuslt = intent.getIntExtra("pay_result", 0);
            if (reuslt == WEPAY_SUCC_RESULT) {
                paySucc();
            } else {
                OperationToast.showOperationResult(context, R.string.pay_fail,R.mipmap.wrong_icon);
                payFail();

            }
        }
    };
}
