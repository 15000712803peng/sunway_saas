package com.cnsunway.saas.wash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.BanlancePayDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.dialog.PayChoiceDialog;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Coupon;
import com.cnsunway.saas.wash.model.Coupons;
import com.cnsunway.saas.wash.model.GetPayData;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.MyPayRequest;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.PayData;
import com.cnsunway.saas.wash.model.PayResult;
import com.cnsunway.saas.wash.resp.AlipayResp;
import com.cnsunway.saas.wash.resp.GetPriceResp;
import com.cnsunway.saas.wash.resp.WepayResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.AlipayTool;
import com.cnsunway.saas.wash.util.FontUtil;
import com.cnsunway.saas.wash.util.NumberUtil;
import com.cnsunway.saas.wash.util.WepayTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by LL on 2015/12/11.
 */
public class GetPayOrderActivity extends LoadingActivity implements View.OnClickListener, BanlancePayDialog.OnBanlanceOkClickedLinstener, PayChoiceDialog.SelectAlipayListener, PayChoiceDialog.SelectWepayListener {
    TextView titleText;
    public final static int WAY_BALANCE = 11;//
    public final static int WAY_COUPON = 12;//
    public final static int WAY_THIRD_PARTY_PAY = 30;

    JsonVolley getPriceVolley;
    String orderNo;
    Order order;
    float couponDiscount;
    PayData couponData;
    PayData thirdPartData;
    TextView couponsDiscountText, couponsDesText, noCouponsText, banlanceDiscountText;
    LinearLayout hasCouponsParent;
    TextView leftPriceText;
    private static final int SELECT_WE_PAY = 32;
    private static final int SELECT_ALI_PAY = 33;
    private static final int SELECT_BANLANCE_PAY = 3;
    RelativeLayout payOrderParent;
    TextView totalPriceText;
    RelativeLayout doPayParent;
    JsonVolley settleAccountsVolley;
    BanlancePayDialog banlancePayDialog;
    PayChoiceDialog payChoiceDialog;
    StringVolley getPayInfoVolley;
    AlipayTool alipayTool;
    WepayTool wepayTool;
    private static final String ALIPAY_SUCC_RESULT = "9000";
    private static final int WEPAY_SUCC_RESULT = 1;
    Coupons coupons = new Coupons();
    Coupons ununsedCoupons = new Coupons();
    RelativeLayout selCouponParent;
    private static final int REQUEST_SELECT_COUPON = 1;
    RelativeLayout layoutGetPay;
    StringVolley notifyPaySuccessVolley;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_get_pay_order);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutGetPay, "OpenSans-Regular.ttf");
    }

    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra("order_no");
        order = (Order)getIntent().getSerializableExtra("order");
        totalPrice = (float) (Math.round((Float.parseFloat(getIntent().getStringExtra("order_price"))) * 100)) / 100;
        getPriceVolley = new JsonVolley(this, Const.Message.MSG_GET_PAY_SUCC, Const.Message.MSG_GET_PAY_FAIL);
        getPriceVolley.addParams("orderNo", orderNo);
        settleAccountsVolley = new JsonVolley(this, Const.Message.MSG_SETTLE_ACCOUNTS_SUCC, Const.Message.MSG_SETTLE_ACCOUNTS_FAIL);
        settleAccountsVolley.addParams("orderNo", orderNo);
        getPayInfoVolley = new StringVolley(this, Const.Message.MSG_GET_PAY_INFO_SUCC, Const.Message.MSG_GET_PAY_INFO_FAIL);
        getPayInfoVolley.addParams("orderNo", orderNo);
        notifyPaySuccessVolley = new StringVolley(this,Const.Message.MSG_RECHARGE_NOTIFY_SUCC,Const.Message.MSG_RECHARGE_NOTIFY_FAIL);
        notifyPaySuccessVolley.addParams("orderNo",orderNo);
        myPayRequest = new MyPayRequest();
        myPayRequest.setOrderNo(orderNo);
        payChoiceDialog = new PayChoiceDialog(this).builder();
        payChoiceDialog.setAlipayListener(this);
        payChoiceDialog.setWepayListener(this);
        alipayTool = new AlipayTool(this, handler);
        wepayTool = new WepayTool(this);
        coupons.setCoupons(new ArrayList<Coupon>());
        ununsedCoupons.setCoupons(new ArrayList<Coupon>());
        registerReceiver(wepayResultReceiver, new IntentFilter(Const.MyFilter.FILTER_WE_PAY_RESULT));
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void initViews() {
        layoutGetPay = (RelativeLayout) findViewById(R.id.rl_get_pay);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText(R.string.pay_order);
        couponsDiscountText = (TextView) findViewById(R.id.text_coupons_discount);
        couponsDesText = (TextView) findViewById(R.id.text_coupon_desc);
        hasCouponsParent = (LinearLayout) findViewById(R.id.has_coupons_parent);
        noCouponsText = (TextView) findViewById(R.id.text_no_coupon);
        banlanceDiscountText = (TextView) findViewById(R.id.text_balance_discount);
        leftPriceText = (TextView) findViewById(R.id.text_left_price);
        payOrderParent = (RelativeLayout) findViewById(R.id.pay_order_parent);
        totalPriceText = (TextView) findViewById(R.id.text_order_total_price);
        doPayParent = (RelativeLayout) findViewById(R.id.do_pay_parent);
        doPayParent.setOnClickListener(this);
        selCouponParent = (RelativeLayout) findViewById(R.id.sel_coupou_parent);
        selCouponParent.setOnClickListener(this);
        totalPriceText.setText("￥" + NumberUtil.format2Dicimal(totalPrice + ""));
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        getPriceVolley.requestGet(Const.Request.pay, getHandler(), UserInfosPref.getInstance(this).getUser().getToken()
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
        showCenterLoading();

    }

    public void selectPay(View view) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wepayResultReceiver);

    }


    @Override
    protected void handlerMessage(Message msg) {

        switch (msg.what) {

            case Const.Message.MSG_GET_PAY_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    GetPriceResp priceResp = (GetPriceResp) JsonParser.jsonToObject(msg.obj + "", GetPriceResp.class);
                    initPrice(priceResp.getData());
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

            case Const.Message.MSG_PAY_CONFIRM_FAIL:

                break;

            case Const.Message.MSG_ALIPAY_RESULT:

                PayResult payResult = new PayResult((String) msg.obj);
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, ALIPAY_SUCC_RESULT)) {
                    paySucc();
                } else {
                    if (TextUtils.equals(resultStatus, "8000")) {

                    } else {
                        OperationToast.showOperationResult(this, R.string.pay_fail);

                    }
                }


                break;

            case Const.Message.MSG_SETTLE_ACCOUNTS_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    if (payChoice == SELECT_BANLANCE_PAY) {
                        paySucc();
                    } else {
                        payChoiceDialog.setAmmout(finalPrice +"");
                        payChoiceDialog.show();
                    }

                } else {
                    OperationToast.showOperationResult(this, R.string.pay_fail);
                }

                break;

            case Const.Message.MSG_SETTLE_ACCOUNTS_FAIL:
                OperationToast.showOperationResult(getApplicationContext(), R.string.pay_fail);

                break;

            case Const.Message.MSG_GET_PAY_INFO_SUCC:
                payChoiceDialog.cancel();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {

                    if (payChoice == SELECT_ALI_PAY) {
                        AlipayResp alipayResp = (AlipayResp) JsonParser.jsonToObject(msg.obj + "", AlipayResp.class);
                        alipayTool.pay(alipayResp.getData().getPrePayInfo().getParams());
                    } else if (payChoice == SELECT_WE_PAY) {
//                        WepayResp wepayResp = (WepayResp) JsonParser.jsonToObject(msg.obj + "", WepayResp.class);
//                        wepayTool.pay(wepayResp.getData());
                    }

                } else {
                    OperationToast.showOperationResult(this, R.string.pay_fail);

                }
                break;

            case Const.Message.MSG_RECHARGE_NOTIFY_SUCC:
                Intent intent = new Intent();
                intent.putExtra("order", order);
                if(order != null){
                    intent.putExtra("action",order.getAction());
                    intent.putExtra("order_index",order.getIndex());
                }
                sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
                setResult(RESULT_OK, intent);
                finish();
                break;
            case Const.Message.MSG_RECHARGE_NOTIFY_FAIL:
                Intent intent2 = new Intent();
                intent2.putExtra("order", order);
                if(order != null){
                    intent2.putExtra("action",order.getAction());
                    intent2.putExtra("order_index",order.getIndex());
                }
                sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
                setResult(RESULT_OK, intent2);
                finish();
                break;

        }

    }

    private void paySucc() {
        OperationToast.showOperationResult(getApplicationContext(), R.string.pay_succ);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        notifyPaySuccessVolley.requestGet(Const.Request.notifyPaySuccess, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );

    }

    private void initPrice(GetPayData data) {
        payOrderParent.setVisibility(View.VISIBLE);
        if (data.getDftCoupon() != null) {
            couponDiscount = (float) (Math.round(Float.parseFloat(data.getDftCoupon().getAmount()) * 100)) / 100;
            couponData = new PayData(WAY_COUPON, couponDiscount + "", data.getDftCoupon().getCouponNo());
            if(!TextUtils.isEmpty(data.getDftCoupon().getDescription())){
                couponsDesText.setText(data.getDftCoupon().getDescription());
                couponsDesText.setVisibility(View.VISIBLE);

            }else {
                couponsDesText.setVisibility(View.INVISIBLE);
            }

            couponsDiscountText.setText("-" + "￥" + NumberUtil.format2Dicimal(data.getDftCoupon().getAmount()));
            couponsDiscountText.setTag(data.getDftCoupon().getCouponNo());
            noCouponsText.setVisibility(View.INVISIBLE);
            hasCouponsParent.setVisibility(View.VISIBLE);
        } else {
            couponData = null;
            noCouponsText.setVisibility(View.VISIBLE);
            hasCouponsParent.setVisibility(View.INVISIBLE);
        }
        if (data.getDftCoupon() != null) {

            if (data.getCoupons() != null && data.getCoupons().size() > 0) {
                coupons.getCoupons().clear();
                for (Coupon c : data.getCoupons()) {
                    if (c.getUsable() != 1 && (c.getStatus() == 1 || c.getStatus() == 2)) {
                        c.setStatus(5);
                    }
                }
                coupons.getCoupons().addAll(data.getCoupons());

            }

//            coupons .getCoupons().addAll(data.getUnusableCoupons());
        }

        if(data.getUnusableCoupons()  != null && data.getUnusableCoupons().size() >0){
            ununsedCoupons.getCoupons().addAll(data.getUnusableCoupons());
        }
        myBlance = (float) (Math.round(Float.parseFloat(data.getBalance() + "") * 100)) / 100;
        refreshFinalPay(couponDiscount);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_COUPON && resultCode == RESULT_OK) {
            String desc = data.getStringExtra("desc");
            String ammout = data.getStringExtra("ammout");
            String id = data.getStringExtra("id");

            if (!TextUtils.isEmpty(desc)) {
                couponsDesText.setText(desc);
                couponsDesText.setVisibility(View.VISIBLE);
            }else {
                couponsDesText.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(ammout)) {
                couponsDiscountText.setText("-" + NumberUtil.format2Dicimal(ammout));
            }

            couponsDiscountText.setTag(id);
            couponDiscount = (float) (Math.round((Float.parseFloat(ammout)) * 100)) / 100;
            couponData = new PayData(12, couponDiscount + "", id);
            refreshFinalPay(couponDiscount);
        }

    }

    public void selectCoupon(View view) {

    }

    float totalPrice = 0;
    float myBlance = 0;
    float balanceDiscount = 0;
    float finalPrice = 0;
    int payChoice = -1;
    PayData banlanceData;

    private void refreshFinalPay(float couponDiscount) {
        float afterDiscount = (float) (Math.round((totalPrice - couponDiscount) * 100)) / 100;
        if(Float.compare(afterDiscount,0.0f) <= 0){
            balanceDiscount = 0;
            finalPrice = 0;
        }else {
            if (Float.compare(afterDiscount, myBlance) > 0) {
                balanceDiscount = myBlance;
                finalPrice = (float) (Math.round((afterDiscount - balanceDiscount) * 100)) / 100;
            } else {
                balanceDiscount = (float) (Math.round((afterDiscount) * 100)) / 100;
                finalPrice = 0;
            }
        }

        banlanceDiscountText.setText("-" + "￥" + NumberUtil.format2Dicimal(balanceDiscount + ""));
        leftPriceText.setText("￥" + NumberUtil.format2Dicimal(finalPrice + ""));
        if (Float.compare(finalPrice, 0) == 0) {
            payChoice = SELECT_BANLANCE_PAY;
        } else {
            payChoice = 0;
        }
    }

    MyPayRequest myPayRequest;
    @Override
    public void onClick(View view) {
        if (view == doPayParent) {
            if (payChoice == SELECT_BANLANCE_PAY) {
                JSONArray payListArray = null;
                myPayRequest.getPayList().remove(new PayData(WAY_BALANCE));
                myPayRequest.getPayList().remove(new PayData(WAY_COUPON));
                if (Float.compare(balanceDiscount, 0) != 0) {
                    banlanceData = new PayData(WAY_BALANCE, balanceDiscount + "");
                    myPayRequest.getPayList().add(banlanceData);
                }
                if (couponData != null) {
                    myPayRequest.getPayList().add(couponData);
                }
                try {
                    JSONObject object = new JSONObject(JsonParser.objectToJsonStr(myPayRequest));
                    payListArray = object.getJSONArray("payList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (payListArray != null) {
                    settleAccountsVolley.addParams("payList", payListArray);
                }
                banlancePayDialog = new BanlancePayDialog(this, balanceDiscount + "", totalPrice + "").builder();
                banlancePayDialog.setBanlanceOkLinstener(this);
                banlancePayDialog.show();
            } else {
                thirdPartData = new PayData(WAY_THIRD_PARTY_PAY, finalPrice + "");
                JSONArray payListArray = null;
                myPayRequest.getPayList().remove(new PayData(WAY_BALANCE));
                myPayRequest.getPayList().remove(new PayData(WAY_COUPON));
                myPayRequest.getPayList().remove(new PayData(WAY_THIRD_PARTY_PAY));
                if (Float.compare(balanceDiscount, 0) != 0) {
                    banlanceData = new PayData(WAY_BALANCE, balanceDiscount + "");
                    myPayRequest.getPayList().add(banlanceData);
                }
                if (couponData != null) {
                    myPayRequest.getPayList().add(couponData);
                }
                myPayRequest.getPayList().add(thirdPartData);
                try {
                    JSONObject object = new JSONObject(JsonParser.objectToJsonStr(myPayRequest));
                    payListArray = object.getJSONArray("payList");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (payListArray != null) {
                    settleAccountsVolley.addParams("payList", payListArray);
                }
                LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();

                settleAccountsVolley.requestPost(Const.Request.settleAccounts,this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                );
            }

        } else if (view == selCouponParent) {
            if (coupons.getCoupons().size() > 0 || ununsedCoupons.getCoupons().size() > 0) {
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
            } else {
                OperationToast.showOperationResult(this, R.string.no_avialable_coupons);
            }
        }
    }
    @Override
    public void banlanceOkClicked() {
        setOperationMsg(getString(R.string.paying));
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        settleAccountsVolley.requestPost(Const.Request.settleAccounts, this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

    }

    @Override
    public void selectAlipay() {
        payChoice = SELECT_ALI_PAY;
        getPayInfoVolley.addParams("payChannel", SELECT_ALI_PAY + "");
        setOperationMsg(getString(R.string.paying));
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        getPayInfoVolley.requestPost(Const.Request.getPrePayInfo, handler, this, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    @Override
    public void selectWepay() {
        payChoice = SELECT_WE_PAY;
        getPayInfoVolley.addParams("payChannel", SELECT_WE_PAY + "");
        setOperationMsg(getString(R.string.paying));
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        getPayInfoVolley.requestPost(Const.Request.getPrePayInfo, handler, this, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    BroadcastReceiver wepayResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int reuslt = intent.getIntExtra("pay_result", 0);
            if (reuslt == WEPAY_SUCC_RESULT) {
                paySucc();
            } else {
                OperationToast.showOperationResult(context, R.string.pay_fail);

            }
        }
    };
}
