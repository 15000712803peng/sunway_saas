package com.cnsunway.saas.wash.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.StoreDetailActivity;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Product;
import com.cnsunway.saas.wash.resp.ProductsResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class ProductsFragment extends BaseFragment{

    String storeId;
    String categoryId;
    JsonVolley getProductsVolley;
    LocationForService locationForService;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         storeId = getArguments().getString("store_id");
         categoryId = getArguments().getString("category_id");
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        getProductsVolley = new JsonVolley(getActivity(), Const.Message.MSG_GET_PRODUCTS_SUCC,Const.Message.MSG_GET_PRODUCTS_FAIL);

    }

    GridView clothGrid;
    ProductAdapter productAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     if(getView() == null){
         setView(inflater.inflate(R.layout.fragment_products,container,false));
         clothGrid = (GridView) getView().findViewById(R.id.product_grid);
         getProductsVolley.requestGet(Const.Request.products + "/" + storeId +"/categories/" + categoryId+"/products",getHandler(), UserInfosPref.getInstance(getActivity()).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

     }

        return getView();
    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_GET_PRODUCTS_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    ProductsResp resp = (ProductsResp) JsonParser.jsonToObject(msg.obj +"",ProductsResp.class);
                    if(resp.getData() !=null && resp.getData().size() > 0){
                        clothGrid.setAdapter(new ProductAdapter(resp.getData()));
                    }
                }
                break;

            case Const.Message.MSG_GET_PRODUCTS_FAIL:
                break;

        }
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



    class ProductAdapter extends BaseAdapter{

        List<Product> products;

        public ProductAdapter(List<Product> products){
            this.products = products;
        }
        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int position) {
            return products.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder holder;
            final Product product = (Product) getItem(position);
            if(convertView == null){
                holder = new Holder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.product_item,null);
                holder.productImage = (ImageView) convertView.findViewById(R.id.product_image);
                holder.productName = (TextView) convertView.findViewById(R.id.product_name);
                holder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
                convertView.setTag(holder);
            }else {
                holder = (Holder) convertView.getTag();
            }

            holder.productPrice.setText(product.getBasePrice());
            holder.productName.setText(product.getProductName());
            Glide.with(getActivity()).load(product.getImgUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.store).into(holder.productImage);
            return convertView;
        }

        class Holder{
            ImageView productImage;
            TextView productName;
            TextView productPrice;
        }
    }
}
