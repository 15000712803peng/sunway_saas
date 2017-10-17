package com.cnsunway.saas.wash.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.fragment.ProductsFragment;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.ProductCatogery;
import com.cnsunway.saas.wash.resp.ProductCatogeriesResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.PagerSlidingTabStrip;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



public class MorePriceActivity extends InitActivity{

    JsonVolley getStoreCatogeriesVolley;
    String storeId;
    LocationForService locationForService;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.pager)
    ViewPager pagers;
    @Bind(R.id.text_title)
    TextView title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_more_price);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        storeId = getIntent().getStringExtra("store_id");
        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        getStoreCatogeriesVolley = new JsonVolley(this, Const.Message.MSG_GET_STORE_CATEGORIES_SUCC,Const.Message.MSG_GET_STORE_CATEGORIES_FAIL);
        getStoreCatogeriesVolley.requestGet(Const.Request.storeCategories + "/" + storeId +"/categories",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());


    }

    private void initTabs(List<ProductCatogery> products){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        pagers.setAdapter(new ProductAdapter(getSupportFragmentManager(),products));
        tabs.setViewPager(pagers);

        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);

        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setTextColor(Color.parseColor("#111D2D"));
//
        tabs.setIndicatorColor(Color.parseColor("#20B1D9"));
        // 设置选中Tab文字的颜色
//        tabs.setSelectedTextColor(getResources().getColor(
//                R.color.green));

        tabs.setSelectedTextColor(Color.parseColor("#20B1D9"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
        tabs.setShouldExpand(false);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        title.setText("价目表");
    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_GET_STORE_CATEGORIES_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    ProductCatogeriesResp resp = (ProductCatogeriesResp) JsonParser.jsonToObject(msg.obj +"",ProductCatogeriesResp.class);
                    if(resp.getData() != null && resp.getData().size() > 0){
                        initTabs(resp.getData());
                    }
                }else {

                }
                break;

            case Const.Message.MSG_GET_STORE_CATEGORIES_FAIL:

                break;

        }
    }

    private void loadProducts(List<ProductCatogery> catogeries){

    }

    public class ProductAdapter extends FragmentPagerAdapter {

        String[] titles;
        List<ProductCatogery> productCategories;


        public ProductAdapter(FragmentManager fm, List<ProductCatogery> productCategories) {
            super(fm);
            this.productCategories = productCategories;


        }

        @Override
        public CharSequence getPageTitle(int position) {

            return productCategories.get(position).getCategoryName();
        }

        @Override
        public Fragment getItem(int position) {
            ProductCatogery productCategory = productCategories.get(position);
            ProductsFragment productsFragment =  new ProductsFragment();
            Bundle productsData = new Bundle();
            productsData.putString("store_id", productCategory.getStoreId());
            productsData.putString("category_id", productCategory.getId());
            productsFragment.setArguments(productsData);
            return productsFragment;
        }

        @Override
        public int getCount() {
            return productCategories.size();
        }
    }

    public void back(View view){
        finish();
    }
}
