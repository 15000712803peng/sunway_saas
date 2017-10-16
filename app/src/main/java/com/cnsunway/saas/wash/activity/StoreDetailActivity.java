package com.cnsunway.saas.wash.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Marketing;
import com.cnsunway.saas.wash.model.Product;
import com.cnsunway.saas.wash.model.Store;
import com.cnsunway.saas.wash.model.StoreDetail;
import com.cnsunway.saas.wash.model.StoreImage;
import com.cnsunway.saas.wash.resp.StoreDetailResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class StoreDetailActivity extends InitActivity implements OnBannerListener {

    Banner storeBanner;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    JsonVolley storeVolley;
    LocationForService locationForService;
    String storeId;
    RecyclerView productsView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_store_detail);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra("store_id");
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.bannder_loading)
                .showImageForEmptyUri(R.mipmap.bannder_loading)
                .showImageOnFail(R.mipmap.bannder_loading).cacheInMemory()
                .cacheOnDisc()
                .build();

        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        storeVolley = new JsonVolley(this, Const.Message.MSG_GET_STORE_DETAIL_SUCC,Const.Message.MSG_GET_STORE_DETAIL_FAIL);
    }

    @Override
    protected void initViews() {
        storeBanner = (Banner) findViewById(R.id.banner_store);
        productsView = (RecyclerView) findViewById(R.id.products_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productsView.setLayoutManager(layoutManager);
        ViewGroup.LayoutParams layoutParams = productsView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
        }
        //高度等于＝条目的高度＋ 10dp的间距 ＋ 10dp（为了让条目居中）
        layoutParams.height = dip2px(140);
        productsView.setLayoutParams(layoutParams);
        productsView.setBackgroundColor(Color.WHITE);
//        productsView.addItemDecoration(new SpaceItemDecoration(10));
        initStoreBanner();
        storeVolley.requestGet(Const.Request.storeDetail + "/" + storeId + "/detail",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

    }

    private int dip2px(float dip) {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
        return (int) (v + 0.5f);
    }

    private void loadStoreImages(StoreDetail store){
        storeBanner.setImages(store.getPics())
                .setImageLoader(new ImageLoaderInterface(){
                    @Override
                    public void displayImage(Context context, Object path, View view) {
                        StoreImage marketing = (StoreImage) path;
                        ImageView imageView = (ImageView) view.findViewById(R.id.image_store_item);
                        imageLoader.displayImage(marketing.getPicUrl(), imageView, options);
                    }
                    @Override
                    public View createImageView(Context context) {
                        View view = LayoutInflater.from(StoreDetailActivity.this).inflate(R.layout.store_image_item, null);
                        return view;

                    }
                }).setOnBannerListener(this).start();


    }

    private void initStoreBanner(){
        storeBanner.setFocusable(true);
        storeBanner.setFocusableInTouchMode(true);
        storeBanner.requestFocus();
        storeBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

    }

    private void fillDetail(StoreDetail store){
        loadStoreImages(store);
        if(store.getHotProducts() != null){
            Log.e("-------------","size:" + store.getHotProducts().size());
            productsView.setAdapter(new HotProductsAdapter(store.getHotProducts()));
//            productsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_GET_STORE_DETAIL_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    StoreDetailResp resp = (StoreDetailResp) JsonParser.jsonToObject(msg.obj +"",StoreDetailResp.class);
                    fillDetail(resp.getData());
                }
                break;

            case Const.Message.MSG_GET_STORE_DETAIL_FAIL:
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }

    private class HotProductsAdapter extends RecyclerView.Adapter<HotProductsAdapter.CourseViewViewHolder> {
        List<Product> hotProducts;
        public HotProductsAdapter(List<Product> hotProducts){
            this.hotProducts = hotProducts;
        }

        @Override
        public CourseViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.product_item, parent, false);
            view.setOnClickListener(clickListener);
            return new CourseViewViewHolder(view);
        }

        private void fillHolder(Product product,CourseViewViewHolder holder){
                holder.productPrice.setText(product.getBasePrice());
                holder.productName.setText(product.getProductName());
            Glide.with(StoreDetailActivity.this).load(product.getImgUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.store).into(holder.productImage);

        }

        @Override
        public void onBindViewHolder(CourseViewViewHolder holder, int position) {
            fillHolder(hotProducts.get(position),holder);
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        @Override
        public int getItemCount() {
            if (hotProducts == null) {
                return 0;
            }
            return hotProducts.size();
        }

        public class CourseViewViewHolder extends RecyclerView.ViewHolder {

            ImageView productImage;
            TextView productName;
            TextView productPrice;
            public CourseViewViewHolder(View itemView) {
                super(itemView);
                productImage = (ImageView) itemView.findViewById(R.id.product_image);
                productName = (TextView) itemView.findViewById(R.id.product_name);
                productPrice = (TextView) itemView.findViewById(R.id.product_price);
            }
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        /**
         * @param space 传入的值，其单位视为dp
         */
        public SpaceItemDecoration(int space) {
            this.mSpace = dip2px(space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
//                Log.d(TAG, "itemCount>>" +itemCount + ";Position>>" + pos);

            outRect.left = mSpace;
            outRect.top = 0;
            outRect.bottom = 0;
            outRect.right = 0;

//
//                if (pos != (itemCount -1)) {
//                    outRect.right = mSpace;
//                } else {
//                    outRect.right = 0;
//                }
        }
    }
}
