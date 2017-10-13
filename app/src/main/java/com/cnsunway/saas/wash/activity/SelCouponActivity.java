package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.adapter.AllCouponAdapter;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Coupon;
import com.cnsunway.saas.wash.model.CouponTitle;
import com.cnsunway.saas.wash.model.Coupons;
import com.cnsunway.saas.wash.sharef.UserInfosPref;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2015/12/8.
 */
public class SelCouponActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    TextView title;
    String token;
    UserInfosPref userInfos;
    ListView couponList;
    Coupons usableCoupons;
    Coupons unusableCoupons;
    AllCouponAdapter allCouponAdapter;
    List<Object> typeCoupons= new ArrayList<>();
    LinearLayout noCouponsParent;
    TextView noCouponsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_coupon);
        initData();
        initViews();
    }


    protected void initData() {
        userInfos = UserInfosPref.getInstance(this);
        token = userInfos.getUser().getToken();
        usableCoupons = (Coupons) JsonParser.jsonToObject(getIntent().getStringExtra("usableCoupons"), Coupons.class);
        unusableCoupons = (Coupons) JsonParser.jsonToObject(getIntent().getStringExtra("unusableCoupons"), Coupons.class);
        if(usableCoupons != null && usableCoupons.getCoupons() != null && usableCoupons.getCoupons().size() > 0){
            CouponTitle title = new CouponTitle();
            title.setTitle("可用优惠券");
            title.setCount(usableCoupons.getCoupons().size());
            typeCoupons.add(title);
            typeCoupons.addAll(usableCoupons.getCoupons());
        }
        if(unusableCoupons != null && unusableCoupons.getCoupons() != null && unusableCoupons.getCoupons().size() > 0){
            CouponTitle title = new CouponTitle();
            title.setTitle("不可用优惠券");
            title.setCount(unusableCoupons.getCoupons().size());
            typeCoupons.add(title);
            typeCoupons.addAll(unusableCoupons.getCoupons());
        }




//        selectId = getIntent().getStringExtra("id");
    }

    protected void initViews() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("选择优惠券");
        couponList = (ListView) findViewById(R.id.lv_coupon);
        couponList.setOnItemClickListener(this);
        noCouponsText = (TextView) findViewById(R.id.text_no_coupon);
        noCouponsText.setText("没有可用优惠券哦～");
        noCouponsParent = (LinearLayout) findViewById(R.id.ll_coupon_none);
        if(typeCoupons.size() == 0){
            noCouponsParent.setVisibility(View.VISIBLE);
        }else {
            initList(typeCoupons);
        }

    }

    public void back(View view) {
        finish();
    }

    private void initList(List<Object> coupons) {
        if (allCouponAdapter == null) {
            allCouponAdapter = new AllCouponAdapter(this, coupons);
//        allCouponAdapter.setNoItemListener(this);
            allCouponAdapter.setCoupons(coupons);

            couponList.setAdapter(allCouponAdapter);
        } else {
            allCouponAdapter.getCoupons().clear();
            allCouponAdapter.getCoupons().addAll(coupons);
//            if(!TextUtils.isEmpty(selectId)){
//                allCouponAdapter.setSelectCoupons(selectId);
//            }
//            allCouponAdapter.notifyDataSetChanged();
        }
    }

    private void loadMoreList() {
    }

    public void noItem() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          Object data = adapterView.getAdapter().getItem(i);
            if(data instanceof Coupon){
                Coupon coupon = (Coupon) data;
                if(coupon.getUsable() != 0){
                    Intent selectedData = new Intent();
                    selectedData.putExtra("desc", coupon.getDescription());
                    selectedData.putExtra("ammout", coupon.getAmount());
                    selectedData.putExtra("id", coupon.getCouponNo());
                    selectedData.putExtra("coupon",JsonParser.objectToJsonStr(coupon));
                    setResult(RESULT_OK, selectedData);
                    finish();
                }
            }
//            if (c.getUsable() != 0) {
//
//            }

    }
}
