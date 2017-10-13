package com.cnsunway.saas.wash.adapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.ServiceCity;

import java.util.List;

/**
 * Created by hp on 2017/6/15.
 */
public class ServiceCityAdapter extends BaseAdapter {
    Context context;
    List<ServiceCity> serviceCitys;
    ServiceCity currentCity;

    public ServiceCity getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(ServiceCity currentCity) {
        this.currentCity = currentCity;
    }

    public List<ServiceCity> getServiceCitys() {
        return serviceCitys;
    }

    public void setServiceCitys(List<ServiceCity> serviceCitys) {
        this.serviceCitys = serviceCitys;
    }

    public ServiceCityAdapter(Context context, List<ServiceCity> serviceCitys) {
        this.context = context;
        this.serviceCitys = serviceCitys;
    }

    @Override
    public int getCount() {
        return serviceCitys.size();

    }

    @Override
    public Object getItem(int position) {
        return serviceCitys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ServiceCity serviceCity = (ServiceCity) getItem(position);
        final View view;
        Holder holder;
        if (convertView == null) {
            view = View.inflate(context, R.layout.dialog_city_item, null);
            setViewHolder(view);
        } else {
            view = convertView;
        }
        holder = (Holder) view.getTag();
        //如果是当前定位的城市，显示"当前定位城市"textview

       if(currentCity != null){
           if(currentCity.equals(serviceCity)){
               holder.currentCity.setVisibility(View.VISIBLE);
           }else {
               holder.currentCity.setVisibility(View.INVISIBLE);
           }
       }else {
           holder.currentCity.setVisibility(View.INVISIBLE);
       }


        holder.serviceCity.setText(serviceCity.getCityName());//显示可服务城市
        return view;
    }

    private void setViewHolder(View view) {
        Holder holder = new Holder();
        holder.serviceCity = (TextView) view.findViewById(R.id.tv_service_city);
        holder.currentCity = (TextView) view.findViewById(R.id.tv_current_city);
        view.setTag(holder);

    }
    class Holder {
        TextView currentCity, serviceCity;
    }
}

