package com.cnsunway.saas.wash.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.Store;
import com.cnsunway.saas.wash.util.GlideCircleTransform;
import com.cnsunway.saas.wash.util.NumberUtil;

import java.math.BigDecimal;
import java.util.List;



public class HomeStoreAdapter extends BaseAdapter{

    List<Store> stores;
    Context context;

    public HomeStoreAdapter(Context context,List<Store> stores){
        this.stores = stores;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int i) {
        return stores.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Store store = (Store) getItem(i);
        Holder holder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.home_store2,null); //推荐门店
            holder = new Holder();
            holder.distance = (TextView) view.findViewById(R.id.txt_distance);
            holder.storeName = (TextView) view.findViewById(R.id.txt_store_name);
            holder.storeImage = (ImageView) view.findViewById(R.id.image_store);
            holder.oneScoreImage = (ImageView) view.findViewById(R.id.score_one);
            holder.twoScoreImage = (ImageView) view.findViewById(R.id.score_two);
            holder.threeScoreImage = (ImageView) view.findViewById(R.id.score_three);
            holder.fourScoreImage = (ImageView) view.findViewById(R.id.score_four);
            holder.fiveScoreImage = (ImageView) view.findViewById(R.id.score_five);
            holder.serviceCountText = (TextView) view.findViewById(R.id.text_service_count);
            holder.starText = (TextView) view.findViewById(R.id.text_star);
            holder.feeText = (TextView) view.findViewById(R.id.text_fee);
            holder.chargeParent = (LinearLayout) view.findViewById(R.id.recharge_parent);
            holder.rechargeText = (TextView) view.findViewById(R.id.text_recharge);
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }
        holder.distance.setText(NumberUtil.format1Dicimal(new BigDecimal(store.getDistance()).divide(new BigDecimal(1000)).floatValue()+"")+"km");
        holder.storeName.setText(store.getStoreName());
        Glide.with(context).load(store.getHeadPortraitUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.store).into(holder.storeImage);
        switch (store.getScore()){
            case 0:
                holder.oneScoreImage.setImageResource(R.mipmap.star_gray);
                holder.twoScoreImage.setImageResource(R.mipmap.star_gray);
                holder.threeScoreImage.setImageResource(R.mipmap.star_gray);
                holder.fourScoreImage.setImageResource(R.mipmap.star_gray);
                holder.fiveScoreImage.setImageResource(R.mipmap.star_gray);
                break;
            case 1:
                holder.oneScoreImage.setImageResource(R.mipmap.star_orange);
                holder.twoScoreImage.setImageResource(R.mipmap.star_gray);
                holder.threeScoreImage.setImageResource(R.mipmap.star_gray);
                holder.fourScoreImage.setImageResource(R.mipmap.star_gray);
                holder.fiveScoreImage.setImageResource(R.mipmap.star_gray);
                break;
            case 2:
                holder.oneScoreImage.setImageResource(R.mipmap.star_orange);
                holder.twoScoreImage.setImageResource(R.mipmap.star_orange);
                holder.threeScoreImage.setImageResource(R.mipmap.star_gray);
                holder.fourScoreImage.setImageResource(R.mipmap.star_gray);
                holder.fiveScoreImage.setImageResource(R.mipmap.star_gray);
                break;
            case 3:
                holder.oneScoreImage.setImageResource(R.mipmap.star_orange);
                holder.twoScoreImage.setImageResource(R.mipmap.star_orange);
                holder.threeScoreImage.setImageResource(R.mipmap.star_orange);
                holder.fourScoreImage.setImageResource(R.mipmap.star_gray);
                holder.fiveScoreImage.setImageResource(R.mipmap.star_gray);
                break;
            case 4:
                holder.oneScoreImage.setImageResource(R.mipmap.star_orange);
                holder.twoScoreImage.setImageResource(R.mipmap.star_orange);
                holder.threeScoreImage.setImageResource(R.mipmap.star_orange);
                holder.fourScoreImage.setImageResource(R.mipmap.star_orange);
                holder.fiveScoreImage.setImageResource(R.mipmap.star_gray);
                break;
            case 5:
                holder.oneScoreImage.setImageResource(R.mipmap.star_orange);
                holder.twoScoreImage.setImageResource(R.mipmap.star_orange);
                holder.threeScoreImage.setImageResource(R.mipmap.star_orange);
                holder.fourScoreImage.setImageResource(R.mipmap.star_orange);
                holder.fiveScoreImage.setImageResource(R.mipmap.star_orange);
                break;


        }
        String fee = "订单满 " + store.getFreightRemitAmount() + " 元免 " + store.getFreightAmount() + " 元取送费";
        holder.feeText.setText(fee);
        holder.serviceCountText.setText("已服务 " +store.getServiceCount() + " 次");
        holder.starText.setText(NumberUtil.format1Dicimal(store.getScore()+""));
        if(TextUtils.isEmpty(store.getRechargeInfo())){
            holder.chargeParent.setVisibility(View.GONE);
        }else {
            holder.chargeParent.setVisibility(View.VISIBLE);
            holder.rechargeText.setText(store.getRechargeInfo());
        }
        return view;
    }

    class Holder{
       public ImageView storeImage;
        public TextView storeName;
        public  TextView distance;
        ImageView oneScoreImage;
        ImageView twoScoreImage;
        ImageView threeScoreImage;
        ImageView fourScoreImage;
        ImageView fiveScoreImage;
        TextView serviceCountText;
        TextView starText;
        TextView feeText;
        LinearLayout chargeParent;
        TextView rechargeText;
    }
}
