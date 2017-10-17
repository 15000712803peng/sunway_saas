package com.cnsunway.saas.wash.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.MorePriceActivity;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Product;
import com.cnsunway.saas.wash.model.ProductCatogery;
import com.cnsunway.saas.wash.model.SecondCategories;
import com.cnsunway.saas.wash.resp.ProductsResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.PagerSlidingTabStrip;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class CategoryProductsFragment extends BaseFragment{

    String storeId;
    LocationForService locationForService;
    List<ProductCatogery> productCatogeries;
    PagerSlidingTabStrip tabs;
    ViewPager pagers;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         storeId = getArguments().getString("store_id");
        productCatogeries = ((SecondCategories)JsonParser.jsonToObject(getArguments().getString("second_ategories"),SecondCategories.class)).getSecondCategories();
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();

    }

    private void initTabs(List<ProductCatogery> products){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        pagers.setAdapter(new ProductAdapter(getChildFragmentManager(),products));
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     if(getView() == null){
         setView(inflater.inflate(R.layout.fragment_category_products,container,false));
         tabs = (PagerSlidingTabStrip) getView().findViewById(R.id.tabs);
         pagers = (ViewPager) getView().findViewById(R.id.pager);
         initTabs(productCatogeries);
     }

        return getView();
    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    protected void initFragmentDatas() {

    }

    @Override
    protected void initMyViews(View view) {

    }



    @Override
    public void onStart() {
        super.onStart();

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
                productsData.putString("store_id", storeId);
                productsData.putString("category_id", productCategory.getId());
                productsFragment.setArguments(productsData);
                return productsFragment;


        }

        @Override
        public int getCount() {
            return productCategories.size();
        }
    }
}
