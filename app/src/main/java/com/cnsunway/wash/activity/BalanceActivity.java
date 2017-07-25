package com.cnsunway.wash.activity;


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

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.dialog.PayChoiceDialog;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.framework.net.StringVolley;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.model.PayResult;
import com.cnsunway.wash.resp.AccountResp;
import com.cnsunway.wash.resp.RechargeAlipayResp;
import com.cnsunway.wash.resp.RechargeWxpayResp;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.util.AlipayTool;
import com.cnsunway.wash.util.FontUtil;
import com.cnsunway.wash.util.WepayTool;

import java.text.DecimalFormat;

/**
 * Created by LL on 2016/3/25.
 */
public class BalanceActivity extends LoadingActivity implements PayChoiceDialog.SelectAlipayListener, PayChoiceDialog.SelectWepayListener, View.OnClickListener {

    TextView titleText;
    PayChoiceDialog rechargeDialog;
    StringVolley accountVolley;
    RelativeLayout balanceParent;
    TextView balanceText, rechargeText;
    JsonVolley startRechargeVolley;
    private static final int ALIPAY_RECHARGE = 33;
    private static final int WX_RECHARGE = 32;
    LinearLayout banlanceDetailParent;
    JsonVolley rechargeSuccesVolley;
    LinearLayout layoutBalance;
    TextView textRechargePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_banlance);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutBalance, "OpenSans-Regular.ttf");
    }

    @Override
    protected void initData() {

        rechargeDialog = new PayChoiceDialog(this).builder();
        rechargeDialog.setAlipayListener(this);
        rechargeDialog.setWepayListener(this);
        accountVolley = new StringVolley(this,
                Const.Message.MSG_ACCOUNT_ALL_SUCC,
                Const.Message.MSG_ACCOUNT_ALL_FAIL);
        startRechargeVolley = new JsonVolley(this, Const.Message.MSG_START_RECHAEGE_SUCC, Const.Message.MSG_START_RECHAEGE_FAIL);
        rechargeSuccesVolley = new JsonVolley(this, Const.Message.MSG_RECHARGE_NOTIFY_SUCC, Const.Message.MSG_RECHARGE_NOTIFY_FAIL);
        alipayTool = new AlipayTool(this, getHandler());
        wepayTool = new WepayTool(this);
        registerReceiver(wepayResultReceiver, new IntentFilter(Const.MyFilter.FILTER_WE_PAY_RESULT));

    }

    @Override
    protected void initViews() {
        textRechargePermission = (TextView) findViewById(R.id.tv_recharge_permission);
        layoutBalance = (LinearLayout) findViewById(R.id.ll_balance);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText(R.string.title_banlance);
        balanceParent = (RelativeLayout) findViewById(R.id.balance_parent);
        balanceText = (TextView) findViewById(R.id.text_balance);
        rechargeText = (TextView) findViewById(R.id.text_recharge);
        rechargeText.setOnClickListener(this);
        textRechargePermission.setOnClickListener(this);
        banlanceDetailParent = (LinearLayout) findViewById(R.id.balance_detail_parent);
        banlanceDetailParent.setOnClickListener(this);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        accountVolley.requestGet(Const.Request.all,
                getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
        showCenterLoading();

    }

    private void refreshBanlance(String banlance) {
        balanceParent.setVisibility(View.VISIBLE);
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if (Float.compare(Float.parseFloat(banlance), 0.0f) == 0) {
            balanceText.setText("0.00");
        } else {
            String b = decimalFormat.format(Float.parseFloat(banlance));
            if (b.startsWith(".")) {
                b = "0" + b;
            }
            balanceText.setText("￥" + b);
        }

    }

    String balanceNum;
    String depositOrderNo;
    AlipayTool alipayTool;
    WepayTool wepayTool;
    private static final String ALIPEY_PAY_SUCC = "9000";
    private static final String ALIPEY_PAYING = "8000";

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_ACCOUNT_ALL_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    AccountResp initResp = (AccountResp) JsonParser
                            .jsonToObject(msg.obj + "", AccountResp.class);
                    balanceNum = initResp.getData().getBalance();
                    refreshBanlance(balanceNum);
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    showNetFail();
                }
                break;

            case Const.Message.MSG_ACCOUNT_ALL_FAIL:
                hideCenterLoading();
                showNetFail();

                break;

            case Const.Message.MSG_START_RECHAEGE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    if (rechargeChoice == ALIPAY_RECHARGE) {
                        RechargeAlipayResp alipayResp = (RechargeAlipayResp) JsonParser.jsonToObject(msg.obj + "", RechargeAlipayResp.class);
                        depositOrderNo = alipayResp.getData().getDepositOrderNo();
                        alipayTool.pay(alipayResp.getData().getPayInfo());
                    } else if (rechargeChoice == WX_RECHARGE) {
                        RechargeWxpayResp wepayResp = (RechargeWxpayResp) JsonParser.jsonToObject(msg.obj + "", RechargeWxpayResp.class);
                        depositOrderNo = wepayResp.getData().getDepositOrderNo();
                        wepayTool.pay(wepayResp.getData().getPayInfo());
                    }
                } else {
                    OperationToast.showOperationResult(this, R.string.recharge_fail);
                    rechargeDialog.cancel();
                }
                break;

            case Const.Message.MSG_ALIPAY_RESULT:
                rechargeDialog.cancel();
                PayResult payResult = new PayResult((String) msg.obj);
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, ALIPEY_PAY_SUCC)) {
                    rechargeSucc();
                } else {
                    if (TextUtils.equals(resultStatus, ALIPEY_PAYING)) {
                    } else {
                        OperationToast.showOperationResult(this, R.string.recharge_fail);
                    }
                }
                break;
        }

    }

    public void back(View view) {
        finish();
    }

    int rechargeChoice = 0;
    @Override
    public void selectAlipay() {
        rechargeChoice = ALIPAY_RECHARGE;
        startRechargeVolley.addParams("paymentAmount", "100");
        startRechargeVolley.addParams("payChannel", ALIPAY_RECHARGE + "");
        setOperationMsg(getString(R.string.paying));
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        startRechargeVolley.requestPost(Const.Request.startRecharge, this, handler, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    public void selectWepay() {
        rechargeChoice = WX_RECHARGE;
        startRechargeVolley.addParams("paymentAmount", "100");
        startRechargeVolley.addParams("payChannel", WX_RECHARGE + "");
        setOperationMsg(getString(R.string.paying));
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        startRechargeVolley.requestPost(Const.Request.startRecharge, this, handler, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    public void onClick(View view) {
        if (view == rechargeText) {
            rechargeDialog.show();
        } else if (view == banlanceDetailParent) {
            startActivity(new Intent(this, BalanceDetailActivity.class));

        } else if (view == textRechargePermission) {
            Intent intent = new Intent(this,WebActivity.class);
            intent.putExtra("url",Const.Request.rechargeagreement);
            intent.putExtra("title",getString(R.string.recharge_permission));
            startActivity(intent);
//            startActivity(new Intent(this, RechargePermissionActivity.class));
        }
    }

    private void rechargeSucc() {
        OperationToast.showOperationResult(this,R.string.recharge_succ);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        accountVolley.requestGet(Const.Request.all,
                getHandler(), UserInfosPref.getInstance(this).getUser().getToken()
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
        if (!TextUtils.isEmpty(depositOrderNo)) {
            rechargeSuccesVolley.addParams("depositOrderNo", depositOrderNo);
            rechargeSuccesVolley.requestGet(Const.Request.rechargeSucces, getHandler(), UserInfosPref.getInstance(this).getUser().getToken()
                    ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
        }
    }

    BroadcastReceiver wepayResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int reuslt = intent.getIntExtra("pay_result", 0);
            if (reuslt == 1) {
                rechargeDialog.cancel();
                rechargeSucc();
            } else {
                OperationToast.showOperationResult(context, R.string.recharge_fail);
                rechargeDialog.cancel();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wepayResultReceiver);
    }
}
