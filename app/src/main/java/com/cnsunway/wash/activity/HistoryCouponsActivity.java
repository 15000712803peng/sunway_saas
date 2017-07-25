package com.cnsunway.wash.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.adapter.CouponAdapter;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.Coupon;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.resp.HistoryCouponResp;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.view.XListView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class HistoryCouponsActivity extends InitActivity implements XListView.IXListViewListener{

    TextView titleText,noHistoryCoupon;
    XListView listCoupon;
    JsonVolley couponVolley;
    int page = 1;
    int pageSize = 10;
    LinearLayout layoutNone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history_conpons);
        super.onCreate(savedInstanceState);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText(R.string.history_coupons);
        listCoupon = (XListView) findViewById(R.id.lv_coupon);
        listCoupon.setPullLoadEnable(true);
        listCoupon.setPullRefreshEnable(true);
        listCoupon.setXListViewListener(this);
        layoutNone = (LinearLayout) findViewById(R.id.ll_coupon_none);
        noHistoryCoupon = (TextView) findViewById(R.id.tv_no_history_coupon);
        noHistoryCoupon.setText("您还没有优惠券哦~");
        couponVolley = new JsonVolley(this,
                Const.Message.MSG_PAST_COUPONS_SUCC,
                Const.Message.MSG_PAST_COUPONS_FAIL);
        couponVolley.addParams("pageNo",page);
        couponVolley.addParams("pageSize",pageSize);
        showLoading();
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        couponVolley.requestGet(Const.Request.pastCoupons, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_PAST_COUPONS_SUCC:
                hideLoading();
             listCoupon.stopRefresh();
            listCoupon.stopLoadMore();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    HistoryCouponResp initResp = (HistoryCouponResp) JsonParser
                            .jsonToObject(msg.obj + "", HistoryCouponResp.class);
                    List<Coupon> initCoupons = initResp.getData().getResults();
                    if (initCoupons == null || initCoupons.size() == 0) {
                        if(initResp.getData().getPaginator().getPage() == 1){
                            layoutNone.setVisibility(View.VISIBLE);
                        }
                        if(initResp.getData().getPaginator().isLastPage()){
                            listCoupon.setPullLoadEnable(false);
                        }else {
                            listCoupon.setPullLoadEnable(true);
                        }
                        return;
                    }
                    if (initResp.getData().getPaginator().getPage() == 1) {
                       initList(initCoupons);
                    } else {
                        couponAdapter.getCoupons().addAll(initCoupons);
                        couponAdapter.notifyDataSetChanged();
                    }
                    if(initResp.getData().getPaginator().isLastPage()){
                        listCoupon.setPullLoadEnable(false);
                    }else {
                        listCoupon.setPullLoadEnable(true);
                    }
//                    initList(initCoupons);
                }

            break;

            case Const.Message.MSG_PAST_COUPONS_FAIL:
                break;
        }

    }
    public void back(View view){
        finish();
    }
    CouponAdapter couponAdapter;

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

    }

    @Override
    public void onRefresh() {
        page = 1;
        couponVolley.addParams("pageNo",page);
        couponVolley.addParams("pageSize",pageSize);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        couponVolley.requestGet(Const.Request.pastCoupons, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    @Override
    public void onLoadMore() {
        page += 1;
        couponVolley.addParams("pageNo",page);
        couponVolley.addParams("pageSize",pageSize);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        couponVolley.requestGet(Const.Request.pastCoupons, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }
}
