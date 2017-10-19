package com.cnsunway.saas.wash.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.Store;
import com.cnsunway.saas.wash.model.StoreDetail;

import java.util.List;


public class OrderStoreAdapter extends BaseAdapter{

    List<Store> stores;
    Context context;
    String fetchTime;
    public interface OnSelectedTime{
        void onSelctedTime();
    }

    private  OnSelectedTime onSelectedTime;

    public OrderStoreAdapter(Context context, List<Store> stores,OnSelectedTime onSelectedTime){
        this.stores = stores;
        this.context = context;
        this.onSelectedTime = onSelectedTime;
    }

    public void setFetchTime(String fetchTime){
        this.fetchTime = fetchTime;
    }

    public void setFetchTimeChaged(String fetchTime){
        this.fetchTime = fetchTime;
        notifyDataSetChanged();
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

    public void clearCheck(){
        for(Store store : stores){
            store.setChecked(false);
        }
//        notifyDataSetChanged();
    }

    public void setCheckStore(int position,String fetchTime){
        clearCheck();
        setFetchTime(fetchTime);
        stores.get(position).setChecked(true);
        notifyDataSetChanged();
    }

    public void uncheckStore(Store store){

    }

    public String getCheckStore(){
        for(Store store : stores){
            if(store.isChecked()){
                return store.getId();
            }
        }
        return "";
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Store store = (Store) getItem(i);
        Holder holder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.order_store,null);
            holder = new Holder();
            holder.storeName = (TextView) view.findViewById(R.id.txt_store_name);
            holder.storeImage = (ImageView) view.findViewById(R.id.image_store);
            holder.storeBox = (CheckBox) view.findViewById(R.id.store_box);
            holder.openTime = (TextView) view.findViewById(R.id.open_time);
            holder.timeTips = (TextView) view.findViewById(R.id.time_tips);
            holder.timeContainer = (LinearLayout) view.findViewById(R.id.time_container);
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }
        if(store.isChecked()){
            holder.storeBox.setChecked(true);
            holder.timeTips.setText("取件时间");
            if(fetchTime != null){
                holder.openTime.setText(fetchTime);
            }
            holder.openTime.setTextColor(Color.parseColor("#20B1D9"));
            holder.timeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onSelectedTime != null){
                        onSelectedTime.onSelctedTime();
                    }
                }
            });
        }else {
            holder.storeBox.setChecked(false);
            holder.timeTips.setText("营业时间"
            );
            holder.openTime.setText(store.getBeginService()+"-" + store.getEndService());
            holder.openTime.setTextColor(Color.parseColor("#444A59"));
            holder.timeContainer.setOnClickListener(null);
        }

        holder.storeName.setText(store.getStoreName());
//        holder.storeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                Toast.makeText(context,"click:" +checked,Toast.LENGTH_SHORT).show();
////
//                clearCheck();
//                store.setChecked(checked);
//                notifyDataSetChanged();
////                if(compoundButton.isChecked()){
////
////                }else {
////                    store.setChecked(false);
////                    notifyDataSetChanged();
////                }
//            }
//        });
        holder.storeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CheckBox box = (CheckBox) view;
                if(box.isChecked()){
                    setCheckStore(i,fetchTime);
                }else {
                    store.setChecked(false);
                    notifyDataSetChanged();
                }
            }
        });
        Glide.with(context).load(store.getHeadPortraitUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.store).into(holder.storeImage);
        return view;
    }

    class Holder{
        public CheckBox storeBox;
       public ImageView storeImage;
        public TextView storeName;
        public TextView openTime;
        public TextView timeTips;
        public LinearLayout timeContainer;

    }
    public String getSelInfo(){

        String result = "";
        String storeId = getCheckStore();
        if(TextUtils.isEmpty(storeId)){
            return result;
        }else {
            result += storeId;
            result += "-"+fetchTime;
        }
        return result;
    }
}
