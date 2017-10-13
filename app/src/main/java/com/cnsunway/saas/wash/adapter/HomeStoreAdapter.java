package com.cnsunway.saas.wash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.Store;
import com.cnsunway.saas.wash.util.GlideCircleTransform;

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
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        holder.storeName.setText(store.getStoreName());
        Glide.with(context).load(store.getHeadPortraitUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.store).into(holder.storeImage);

        return view;
    }

    class Holder{
       public ImageView storeImage;
        public TextView storeName;
        public  TextView distance;
    }
}
