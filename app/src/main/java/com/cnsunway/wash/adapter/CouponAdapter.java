package com.cnsunway.wash.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.model.Coupon;
import com.cnsunway.wash.util.FontUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class  CouponAdapter extends BaseAdapter {

    Context context;
    List<Coupon> coupons;
    String validDate, expiredDate;
    String yearV, yearE, monthV, monthE, dateV, dateE;

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public CouponAdapter(Context context, List<Coupon> coupons) {
        this.context = context;
        this.coupons = coupons;
    }

    @Override
    public int getCount() {
        return coupons.size();
    }

    public void setSelectCoupons(String id){
        for(Coupon c : this.coupons){
            if(c.getCouponNo().equals(id)){
                c.setSelected(true);
                break;
            }
        }
//        notifyDataSetChanged();
    }
    @Override
    public Object getItem(int position) {
        return coupons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Coupon coupon = (Coupon) getItem(position);
        Holder holder;
        final View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.a_coupon_item1, null);
            FontUtil.applyFont(context, view, "OpenSans-Regular.ttf");
            setViewHolder(view);

        } else {
            view = convertView;
        }
        holder = (Holder) view.getTag();
        holder.name.setText(coupon.getName());
        holder.description.setText(coupon.getDescription());
        holder.money.setText(coupon.getDenomination());
        validDate = coupon.getValidDate().substring(0, 10);
        expiredDate = coupon.getExpireDate().substring(0, 10);
        if(coupon.isSelected()){
            holder.checked.setVisibility(View.VISIBLE);
        }else {
            holder.checked.setVisibility(View.INVISIBLE);
        }
        if (coupon.isExpireSoon()){
            holder.expire.setVisibility(View.VISIBLE);
        } else {
            holder.expire.setVisibility(View.INVISIBLE);
        }
      holder.validity.setText(context.getText(R.string.valid_time) + " " + validDate + " " + context.getText(R.string.to) + " " + expiredDate);
        if (coupon.getUseChannel() == 0){    //通用
            holder.only.setText("仅限在线支付使用");
//            holder.only.setVisibility(View.GONE);
        }
        if (coupon.getUseChannel() == 1){   //在线支付
            holder.only.setText("仅限在线支付使用");
//            holder.only.setVisibility(View.GONE);
        }
        if (coupon.getUseChannel() == 2){   // 仅限APP使用
            holder.only.setText("仅限APP使用");
        }


        if (coupon.getStatus() == 1) {
        } else if (coupon.getStatus() == 2) {

        } else if (coupon.getStatus() == 3) {
            holder.name.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.description.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.money.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.moneyIcon.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.only.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.couponBg.setBackgroundResource(R.mipmap.coupon_bg2);
        } else if (coupon.getStatus() == 4) {

            holder.name.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.description.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.money.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.moneyIcon.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.only.setTextColor(context.getResources().getColor(R.color.text_gray2));
            holder.couponBg.setBackgroundResource(R.mipmap.coupon_bg2);

        }


        return view;
    }

    private int getCouponStatus(int status) {
        if (status == 1) {
            return Const.CouponStatus.STATUS_COUPON_VALID;
        } else if (status == 2) {
            return Const.CouponStatus.STATUS_COUPON_LOCKED;
        } else if (status == 3) {
            return Const.CouponStatus.STATUS_COUPON_USED;
        } else if (status == 4) {
            return Const.CouponStatus.STATUS_COUPON_EXPIRED;
        }
        return -1;
    }

    public void setViewHolder(View view) {
        CouponAdapter.Holder holder = new CouponAdapter.Holder();

        holder.couponBg = (RelativeLayout) view.findViewById(R.id.rl_coupon_bg);
        holder.checked = (ImageView) view.findViewById(R.id.iv_checked);
        holder.expire = (ImageView) view.findViewById(R.id.iv_expire);

        holder.name = (TextView) view.findViewById(R.id.iv_coupon_name);
        holder.description = (TextView) view.findViewById(R.id.tv_coupon_description);
        holder.validity = (TextView) view.findViewById(R.id.tv_coupon_validity);
        holder.money = (TextView) view.findViewById(R.id.tv_coupon_money);
        holder.moneyIcon = (TextView) view.findViewById(R.id.tv_coupon_icon);
        holder.only = (TextView) view.findViewById(R.id.tv_coupon_only);
        view.setTag(holder);

    }

    class Holder {
        RelativeLayout couponBg;
        ImageView checked,expire;
        TextView money, validity, description,moneyIcon, name,only;

    }
}
