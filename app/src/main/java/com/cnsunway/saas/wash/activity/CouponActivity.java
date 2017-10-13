package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.adapter.CouponAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.CouponExchangeDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Coupon;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.resp.CouponResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.XListView;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class CouponActivity extends InitActivity implements View.OnClickListener,XListView.IXListViewListener{
    TextView textTitle,couponNumText;
    LinearLayout layoutExchange, layoutNone,couponNum;
    RelativeLayout layoutNetFail;
    EditText editExchangeCode;
    Button btnExchange;
    XListView listCoupon;
    CouponAdapter couponAdapter;
    UserInfosPref userInfos;
    JsonVolley couponVolley;
    StringVolley exchangeCouponVolley;
    String token;
    TextView historyText1;
    private static final int EXCHANGE_ERROR_1 = 1;
    private static final int EXCHANGE_ERROR_2 = 2;
    int page = 1;
    int pageSize = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coupon);
        super.onCreate(savedInstanceState);
        Log.e("----------","counpon activity");
    }

    @Override
    protected void initData() {
        userInfos = UserInfosPref.getInstance(this);
        token = userInfos.getUser().getToken();
        couponVolley = new JsonVolley(this,
                Const.Message.MSG_COUPON_LIST_SUCC,
                Const.Message.MSG_COUPON_LIST_FAIL);
        couponVolley.addParams("pageNo",page);
        couponVolley.addParams("pageSize",pageSize);
        exchangeCouponVolley = new StringVolley(this,
                Const.Message.MSG_EXCHANGE_COUPON_SUCC,
                Const.Message.MSG_EXCHANGE_COUPON_FAIL);
    }

    @Override
    protected void initViews() {
        textTitle = (TextView) findViewById(R.id.text_title);
        textTitle.setText("优惠券");
        layoutNone = (LinearLayout) findViewById(R.id.ll_coupon_none);
        layoutNetFail = (RelativeLayout) findViewById(R.id.rl_network_fail);
        layoutExchange = (LinearLayout) findViewById(R.id.ll_exchange);
        editExchangeCode = (EditText) findViewById(R.id.et_exchange_code);
        btnExchange = (Button) findViewById(R.id.btn_exchange);
        listCoupon = (XListView) findViewById(R.id.lv_coupon);
        historyText1 = (TextView) findViewById(R.id.text_coupon_history);
        historyText1.setOnClickListener(this);
        listCoupon.setPullRefreshEnable(true);
        listCoupon.setPullLoadEnable(true);
        listCoupon.setXListViewListener(this);
        listCoupon.setPullLoadEnable(false);
        listCoupon.setPullRefreshEnable(false);
        btnExchange.setOnClickListener(this);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        couponVolley.requestGet(Const.Request.coupon, getHandler(), token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
        layoutExchange.setVisibility(View.INVISIBLE);
        showLoading();
        couponNum = (LinearLayout) findViewById(R.id.ll_coupon_number);
        couponNumText = (TextView) findViewById(R.id.tv_coupon_num);

    }

    public void back(View view) {
        finish();
    }

    private void showNetFail() {
        hideLoading();
        layoutExchange.setVisibility(View.INVISIBLE);
        layoutNetFail.setVisibility(View.VISIBLE);
    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_COUPON_LIST_SUCC:
                listCoupon.stopRefresh();
                listCoupon.stopLoadMore();
                hideLoading();
                layoutExchange.setVisibility(View.VISIBLE);
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
//                    listCoupon.setRefreshTime(DateUtil.getCurrentDate());
//                    listCoupon.stopRefresh(this.getResources()
//                            .getString(R.string.xlistview_refresh_succ));
                    CouponResp initResp = (CouponResp) JsonParser
                            .jsonToObject(msg.obj + "", CouponResp.class);
                    List<Coupon> initCoupons = initResp.getData();
//                    List<Coupon> initCoupons = initResp.getData().getCoupons();//符合使用规则的优惠券
//                    List<UnusableCoupon> initUnCoupons = initResp.getData().getUnusableCoupons();//不符合使用规则的优惠券

                    if (initCoupons == null || initCoupons.size() == 0) {
                        layoutNone.setVisibility(View.VISIBLE);
                        couponNum.setVisibility(View.GONE);
//                        layoutNone.setOnClickListener(this);
                        return;
                    }


                    initList(initCoupons);
                    int num = initCoupons.size();
                    couponNumText.setText(num+"");

                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
//                    listCoupon.stopRefresh(this.getResources()
//                            .getString(R.string.xlistview_refresh_fail));
                    layoutNone.setVisibility(View.VISIBLE);
                    couponNum.setVisibility(View.GONE);
//                    layoutNone.setOnClickListener(this);
                }

                break;

            case Const.Message.MSG_COUPON_LIST_FAIL:
                listCoupon.stopRefresh();
                listCoupon.stopLoadMore();
//                listCoupon.stopRefresh(this.getResources()
//                        .getString(R.string.xlistview_refresh_fail));
                showNetFail();
                layoutNetFail.setOnClickListener(this);
                break;
            case Const.Message.MSG_EXCHANGE_COUPON_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    BaseResp baseResp = (BaseResp) JsonParser.jsonToObject(msg.obj + "",BaseResp.class);
                    LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
                    couponVolley.requestGet(Const.Request.coupon,
                            getHandler(), token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                    editExchangeCode.setText("");
                    CouponExchangeDialog dialog = new CouponExchangeDialog(this).builder("兑换成功", baseResp.getResponseMsg());
                    dialog.show();
                    sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_MINE_TABS));
                    onRefresh();
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {

                    if (msg.arg2 == EXCHANGE_ERROR_1) {
                        if (!TextUtils.isEmpty(msg.obj + "")) {
                            CouponExchangeDialog dialog = new CouponExchangeDialog(this).builder("兑换失败", msg.obj + "");
                            dialog.show();
                        }
                    } else if (msg.arg2 == EXCHANGE_ERROR_2) {
                        if (!TextUtils.isEmpty(msg.obj + "")) {
                            CouponExchangeDialog dialog = new CouponExchangeDialog(this).builder("哎呀! 来晚了", msg.obj + "");
                            dialog.show();
                        }
                    }

                }
                break;

            case Const.Message.MSG_EXCHANGE_COUPON_FAIL:
                OperationToast.showOperationResult(this, R.string.exchange_fail);
                break;
            default:
                break;

        }
    }

    public void onRefresh() {
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        couponVolley.requestGet(Const.Request.coupon, getHandler(), token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    public void onLoadMore() {

    }

    @Override
    public void onClick(View v) {
        if (v == btnExchange) {
            String code = editExchangeCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                OperationToast.showOperationResult(CouponActivity.this, R.string.input_exchange_code);
                return;
            }
            exchangeCouponVolley.addOneOrderedParams(URLEncoder.encode(code));
            setOperationMsg(getResources().getString(R.string.operating));
            LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
            exchangeCouponVolley._requestGet(Const.Request.exchangeCoupon, getHandler(), this, token,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
        } else if (v == layoutNetFail) {
            showLoading();
            layoutNetFail.setVisibility(View.INVISIBLE);
            onRefresh();
        }else if(v == historyText1){
            startActivity(new Intent(this,HistoryCouponsActivity.class));
        }

    }

    private void initList(List<Coupon> coupons) {
        layoutNone.setVisibility(View.GONE);
        if (couponAdapter == null) {
            couponAdapter = new CouponAdapter(this, coupons);
            couponAdapter.setCoupons(coupons);
            listCoupon.setAdapter(couponAdapter);
        } else {
            couponAdapter.getCoupons().clear();
            couponAdapter.getCoupons().addAll(coupons);
            couponAdapter.notifyDataSetChanged();
        }
        listCoupon.showHistoryTextFooter(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CouponActivity.this,HistoryCouponsActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
