package com.cnsunway.saas.wash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.Store;

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
        return 6;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.home_store2,null); //推荐门店
//            view = LayoutInflater.from(context).inflate(R.layout.activity_select_stores_item,null); //选择门店
//            view = LayoutInflater.from(context).inflate(R.layout.activity_user_review_item,null);   //用户评价

//           ViewGroup.LayoutParams lp =  view.getLayoutParams();
//            lp.height = (int) context.getResources().getDimension(R.dimen.banner_height);
//            view.setLayoutParams(lp);
        }
        return view;
    }
}
