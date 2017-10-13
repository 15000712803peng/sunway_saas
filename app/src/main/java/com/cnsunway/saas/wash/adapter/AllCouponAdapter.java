package com.cnsunway.saas.wash.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.model.Coupon;
import com.cnsunway.saas.wash.model.CouponTitle;
import com.cnsunway.saas.wash.model.Coupons;
import com.cnsunway.saas.wash.util.FontUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class AllCouponAdapter extends BaseAdapter {
    Context context;
    String validDate, expiredDate;
    final  int TYPE_TITLE = 1;
    final  int TYEP_COUPONS = 2;
    List<Coupon>  mCouponList;
    Coupons usableCoupons;
    public List<Object> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Object> coupons) {
        this.coupons = coupons;
    }
    List<Object> coupons;

    public AllCouponAdapter(Context context, List<Object> coupons) {

        this.context = context;
        this.coupons = coupons;
    }

    @Override
    public int getCount() {
        return coupons.size();
    }

//    public void setSelectCoupons(String id){
//        if (coupons.contains(usableCoupons)) {
//            for(Coupon c : mCouponList){
//                if (c.getCouponNo().equals(id)) {
//                    c.setSelected(true);
//                    break;
//                }
//            }
////        notifyDataSetChanged();
//        }
//    }
    @Override
    public Object getItem(int position) {
        return coupons.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof CouponTitle){
            return TYPE_TITLE;}
       else if (getItem(position) instanceof Coupon) {
            return TYEP_COUPONS;
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        View view = null;
        TitleHolder titleHolder = null;
        Holder couponHoler  = null;
        if(type == TYPE_TITLE){
            CouponTitle date = (CouponTitle) getItem(position);
            if (convertView == null){
                view = View.inflate(context,R.layout.coupon_title_item,null);
                setViewTitleHolder(view);
            }else {
                view = convertView;
            }
            titleHolder = (TitleHolder) view.getTag();
            titleHolder.counts.setText(date.getTitle()+" " + date.getCount() +" 张");
        }else if (type == TYEP_COUPONS) {
            Coupon  coupon = (Coupon) getItem(position);
            Holder holder;
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
//            Log.e("useChannel",coupon.getUseChannel()+"");
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

            holder.money.setText(coupon.getDenomination());
            validDate = coupon.getValidDate().substring(0, 10);
            expiredDate = coupon.getExpireDate().substring(0, 10);
            if (coupon.isSelected()) {
                holder.checked.setVisibility(View.VISIBLE);
            } else {
                holder.checked.setVisibility(View.INVISIBLE);
            }
            if (coupon.isExpireSoon()) {
                holder.expire.setVisibility(View.VISIBLE);
            } else {
                holder.expire.setVisibility(View.INVISIBLE);
            }
            holder.validity.setText(context.getText(R.string.valid_time) + " " + validDate + " " + context.getText(R.string.to) + " " + expiredDate);

            if (coupon.getUsable() == 0){
                holder.name.setTextColor(context.getResources().getColor(R.color.text_gray2));
                holder.description.setTextColor(context.getResources().getColor(R.color.text_gray2));
                holder.money.setTextColor(context.getResources().getColor(R.color.text_gray2));
                holder.moneyIcon.setTextColor(context.getResources().getColor(R.color.text_gray2));
                holder.only.setTextColor(context.getResources().getColor(R.color.text_gray2));
                holder.expire.setImageResource(R.mipmap.have_expired);
            } else {
                //可用
                holder.name.setTextColor(context.getResources().getColor(R.color.text));
                holder.description.setTextColor(context.getResources().getColor(R.color.text));
                holder.only.setTextColor(context.getResources().getColor(R.color.text));
                holder.money.setTextColor(context.getResources().getColor(R.color.red));
                holder.moneyIcon.setTextColor(context.getResources().getColor(R.color.red));
                holder.expire.setImageResource(R.mipmap.expire);
            }

        }



        return view;
    }

    private void setViewTitleHolder(View view) {
        AllCouponAdapter.TitleHolder titleHolder = new AllCouponAdapter.TitleHolder();
        titleHolder.counts = (TextView) view.findViewById(R.id.text_counts);
        view.setTag(titleHolder);
    }

    private int getStatus(int status) {
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
        AllCouponAdapter.Holder holder = new AllCouponAdapter.Holder();

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

    private class TitleHolder {
        TextView counts;
    }
}
